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
    public boolean w() {
        return w;
    }
    public boolean a() {
        return a;
    }
    public boolean s() {
        return s;
    }
    public boolean d() {
        return d;
    }
    public boolean space() {
        return space;
    }
    public double mouseX() {
        return mouseX;
    }
    public double mouseY() {
        return mouseY;
    }
    public boolean throwingDodgeball() {
        return throwingDodgeball;
    }
    
    public void setW(boolean wDown) {
        w = wDown;
    }
    public void setA(boolean aDown) {
        a = aDown;
    }
    public void setS(boolean sDown) {
        s = sDown;
    }
    public void setD(boolean dDown) {
        d = dDown;
    }
    public void setSpace(boolean spaceDown) {
        space = spaceDown;
    }
    public void setMouseX(double mouseX) {
        this.mouseX = mouseX;
    }
    public void setMouseY(double mouseY) {
        this.mouseY = mouseY;
    }
    public void setThrowingDodgeball(boolean throwingDodgeball) {
        this.throwingDodgeball = throwingDodgeball;
    }
}
