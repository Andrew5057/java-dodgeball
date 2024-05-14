package DodgeballClient;

import Game3D.*;
import java.io.File;
import java.io.IOException;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Test implements KeyListener {
    private static boolean up, down, left, right, fwd, back;
    private static boolean yawL, yawR, pitU, pitD;
    private static boolean directionRequested;
    
    
    /** 
     * @param args
     */
    public static void main(String[] args) throws IOException {
        Model3 model = new Model3(new File(new File("").getAbsolutePath() + "/DodgeballClient/Assets/Dodgeball.md3"));
        
        GameWindow window = new GameWindow(1200, 1080, null);
        window.addModel(model);
        window.repaint();
        window.addKeyListener(new Test());
        long time = System.currentTimeMillis();
        while (true) {
            long ping = System.currentTimeMillis() - time;
            try {
                if (ping < 33) {
                    Thread.sleep(33 - ping);
                }
            } catch (Exception e) {
                System.err.println(e);
            }
            time = System.currentTimeMillis();
            if (directionRequested) {
                System.out.println(window.cameraPosition());
                System.out.println(window.cameraDirection());
                System.out.println(window.cameraHorizontal());
                System.out.println(window.cameraVertical());
                System.out.println(window.cameraDirection().dot(window.cameraHorizontal()));
                System.out.println();
                directionRequested = false;
            }
            
            window.translateCamera(window.cameraHorizontal().multiply(((left ? -1 : 0) + (right ? 1 : 0)) * 20));
            window.translateCamera(window.cameraDirection().multiply((fwd ? 1 : 0) + (back ? -1 : 0)));
            window.translateCamera(new Vector3(0, 1, 0).multiply((down ? -1 : 0) + (up ? 1 : 0)));
            
            window.rotateCamera(
                (yawL ? -1 : 0) + (yawR ? 1 : 0),
                (pitU ? 1 : 0) + (pitD ? -1 : 0)
            );
            
            window.repaint();
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e) { }
    
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case (KeyEvent.VK_A): left = true; break;
            case (KeyEvent.VK_S): back = true; break;
            case (KeyEvent.VK_D): right = true; break;
            case (KeyEvent.VK_E): up = true; break;
            case (KeyEvent.VK_W): fwd = true; break;
            case (KeyEvent.VK_Q): down = true; break;
            case (KeyEvent.VK_UP): pitU = true; break;
            case (KeyEvent.VK_DOWN): pitD = true; break;
            case (KeyEvent.VK_LEFT): yawL = true; break;
            case (KeyEvent.VK_RIGHT): yawR = true; break;
            case (KeyEvent.VK_C): directionRequested = true; break;
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case (KeyEvent.VK_A): left = false; break;
            case (KeyEvent.VK_S): back = false; break;
            case (KeyEvent.VK_D): right = false; break;
            case (KeyEvent.VK_E): up = false; break;
            case (KeyEvent.VK_W): fwd = false; break;
            case (KeyEvent.VK_Q): down = false; break;
            case (KeyEvent.VK_UP): pitU = false; break;
            case (KeyEvent.VK_DOWN): pitD = false; break;
            case (KeyEvent.VK_LEFT): yawL = false; break;
            case (KeyEvent.VK_RIGHT): yawR = false; break;
        }
    }
}
