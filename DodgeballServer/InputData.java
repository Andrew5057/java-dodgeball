package DodgeballServer;


/**
 * Stores a player's input data, including buttons and mouse positions.
 *
 * @author Andrew Yim
 * @version 3-8-2024
 */
public class InputData {
    // In which non-threadsafe objects becomes really helpful
    private boolean w, a, s, d, space;
    private double mouseX, mouseY;
    private boolean throwingDodgeball;
    
    public InputData() {
        w = false;
        a = false;
        s = false;
        d = false;
        space = false;
        mouseX = 0;
        mouseY = 0;
        throwingDodgeball = false;
    }
    
    
    /** 
     * @return boolean
     */
    public synchronized boolean w() {
        return w;
    }
    public synchronized boolean a() {
        return a;
    }
    public synchronized boolean s() {
        return s;
    }
    public synchronized boolean d() {
        return d;
    }
    public synchronized boolean space() {
        return space;
    }
    public synchronized double mouseX() {
        return mouseX;
    }
    public synchronized double mouseY() {
        return mouseY;
    }
    public synchronized boolean throwingDodgeball() {
        return throwingDodgeball;
    }
    
    public synchronized void setW(boolean wDown) {
        w = wDown;
    }
    public synchronized void setA(boolean aDown) {
        a = aDown;
    }
    public synchronized void setS(boolean sDown) {
        s = sDown;
    }
    public synchronized void setD(boolean dDown) {
        d = dDown;
    }
    public synchronized void setSpace(boolean spaceDown) {
        space = spaceDown;
    }
    public synchronized void setMouseX(double mouseX) {
        this.mouseX = mouseX;
    }
    public synchronized void setMouseY(double mouseY) {
        this.mouseY = mouseY;
    }
    public synchronized void setThrowingDodgeball(boolean throwingDodgeball) {
        this.throwingDodgeball = throwingDodgeball;
    }
}
