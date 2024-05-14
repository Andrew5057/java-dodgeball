package Game3D;


/**
 * Defines a spherical Hitbox3.
 *
 * @author Andrew Yim
 * @version 2-1-2024
 */
public class SphereBox3 extends Hitbox3 {
    private double radius;
    
    /**
     * Define a spherical hitbox in terms of its radius and center.
     * 
     * @param   radius  The radius of the SphereBox3
     * @param   center  The center of the SphereBox3 as a Vector3.
     */
    public SphereBox3(double radius, Vector3 center) {
        super(center);
        this.radius = radius;
        double diameter = radius * 2;
        this.dimensions = new Vector3(diameter, diameter, diameter);
    }
    
    /**
     * Determine whether a point is contained in the SphereBox3.
     * 
     * @param   point   The point as a Vector3.
     */
    @Override
    public boolean contains(Vector3 point) {
        return center.distanceTo(point) <= radius;
    }
    /**
     * Determine whether the point (x, y, z) is contained in the SphereBox3.
     * 
     * @param   x   The x coordinate of the point.
     * @param   y   The y coordinate of the point.
     * @param   z   The z coordinate of the point.
     */
    @Override
    public boolean contains(double x, double y, double z) {
        return contains(new Vector3(x, y, z));
    }
    
    
    /** 
     * @return String
     */
    @Override
    public String toString() {
        return "SphereBox3:\nRadius: " + radius + "\n" + super.toString();
    }
}
