package DodgeballServer;

import java.util.ArrayList;
import java.util.List;
import java.net.Socket;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import Game3D.Vector2;
import Game3D.Vector3;

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
     * Constructor for objects of class DodgeballDaemon
     */
    public ClientHandler(GameManager manager, Socket socket, Player player) throws IOException {
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
        
        long time = System.currentTimeMillis();
        while (true) {
            try {
                long ping = System.currentTimeMillis()-time;
                if (ping < 33) {
                    Thread.sleep(33 - ping);
                }
                time = System.currentTimeMillis();

                // Write game data: Player pos, Player dir, Dodgeball pos
                writeVector3(player.center().add(new Vector3(0, Player.HEIGHT/2.0, 0)));
                writeVector3(player.lookVector());

                List<Player> players  = manager.players();
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
                List<Dodgeball> dodgeballs  = manager.dodgeballs();
                dodgeballs = new ArrayList<Dodgeball>(dodgeballs);
                output.writeInt(dodgeballs.size());
                for (Dodgeball db : dodgeballs) {
                    writeVector3(db.position());
                }
                
                // Read input data
                boolean playing = input.readBoolean();
                boolean w = input.readBoolean();
                boolean a = input.readBoolean();
                boolean s = input.readBoolean();
                boolean d = input.readBoolean();
                boolean space = input.readBoolean();
                boolean click = input.readBoolean();
                double mouseX = input.readDouble();
                double mouseY = input.readDouble();
                
                if (!playing) {
                    manager.removePlayer(player);
                    System.out.println("Player left");
                    break;
                }
                
                // Update the central server
                player.inputData().setW(w);
                player.inputData().setA(a);
                player.inputData().setS(s);
                player.inputData().setD(d);
                player.inputData().setSpace(space);
                player.inputData().setThrowingDodgeball(click);
                player.inputData().setMouseX(mouseX);
                player.inputData().setMouseY(mouseY);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    /** 
     * @param vector
     * @throws IOException
     */
    private void writeVector3(Vector3 vector) throws IOException {
        output.writeDouble(vector.X);
        output.writeDouble(vector.Y);
        output.writeDouble(vector.Z);
    }
    
    private void writeVector2(Vector2 vector) throws IOException {
        output.writeDouble(vector.X);
        output.writeDouble(vector.Y);
    }
}
