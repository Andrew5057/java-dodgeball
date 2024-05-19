package dodgeball.game;

/**
 * Defines a rectangular prism-shaped Hitbox3.
 *
 * @author Andrew Yim
 * @version 2-1-2024
 */
public class RectangleBox3 extends Hitbox3 {
  /**
   * Define a hitbox in the shape of a rectangular prism.
   *
   * @param dimensions A Vector3 containing the length of the hitbox along the x-,
   *                   y-, and z-axes, respectively.
   * @param center     The center of the hitbox as a Vector3.
   */
  public RectangleBox3(Vector3 dimensions, Vector3 center) {
    super(center);
    this.dimensions = dimensions;
  }

  /**
   * Determine whether the point (x, y, z) is contained in the RectangleBox3.
   *
   * @param x The x coordinate of the point.
   * @param y The y coordinate of the point.
   * @param z The z coordinate of the point.
   */
  @Override
  public boolean contains(double x, double y, double z) {
    if (x < center.x - dimensions.x || x > center.x + dimensions.x) {
      return false;
    }
    if (y < center.y - dimensions.y || y > center.y + dimensions.y) {
      return false;
    }
    if (z < center.z - dimensions.z || z > center.z + dimensions.z) {
      return false;
    }

    return true;
  }

  /**
   * Determine whether a point is contained in the SphereBox3.
   *
   * @param point The point as a Vector3.
   */
  @Override
  public boolean contains(Vector3 point) {
    return contains(point.x, point.y, point.z);
  }

  @Override
  public String toString() {
    return "RectangleBox3\nDimensions: (" + dimensions.x + ", " + dimensions.y + ", "
        + dimensions.z + ")\n" + super.toString();
  }
}
