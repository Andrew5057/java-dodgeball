package DodgeballClient;

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
    // instance variables - replace the example below with your own
    private boolean wDown, aDown, sDown, dDown, spaceDown, lClickDown, focused;

    public PlayerInput(GameWindow window) {
        wDown = false;
        aDown = false;
        sDown = false;
        dDown = false;
        spaceDown = false;
        lClickDown = false;
        focused = true;
        window.addKeyListener(this);
        window.addMouseListener(this);
    }
    
    public boolean wDown() { return wDown; }
    public boolean aDown() { return aDown; }
    public boolean sDown() { return sDown; }
    public boolean dDown() { return dDown; }
    public boolean spaceDown() { return spaceDown; }
    public boolean lClickDown() { return lClickDown; }
    public boolean isFocused() { return focused; }

    public void releaseLClick() {
        lClickDown = false;
    }
    
    /** 
     * @return double
     */
    public double mouseX() {
        Point location = MouseInfo.getPointerInfo().getLocation();
        return location.getX();
    }
    public double mouseY() {
        Point location = MouseInfo.getPointerInfo().getLocation();
        return location.getY();
    }

    public void keyPressed(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.VK_W:
                wDown = true;
                break;
            case KeyEvent.VK_A:
                aDown = true;
                break;
            case KeyEvent.VK_S:
                sDown = true;
                break;
            case KeyEvent.VK_D:
                dDown = true;
                break;
            case KeyEvent.VK_SPACE:
                spaceDown = true;
                break;
        }
    }
    public void keyReleased(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.VK_W:
                wDown = false;
                break;
            case KeyEvent.VK_A:
                aDown = false;
                break;
            case KeyEvent.VK_S:
                sDown = false;
                break;
            case KeyEvent.VK_D:
                dDown = false;
                break;
            case KeyEvent.VK_SPACE:
                spaceDown = false;
                break;
        }
    }

    public void keyTyped(KeyEvent event) {}

    public void mousePressed(MouseEvent event) {
        if (event.getButton() == MouseEvent.BUTTON1) {
            lClickDown = true;
        }
    }

    public void mouseExited(MouseEvent event) {
        focused = false;
    }
    public void mouseEntered(MouseEvent event) {
        focused = true;
    }

    public void mouseReleased(MouseEvent event) {}
    public void mouseClicked(MouseEvent event) {}
}
