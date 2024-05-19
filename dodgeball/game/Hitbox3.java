package dodgeball.game;

/**
 * A framework for 3D hitboxes .
 *
 * @author Andrew Yim
 * @version 1-31-2024
 */
public abstract class Hitbox3 {
  protected Vector3 center;
  protected Vector3 dimensions;

  /**
   * Define a 3D hitbox in terms of its center.
   *
   * @param center A Vector3 containing the x, y, and z coordinates of the
   *               Hitbox3's center.
   */
  public Hitbox3(Vector3 center) {
    this.center = center;
  }

  /**
   * Define a 3D hitbox in terms of the coordiantes of its center.
   *
   * @param x The x-coordinate of the center.
   * @param y The y-coordinate of the center.
   * @param z The z-coordinate of the center.
   */
  public Hitbox3(double x, double y, double z) {
    this(new Vector3(x, y, z));
  }

  /**
   * Define a 3D hitbox with center at the origin.
   */
  public Hitbox3() {
    this(Vector3.ZERO);
  }

  /**
   * The center of the Hitbox3 as a Vector3.
   *
   * @return The Hitbox3's center as a Vector3.
   */
  public Vector3 center() {
    return center;
  }

  /**
   * Set the center of the Hitbox3.
   *
   * @param center A Vector3 containing the x, y, and z coordiantes of the new
   *               center.
   */
  public void setCenter(Vector3 center) {
    this.center = center;
  }

  /**
   * The minimum x-coordinate of the Hitbox3.
   *
   * @return The minimum x-coordinate of the Hitbox3.
   */
  public double minX() {
    return center.x - (dimensions.x / 2);
  }

  /**
   * The maximum x-coordinate of the Hitbox3.
   *
   * @return The maximum x-coordinate of the Hitbox3.
   */
  public double maxX() {
    return center.x + (dimensions.x / 2);
  }

  /**
   * The minimum y-coordinate of the Hitbox3.
   *
   * @return The minimum y-coordinate of the Hitbox3.
   */
  public double minY() {
    return center.y - (dimensions.y / 2);
  }

  /**
   * The maximum x-coordinate of the Hitbox3.
   *
   * @return The maximum x-coordinate of the Hitbox3.
   */
  public double maxY() {
    return center.y + (dimensions.y / 2);
  }

  /**
   * The minimum z-coordinate of the Hitbox3.
   *
   * @return The minimum z-coordinate of the Hitbox3.
   */
  public double minZ() {
    return center.z - (dimensions.z / 2);
  }

  /**
   * The maximum x-coordinate of the Hitbox3.
   *
   * @return The maximum x-coordinate of the Hitbox3.
   */
  public double maxZ() {
    return center.z + (dimensions.z / 2);
  }

  /**
   * Determine if two Hitbox3 objects are equivalent. Two Hitbox3s are equivalent
   * if they
   * are of exactly the same class (not ancestors/descendants of each other) and
   * they have
   * the same dimensions and centers.
   */
  public boolean equals(Hitbox3 other) {
    if (other.getClass() != getClass()) {
      return false;
    }
    return center.equals(other.center) && dimensions.equals(other.dimensions);
  }
  
  @Override
  public String toString() {
    return "Center: (" + center.x + ", " + center.y + ", " + center.z + ")";
  }

  /**
   * Determine whether the Hitbox3 contains the point (x, y, z). Must be
   * overriden.
   *
   * @param x The x-coordinate of the point.
   * @param y The y-coordinate of the point.
   * @param z The z-coordinate of the point.
   */
  public abstract boolean contains(double x, double y, double z);

  /**
   * Determine whether the Hitbox3 contains the given point. Must be overriden.
   *
   * @param point The point as a Vector3.
   */
  public abstract boolean contains(Vector3 point);
}
