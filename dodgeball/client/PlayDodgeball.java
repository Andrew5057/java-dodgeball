package dodgeball.client;

import java.awt.AWTException;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Join a game of dodgeball.
 */
public class PlayDodgeball {
  /**
   * Play a game of dodgeball.
   *
   * @param args Command line args
   * @throws AWTException If a drawing fails
   * @throws FileNotFoundException If modeling fails
   * @throws IOException If server communications fail
   * @throws InterruptedException If sleep gets interrupted
   */
  public static void main(String[] args) throws AWTException, FileNotFoundException, IOException, 
      InterruptedException {
    System.out.println("\nWelcome to Dodgeball!\n");
    System.out.println("There are no rules here, so yall are gonna have to make up the rules "
        + "yourselves.\n");
    System.out.println("I accept no responsibility if this bricks your computer, sets your desk "
        + "on fire, or causes any other property damage.\n");
    System.out.println("GLHF!\n");
    
    Client client = new Client();
    new Thread(client).start();
  }
}