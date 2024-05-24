package dodgeball.server;

/**
 * Host a game of Dodgeball from this computer.
 */
public class RunDodgeball {
  /**
   * Host a game of dodgeball.
   *
   * @param args Command line arguments
   */
  public static void main(String[] args) {
    System.out.println("\nNow hosting a game of dodgeball. Thank you for your sacrifice!\n");

    GameManager manager = new GameManager();
    new Thread(manager).start();
  }
}
