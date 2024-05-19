package dodgeball.game;

/**
 * Represents a 3D object that is affected by gravity.
 *
 * @author Andrew Yim
 * @version 2-1-2024
 */
public class Projectile3 implements Cloneable {
  public static final double GRAVITY = -9.8;
  static final double HALF_GRAV = GRAVITY / 2.0;
  protected Vector3 position;
  protected Vector3 velocity; // Units per second

  /**
   * Define a projectile with a given position and initial velocity.
   *
   * @param position The initial position of the projectile as a Vector3.
   * @param velocity The velocity of the projectile in units per second as a
   *                 Vector3.
   */
  public Projectile3(Vector3 position, Vector3 velocity) {
    this.position = position;
    this.velocity = velocity;
  }

  /**
   * Define a projectile with an initial velocity of zero.
   *
   * @param position The initial position of the projectile as a Vector3.
   */
  public Projectile3(Vector3 position) {
    this(position, Vector3.ZERO);
  }

  @Override
  public Projectile3 clone() {
    return new Projectile3(position, velocity);
  }

  /**
   * The current position of the Projectile3.
   *
   * @return The current position of the Projectile3.
   */
  public Vector3 position() {
    return position;
  }

  /**
   * The current velocity of the Projectile3.
   *
   * @return The current velocity of the Projectile3.
   */
  public Vector3 velocity() {
    return velocity;
  }

  /**
   * Updates the Projectile3 to have the position and velocity it will have a
   * given number of
   * seconds into the future.
   *
   * @param seconds The number of seconds in the future that the Projectile3
   *                should be
   *                updated to. Should usually be the number of seconds that have
   *                passed
   *                since the last <code>update()</code> call.
   */
  public void update(double seconds) {
    double deltaX = velocity.x * seconds;
    double deltaY = HALF_GRAV * seconds * seconds + velocity.y * seconds;
    double deltaZ = velocity.z * seconds;
    position = position.add(new Vector3(deltaX, deltaY, deltaZ));

    double deltaV = GRAVITY * seconds;
    velocity = velocity.add(new Vector3(0, deltaV, 0));
  }

  /**
   * Determines whether the Projectile3 is contained by a given Hitbox3.
   *
   * @return <code>true</code> if the Hitbox3 contains the Projectile3,
   *         <code>false</code>
   *         otherwise.
   */
  public boolean isTouching(Hitbox3 box) {
    return box.contains(position);
  }
}
