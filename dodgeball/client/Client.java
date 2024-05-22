package dodgeball.client;

import dodgeball.game.Model3;
import dodgeball.game.Vector2;
import dodgeball.game.Vector3;
import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Robot;
import java.awt.Toolkit;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * The client-side program responsible for handling input and graphics for a
 * game of Dodgeball.
 *
 * @author Andrew Yim
 * @version 3-20-2024
 */
public class Client implements Runnable {
  private static Model3 playerModel;
  private static Model3 dodgeballModel;
  private static final int MS_PER_FRAME = 33;

  private GameWindow window;
  private boolean playing;
  private PlayerInput playerInput;
  private Robot robot;
  private Socket socket;
  private DataInputStream input;
  private DataOutputStream output;

  /**
   * Create a new <code>Client</code> object without attempting to run it.
   * Generates a new Robot for mouse movement as well as attempting to load models if they have not
   * been loaded yet.
   *
   * @throws FileNotFoundException if the model files have been misplaced.
   * @throws IOException if the model files cannot be read.
   * @throws AWTException if a Robot cannot be instantiated.
   */
  public Client() throws FileNotFoundException, IOException, AWTException {
    String rootPath = new File("").getAbsolutePath();
    if (playerModel == null) {
      playerModel = new Model3(new File(rootPath + "/dodgeball/client/assets/Player.md3"));
    }
    if (dodgeballModel == null) {
      dodgeballModel = new Model3(new File(rootPath + "/dodgeball/client/assets/Dodgeball.md3"));
    }
    robot = new Robot();
  }

  /**
   * Join and play a game of Dodgeball.
   */
  @Override
  public void run() {
    if (!connectToServer()) {
      return;
    }
    createWindow();

    playing = true;
    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    scheduler.scheduleAtFixedRate(new Runnable() {
      public void run() {
        try {
          update();
        } catch (IOException e) {
          e.printStackTrace();
          try {
            socket.close();
          } catch (IOException ioe) {
            ioe.printStackTrace();
          }
          System.exit(0);
        }
      }
    }, 0, MS_PER_FRAME, TimeUnit.MILLISECONDS);
  }

  private void update() throws IOException {
    Vector3 myPos = readVector3();
    Vector3 myDir = readVector3();
    window.setCameraPosition(myPos);
    window.setCameraDirection(myDir);

    List<Model3> models = new ArrayList<Model3>();

    int numPlayers = input.readInt();
    List<Vector3> playerPositions = readManyVector3s(numPlayers);
    List<Vector2> playerDirections = readManyVector2s(numPlayers);
    addPlayerModels(playerPositions, playerDirections, models);

    int numDodgeballs = input.readInt();
    List<Vector3> dodgeballPositions = readManyVector3s(numDodgeballs);
    addDodgeballModels(dodgeballPositions, models);

    writeInfo();

    if (playerInput.isFocused()) {
      robot.mouseMove((int) (window.getSize().getWidth() / 2.0),
          (int) (window.getSize().getHeight() / 2.0));
    }

    playerInput.releaseLeftClick();

    window.setModels(models);
    window.render();

    if (!playing) {
      socket.close();
      System.exit(0);
    }
  }

  /**
   * Clarify that the player is no longer playing. Should be called rather than
   * force-closing whenever possible.
   */
  public void quit() {
    playing = false;
  }

  /**
   * Attempt to connect to a Dodgeball host. Ask for a host name and attempt to
   * connect to that host; if the
   * host cannot be found, ask for the user to re-enter.
   *
   * @return <code>true</code> if a connection could be made, <code>false</code>
   *         otherwise.
   */
  private boolean connectToServer() {
    Scanner reader = new Scanner(System.in);

    System.out.println("Enter the host name: ");
    String hostId = reader.nextLine();
    boolean found = false;
    while (!found) {
      try {
        socket = new Socket(hostId, 8080);
        found = true;
      } catch (UnknownHostException e) {
        System.out.println("Host " + hostId
            + " could not be found. Enter the host name: ");
        hostId = reader.nextLine();
        try {
          socket = new Socket(hostId, 8080);
          found = true;
        } catch (UnknownHostException uhe) {
          ;
        } catch (IOException ioe) {
          ;
        }
      } catch (IOException e) {
        e.printStackTrace();
        reader.close();
        return false;
      }
    }
    reader.close();

    try {
      input = new DataInputStream(socket.getInputStream());
      output = new DataOutputStream(socket.getOutputStream());
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }

    return true;
  }

  /**
   * Initialize a new GameWindow and corresponding input handler based on the
   * player's screen size.
   */
  private void createWindow() {
    int res = Toolkit.getDefaultToolkit().getScreenResolution();
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    double width = dim.getWidth();
    double height = dim.getHeight();
    window = new GameWindow((int) (res * width), (int) (res * height), this);
    playerInput = new PlayerInput(window);
  }

  /**
   * Add a number of player models to a <code>List</code> of <code>Model3</code>
   * objects.
   *
   * @param positions  A <code>List</code> of <code>Vector3</code> objects
   *                   representing the players' centers.
   * @param directions A <code>List</code> of <code>Vector3</code> objects
   *                   representing the players' look vectors.
   * @param models     The <code>List&lt;Model33&gt;</code> that new models should
   *                   be added to.
   */
  private void addPlayerModels(List<Vector3> positions, List<Vector2> directions,
      List<Model3> models) {
    for (int i = 0; i < positions.size(); i++) {
      Vector3 pos = positions.get(i);
      Vector2 dir = directions.get(i);

      Model3 playerToDraw = playerModel.clone();
      playerToDraw.translate(pos);

      double lookAngle = Math.acos(dir.dot(Vector2.I));
      playerToDraw.rotate(lookAngle);
      models.add(playerToDraw);
    }
  }

  /**
   * Add a number of dodgeball models to a <code>List</code> of
   * <code>Model3</code> objects.
   *
   * @param positions A <code>List</code> of <code>Vector3</code> objects
   *                  representing the dodgeballs' centers.
   * @param models    The <code>List&lt;Model33&gt;</code> that new models should
   *                  be added to.
   */
  private void addDodgeballModels(List<Vector3> positions, List<Model3> models) {
    for (int i = 0; i < positions.size(); i++) {
      Vector3 pos = positions.get(i);

      Model3 dodgeballToDraw = dodgeballModel.clone();
      dodgeballToDraw.translate(pos);
      models.add(dodgeballToDraw);
    }
  }

  /**
   * Write player data to the current output stream. Writes, in order:
   * 1. A <code>boolean</code> value representing whether or not the player is
   * playing.
   * 2. Six <code>boolean</code> values representing whether the w, a, s, d,
   * space, and left-click buttons are being
   * held, respectively.
   * 3. Two <code>double</code> values representing the mouse's x and y positions,
   * respectively, where 0 is the center,
   * -1 is the top or left, and 1 is the bottom or right.
   *
   * @throws IOException if data cannot be written to the output stream.
   */
  private void writeInfo() throws IOException {
    output.writeBoolean(playing);
    output.writeBoolean(playerInput.wdown());
    output.writeBoolean(playerInput.adown());
    output.writeBoolean(playerInput.sdown());
    output.writeBoolean(playerInput.ddown());
    output.writeBoolean(playerInput.spaceDown());
    output.writeBoolean(playerInput.leftClickDown());
    double absoluteX = playerInput.mouseX();
    double relativeX = absoluteX - window.getSize().getWidth() / 2.0;
    output.writeDouble(relativeX);
    double absoluteY = playerInput.mouseY();
    double relativeY = -absoluteY + window.getSize().getHeight() / 2.0;
    output.writeDouble(relativeY);
  }

  /**
   * Construct a Vector3 based on the next three <code>double</code> values from
   * the current input stream.
   *
   * @return A new <code>Vector3</code> object based on the current input stream's
   *         next three <code>double</code> values.
   * @throws IOException if data cannot be read from the input stream.
   */
  private Vector3 readVector3() throws IOException {
    double x = input.readDouble();
    double y = input.readDouble();
    double z = input.readDouble();
    return new Vector3(x, y, z);
  }

  /**
   * Construct a Vector2 based on the next two <code>double</code> values from the
   * current input stream.
   *
   * @return A new <code>Vector2</code> object based on the current input stream's
   *         next two <code>double</code> values.
   * @throws IOException if data cannot be read from the input stream.
   */
  private Vector2 readVector2() throws IOException {
    double x = input.readDouble();
    double y = input.readDouble();
    return new Vector2(x, y);
  }

  /**
   * Read an arbitrary number of <code>Vector3</code> objects from the current
   * input stream.
   *
   * @param numVectors The number of <code>Vector3</code> objects that should be
   *                   read.
   * @return A <code>List&lt;Vector3&gt;</code> containing the next
   *         <code>numVectors</code> numbers from the input stream..
   * @throws IOException if data cannot be read from the input stream.
   */
  private List<Vector3> readManyVector3s(int numVectors) throws IOException {
    List<Vector3> vectors = new ArrayList<Vector3>();
    for (int i = 0; i < numVectors; i++) {
      vectors.add(readVector3());
    }
    return vectors;
  }

  /**
   * Read an arbitrary number of <code>Vector2</code> objects from the current
   * input stream.
   *
   * @param numVectors The number of <code>Vector3</code> objects that should be
   *                   read.
   * @return A <code>List&lt;Vector2&gt;</code> containing the next
   *         <code>numVectors</code> numbers from the input stream..
   * @throws IOException if data cannot be read from the input stream.
   */
  private List<Vector2> readManyVector2s(int numVectors) throws IOException {
    List<Vector2> vectors = new ArrayList<Vector2>();
    for (int i = 0; i < numVectors; i++) {
      vectors.add(readVector2());
    }
    return vectors;
  }
}
