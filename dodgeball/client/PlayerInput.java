package dodgeball.client;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Handle keyboard input for a client.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class PlayerInput implements KeyListener, MouseListener {
  private boolean wdown;
  private boolean adown;
  private boolean sdown;
  private boolean ddown;
  private boolean spaceDown;
  private boolean cdown;
  private boolean leftClickDown;
  private boolean focused;

  /**
   * Create a new player input handler and attach it to a <code>dodgeball.client.GameWindow</code>.
   *
   * @param window The game window that this should be attached to.
   */
  public PlayerInput(GameWindow window) {
    wdown = false;
    adown = false;
    sdown = false;
    ddown = false;
    spaceDown = false;
    cdown = false;
    leftClickDown = false;
    focused = true;
    window.addKeyListener(this);
    window.addMouseListener(this);
  }

  public boolean wdown() {
    return wdown;
  }

  public boolean adown() {
    return adown;
  }

  public boolean sdown() {
    return sdown;
  }

  public boolean ddown() {
    return ddown;
  }

  public boolean spaceDown() {
    return spaceDown;
  }

  public boolean cdown() {
    return cdown;
  }

  public boolean leftClickDown() {
    return leftClickDown;
  }

  public boolean isFocused() {
    return focused;
  }

  public void releaseLeftClick() {
    leftClickDown = false;
  }
  
  public double mouseX() {
    Point location = MouseInfo.getPointerInfo().getLocation();
    return location.getX();
  }

  public double mouseY() {
    Point location = MouseInfo.getPointerInfo().getLocation();
    return location.getY();
  }

  @Override
  public void keyPressed(KeyEvent event) {
    switch (event.getKeyCode()) {
      case KeyEvent.VK_W:
        wdown = true;
        break;
      case KeyEvent.VK_A:
        adown = true;
        break;
      case KeyEvent.VK_S:
        sdown = true;
        break;
      case KeyEvent.VK_D:
        ddown = true;
        break;
      case KeyEvent.VK_SPACE:
        spaceDown = true;
        break;
      case KeyEvent.VK_C:
        cdown = true;
        break;
      default:
        break;
    }
  }
  
  @Override
  public void keyReleased(KeyEvent event) {
    switch (event.getKeyCode()) {
      case KeyEvent.VK_W:
        wdown = false;
        break;
      case KeyEvent.VK_A:
        adown = false;
        break;
      case KeyEvent.VK_S:
        sdown = false;
        break;
      case KeyEvent.VK_D:
        ddown = false;
        break;
      case KeyEvent.VK_SPACE:
        spaceDown = false;
        break;
      case KeyEvent.VK_C:
        cdown = false;
        break;
      default:
        break;
    }
  }

  @Override
  public void keyTyped(KeyEvent event) {
  }

  @Override
  public void mousePressed(MouseEvent event) {
    if (event.getButton() == MouseEvent.BUTTON1) {
      leftClickDown = true;
    }
  }

  @Override
  public void mouseExited(MouseEvent event) {
    focused = false;
  }
  
  @Override
  public void mouseEntered(MouseEvent event) {
    focused = true;
  }
  
  @Override
  public void mouseReleased(MouseEvent event) {
  }
  
  @Override
  public void mouseClicked(MouseEvent event) {
  }
}
