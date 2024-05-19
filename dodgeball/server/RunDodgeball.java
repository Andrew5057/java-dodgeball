package dodgeball.server;

/**
 * Host a game of Dodgeball from this computer.
 */
public class RunDodgeball {
  public static void main(String[] args) {
    GameManager manager = new GameManager();
    new Thread(manager).start();
  }
}
