package DodgeballServer;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

/**
 * 
 */
public class DodgeballDaemon implements Runnable {
    private int port;
    private GameManager manager;
    
    /**
     * Constructor for objects of class DodgeballDaemon
     */
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
            }
            catch (IOException e) {
                System.err.println("Error:\n" + e.toString());
                try {
                    listener.close();
                }
                catch (IOException ioe) {
                    System.err.println("Error while closing socket: " + ioe);
                    return;
                }
            }
        }
    }
}
