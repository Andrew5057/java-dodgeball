package dodgeball.server;

/**
 * Stores a player's input data, including buttons and mouse positions.
 *
 * @author Andrew Yim
 * @version 3-8-2024
 */
public class InputData {
  private boolean wdown;
  private boolean adown;
  private boolean sdown;
  private boolean ddown;
  private boolean spaceDown;
  private boolean cdown;
  private double mouseX;
  private double mouseY;
  private boolean throwingDodgeball;

  /**
   * Construct a new input data bank with all values set to 0 or <code>false</code>.
   */
  public InputData() {
    wdown = false;
    adown = false;
    sdown = false;
    ddown = false;
    spaceDown = false;
    cdown = false;
    mouseX = 0;
    mouseY = 0;
    throwingDodgeball = false;
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

  public double mouseX() {
    return mouseX;
  }

  public double mouseY() {
    return mouseY;
  }

  public boolean throwingDodgeball() {
    return throwingDodgeball;
  }

  public void setW(boolean wdown) {
    this.wdown = wdown;
  }

  public void setA(boolean adown) {
    this.adown = adown;
  }

  public void setS(boolean sdown) {
    this.sdown = sdown;
  }

  public void setD(boolean ddown) {
    this.ddown = ddown;
  }

  public void setSpace(boolean spaceDown) {
    this.spaceDown = spaceDown;
  }

  public void setC(boolean cdown) {
    this.cdown = cdown;
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
