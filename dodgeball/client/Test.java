package dodgeball.client;

import dodgeball.game.Model3;
import dodgeball.game.Vector3;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Generic tester for graphics.
 */
public class Test implements KeyListener {
  private static boolean up;
  private static boolean down;
  private static boolean left;
  private static boolean right;
  private static boolean fwd;
  private static boolean back;
  private static boolean yawL;
  private static boolean yawR;
  private static boolean pitU;
  private static boolean pitD;
  private static boolean directionRequested;

  /**
   * Test graphics.
   *
   * @param args Command line arguments
   * @throws IOException If file reading fails.
   */
  public static void main(String[] args) throws IOException {
    Model3 model = new Model3(new File(new File("").getAbsolutePath()
        + "/dodgeball/client/assets/Dodgeball.md3"));
    model.translate(new Vector3(0, 1.5, 0));
    
    GameWindow window = new GameWindow(1200, 1080, null);
    window.addModel(model);
    window.addKeyListener(new Test());
    window.setCameraPosition(new Vector3(-3, 1.5, 0));
    window.setCameraDirection(Vector3.I);

    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    scheduler.scheduleAtFixedRate(new Runnable() {
      public void run() {
        update(window);
      }
    }, 0, 33, TimeUnit.MILLISECONDS);
  }

  private static void update(GameWindow window) {
    if (directionRequested) {
      System.out.println(window.cameraPosition());
      System.out.println(window.cameraDirection());
      System.out.println(window.cameraHorizontal());
      System.out.println(window.cameraVertical());
      System.out.println(window.cameraDirection().dot(window.cameraHorizontal()));
      System.out.println();
      directionRequested = false;
    }

    window.translateCamera(window.cameraHorizontal().multiply(((left ? -0.033 : 0)
        + (right ? 0.033 : 0)) * 20));
    window.translateCamera(window.cameraDirection().multiply((fwd ? 0.033 : 0)
        + (back ? -0.033 : 0)));
    window.translateCamera(Vector3.J.multiply((down ? -0.033 : 0) + (up ? 0.033 : 0)));

    window.rotateCamera(
        (yawL ? -1 : 0) + (yawR ? 1 : 0),
        (pitU ? 1 : 0) + (pitD ? -1 : 0));
    
    window.render();
  }

  @Override
  public void keyTyped(KeyEvent e) {
  }

  @Override
  public void keyPressed(KeyEvent e) {
    switch (e.getKeyCode()) {
      case (KeyEvent.VK_A):
        left = true;
        break;
      case (KeyEvent.VK_S):
        back = true;
        break;
      case (KeyEvent.VK_D):
        right = true;
        break;
      case (KeyEvent.VK_E):
        up = true;
        break;
      case (KeyEvent.VK_W):
        fwd = true;
        break;
      case (KeyEvent.VK_Q):
        down = true;
        break;
      case (KeyEvent.VK_UP):
        pitU = true;
        break;
      case (KeyEvent.VK_DOWN):
        pitD = true;
        break;
      case (KeyEvent.VK_LEFT):
        yawL = true;
        break;
      case (KeyEvent.VK_RIGHT):
        yawR = true;
        break;
      case (KeyEvent.VK_C):
        directionRequested = true;
        break;
      default:
        break;
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
    switch (e.getKeyCode()) {
      case (KeyEvent.VK_A):
        left = false;
        break;
      case (KeyEvent.VK_S):
        back = false;
        break;
      case (KeyEvent.VK_D):
        right = false;
        break;
      case (KeyEvent.VK_E):
        up = false;
        break;
      case (KeyEvent.VK_W):
        fwd = false;
        break;
      case (KeyEvent.VK_Q):
        down = false;
        break;
      case (KeyEvent.VK_UP):
        pitU = false;
        break;
      case (KeyEvent.VK_DOWN):
        pitD = false;
        break;
      case (KeyEvent.VK_LEFT):
        yawL = false;
        break;
      case (KeyEvent.VK_RIGHT):
        yawR = false;
        break;
      default:
        break;
    }
  }
}
