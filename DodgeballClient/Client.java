package DodgeballClient;

import Game3D.Model3;
import Game3D.Vector2;
import Game3D.Vector3;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Robot;
import java.awt.Toolkit;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * The client-side program responsible for handling input and graphics for a game of Dodgeball.
 *
 * @author Andrew Yim
 * @version 3-20-2024
 */
public class Client implements Runnable {
    private GameWindow window;
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;
    private PlayerInput playerInput;
    private Robot robot;
    private static Model3 playerModel, dodgeballModel;
    private boolean playing;
    public static final int MS_PER_FRAME = 33;
    
    public Client() throws FileNotFoundException, IOException, AWTException {
        String rootPath = new File("").getAbsolutePath();
        if (playerModel == null) {
            // playerModel = new Model3(new File(rootPath + "DodgeballClient/Assets/Player.md3"));
        }
        if (dodgeballModel == null) {
            dodgeballModel = new Model3(new File(rootPath + "DodgeballClient/Assets/Dodgeball.md3"));
        }
        robot = new Robot();
    }
    
    @Override
    public void run() {
        if (!connectToServer()) { return; }
        createWindow();

        playing = true;
        long time = System.currentTimeMillis();
        try {
            while (true) {
                window.clear();

                long ping = System.currentTimeMillis() - time;
                if (ping < MS_PER_FRAME) {
                    Thread.sleep(MS_PER_FRAME - ping);
                } else {
                    for (int i = 1; time+MS_PER_FRAME*i < System.currentTimeMillis(); i++) {
                        readVector3();
                        readVector3();
                        int n = input.readInt();
                        readManyVector3s(n);
                        readManyVector2s(n);
                        readManyVector3s(input.readInt());
                        writeInfo();
                    }
                }
                time = System.currentTimeMillis();

                Vector3 myPos = readVector3();
                Vector3 myDir = readVector3();
                window.setCameraPosition(myPos);
                window.setCameraDirection(myDir);
                
                int numPlayers = input.readInt();
                List<Vector3> playerPositions = readManyVector3s(numPlayers);
                List<Vector2> playerDirections = readManyVector2s(numPlayers);
                addPlayerModels(playerPositions, playerDirections);
                
                int numDodgeballs = input.readInt();
                List<Vector3> dodgeballPositions = readManyVector3s(numDodgeballs);
                addDodgeballModels(dodgeballPositions);
                
                writeInfo();

                if (playerInput.isFocused()) {
                    robot.mouseMove((int) (window.getSize().getWidth() / 2.0), (int) (window.getSize().getHeight() / 2.0));
                }

                playerInput.releaseLClick();

                window.repaint();

                if (!playing) {
                    socket.close();
                    return;
                }
            }
        } catch (IOException e) {
            System.err.println(e);
        } catch (InterruptedException e) {
            System.err.println(e);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }

    public void quit() {
        playing = false;
    }



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
                System.out.println("Host "  + hostId +
                        " could not be found. Enter the host name: ");
                hostId = reader.nextLine();
                try {
                    socket = new Socket(hostId, 8080);
                    found = true;
                } catch (UnknownHostException uhe) {} catch (IOException ioe) {}
            } catch (IOException e) {
                System.err.println(e);
                reader.close();
                return false;
            }
        }
        reader.close();

        try {
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.err.println("Error:\n" + e);
            return false;
        }

        return true;
    }

    private void createWindow() {
        int res = Toolkit.getDefaultToolkit().getScreenResolution();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        double width = dim.getWidth();
        double height = dim.getHeight();
        window = new GameWindow((int)(res*width), (int)(res*height), this);
        playerInput = new PlayerInput(window);
    }



    private void addPlayerModels(List<Vector3> positions, List<Vector2> directions) {
        for (int i = 0; i < positions.size(); i++) {
            Vector3 pos = positions.get(i);
            Vector2 dir = directions.get(i);
            
            Model3 playerToDraw = playerModel.clone();
            playerToDraw.translate(pos);
            
            double lookAngle = Math.acos(dir.dot(Vector2.I));
            playerToDraw.rotate(lookAngle);
            window.addModel(playerToDraw);
        }
    }

    private void addDodgeballModels(List<Vector3> positions) {
        for (int i = 0; i < positions.size(); i++) {
            Vector3 pos = positions.get(i);

            Model3 dodgeballToDraw = dodgeballModel.clone();
            dodgeballToDraw.translate(pos);
            
            window.addModel(dodgeballToDraw);
        }
    }

    private void writeInfo() throws IOException {
        output.writeBoolean(playing);
        output.writeBoolean(playerInput.wDown());
        output.writeBoolean(playerInput.aDown());
        output.writeBoolean(playerInput.sDown());
        output.writeBoolean(playerInput.dDown());
        output.writeBoolean(playerInput.spaceDown());
        output.writeBoolean(playerInput.lClickDown());
        double absoluteX = playerInput.mouseX();
        double relativeX = absoluteX - window.getSize().getWidth() / 2.0;
        output.writeDouble(relativeX);
        double absoluteY = playerInput.mouseY();
        double relativeY = -absoluteY + window.getSize().getHeight() / 2.0;
        output.writeDouble(relativeY);
    }



    private Vector3 readVector3() throws IOException {
        double x = input.readDouble();
        double y = input.readDouble();
        double z = input.readDouble();
        return new Vector3(x, y, z);
    }
    private Vector2 readVector2() throws IOException {
        double x = input.readDouble();
        double y = input.readDouble();
        return new Vector2(x, y);
    }
    
    private List<Vector3> readManyVector3s(int numVectors) throws IOException {
        List<Vector3> vectors = new ArrayList<Vector3>();
        for (int i = 0; i < numVectors; i++) {
            vectors.add(readVector3());
        }
        return vectors;
    }
    private List<Vector2> readManyVector2s(int numVectors) throws IOException {
        List<Vector2> vectors = new ArrayList<Vector2>();
        for (int i = 0; i < numVectors; i++) {
            vectors.add(readVector2());
        }
        return vectors;
    }
}
