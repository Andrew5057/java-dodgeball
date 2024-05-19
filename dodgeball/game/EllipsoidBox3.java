package dodgeball.game;

/**
 * Defines an ellipsoid Hitbox3.
 *
 * @author Andrew Yim
 * @version 2-1-2024
 */
public class EllipsoidBox3 extends Hitbox3 {
  /**
   * Define a hitbox in the shape of an ellipsoid.
   *
   * @param dimensions A Vector3 containing the length of the hitbox along the x-,
   *                   y-, and
   *                   z-axes, respectively.
   * @param center     The center of the hitbox as a Vector3.
   */
  public EllipsoidBox3(Vector3 dimensions, Vector3 center) {
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
  public boolean contains(double x, double y, double z) {
    return Math.pow(2.0 * (x - center.xcoord) / dimensions.xcoord, 2)
        + Math.pow(2.0 * (y - center.ycoord) / dimensions.ycoord, 2)
        + Math.pow(2.0 * (z - center.zcoord) / dimensions.zcoord, 2)
        <= 1;
  }

  /**
   * Determine whether a point is contained in the SphereBox3.
   *
   * @param point The point as a Vector3.
   */
  public boolean contains(Vector3 point) {
    return contains(point.xcoord, point.ycoord, point.zcoord);
  }
  
  @Override
  public String toString() {
    return "EllipsoidBox3\nDimensions: " + dimensions.xcoord + ", " + dimensions.ycoord + ", "
        + dimensions.zcoord + ")\n" + super.toString();
  }
}
