package dodgeball.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Spawns <code>dodgeballserver.ClientHandler</code> instances.
 */
public class DodgeballDaemon implements Runnable {
  private int port;
  private GameManager manager;
  
  public DodgeballDaemon(int port, GameManager manager) {
    this.port = port;
    this.manager = manager;
  }

  @Override
  public void run() {
    ServerSocket listener = null;
    try {
      listener = new ServerSocket(port);
      System.out.println("Your host name: " + InetAddress.getLocalHost().getHostName());
    } catch (IOException e) {
      e.printStackTrace();
      return;
    }
    while (true) {
      try {
        Socket socket = listener.accept();
        Player player = new Player();
        manager.addPlayer(player);
        new Thread(new ClientHandler(manager, socket, player)).start();
      } catch (IOException e) {
        e.printStackTrace();
        try {
          listener.close();
        } catch (IOException ioe) {
          e.printStackTrace();
          return;
        }
      }
    }
  }
}
