package Game3D;


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
     * @param   dimensions  A Vector3 containing the length of the hitbox along the x-, y-, and 
     *                      z-axes, respectively.
     * @param   center  The center of the hitbox as a Vector3.
     */
    public RectangleBox3(Vector3 dimensions, Vector3 center) {
        super(center);
        this.dimensions = dimensions;
    }
    
    /**
     * Determine whether the point (x, y, z) is contained in the RectangleBox3.
     * 
     * @param   x   The x coordinate of the point.
     * @param   y   The y coordinate of the point.
     * @param   z   The z coordinate of the point.
     */
    @Override
    public boolean contains(double x, double y, double z) {
        if (x < center.X - dimensions.X || x > center.X + dimensions.X) {
            return false;
        }
        if (y < center.Y - dimensions.Y || y > center.Y + dimensions.Y) {
            return false;
        }
        if (z < center.Z - dimensions.Z || z > center.Z + dimensions.Z) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Determine whether a point is contained in the SphereBox3.
     * 
     * @param   point   The point as a Vector3.
     */
    @Override
    public boolean contains(Vector3 point) {
        return contains(point.X, point.Y, point.Z);
    }

    
    /** 
     * @return String
     */
    @Override
    public String toString() {
        return "RectangleBox3\nDimensions: (" + dimensions.X + ", " + dimensions.Y + ", " 
                + dimensions.Z + ")\n" + super.toString();
    }
}
