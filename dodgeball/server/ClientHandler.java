package dodgeball.server;

import dodgeball.game.Vector2;
import dodgeball.game.Vector3;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Handles commmunications between individual clients and the server.
 */
public class ClientHandler implements Runnable {
  private Player player;
  private GameManager manager;
  private Socket socket;
  private DataInputStream input;
  private DataOutputStream output;

  /**
   * Construct a new client handler.
   *
   * @param manager The <code>dodgeball.server.GameManager</code> that spawned this handler.
   * @param socket The <code>jave.net.Socket</code> that this handler should use to communicate.
   * @param player The <code>dodgeball.server.Player</code> that this handler handles.
   */
  public ClientHandler(GameManager manager, Socket socket, Player player) {
    this.player = player;
    this.manager = manager;
    this.socket = socket;
  }

  @Override
  public void run() {
    try {
      input = new DataInputStream(socket.getInputStream());
      output = new DataOutputStream(socket.getOutputStream());
    } catch (Exception e) {
      e.printStackTrace();
      return;
    }

    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    scheduler.scheduleAtFixedRate(() -> {
      if (!update()) {
        scheduler.shutdown();
      }
    }, 0, 33, TimeUnit.MILLISECONDS);
  }

  private boolean update() {
    try {
      output.writeBoolean(player.hit());

      writeVector3(player.headPosition());
      writeVector3(player.lookVector());

      writePlayers();
      writeDodgeballs();

      if (!readPlayerInput()) {
        return false;
      }
    } catch (IOException e) {
      e.printStackTrace();
      try {
        socket.close();
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
      System.exit(0);
    }
    return true;
  }

  private void writePlayers() throws IOException {
    List<Player> players = manager.players();
    players = new ArrayList<Player>(players);
    output.writeInt(players.size() - 1);
    for (Player p : players) {
      if (p == player) {
        continue;
      }
      writeVector3(p.center());
    }
    for (Player p : players) {
      if (p == player) {
        continue;
      }
      writeVector2(p.lookVector().flatten());
    }
  }

  private void writeDodgeballs() throws IOException {
    List<Dodgeball> dodgeballs = manager.dodgeballs();
    dodgeballs = new ArrayList<Dodgeball>(dodgeballs);
    output.writeInt(dodgeballs.size());
    for (Dodgeball db : dodgeballs) {
      writeVector3(db.position());
    }
  }

  /** Return true if the player is still playing; false otherwise. */
  private boolean readPlayerInput() throws IOException {
    boolean playing = input.readBoolean();
    if (!playing) {
      manager.removePlayer(player);
      System.out.println("Player left");
      return false;
    }

    boolean w = input.readBoolean();
    boolean a = input.readBoolean();
    boolean s = input.readBoolean();
    boolean d = input.readBoolean();
    boolean space = input.readBoolean();
    boolean c = input.readBoolean();
    boolean click = input.readBoolean();
    double mouseX = input.readDouble();
    double mouseY = input.readDouble();

    // Update the central server
    player.inputData().setW(w);
    player.inputData().setA(a);
    player.inputData().setS(s);
    player.inputData().setD(d);
    player.inputData().setSpace(space);
    player.inputData().setC(c);
    player.inputData().setThrowingDodgeball(click);
    player.inputData().setMouseX(mouseX);
    player.inputData().setMouseY(mouseY);

    return true;
  }
  
  private void writeVector3(Vector3 vector) throws IOException {
    output.writeDouble(vector.xcoord);
    output.writeDouble(vector.ycoord);
    output.writeDouble(vector.zcoord);
  }

  private void writeVector2(Vector2 vector) throws IOException {
    output.writeDouble(vector.xcoord);
    output.writeDouble(vector.ycoord);
  }
}
