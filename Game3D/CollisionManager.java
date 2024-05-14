package Game3D;


/**
 * Implements an Axis-Aligned Bounding Box Tree to manage collisions between point-based
 * projectiles and Hitbox3s.
 *
 * @author Andrew Yim
 * @version 2-26-2024
 */
public class CollisionManager {
    BoundingBox3 root;
    public CollisionManager() {
        root = new BoundingBox3(new Vector3(0, 0, 0), new Vector3(0, 0, 0));
    }
    
    
    /** 
     * @param child
     */
    public void add(Hitbox3 child) {
        root.add(child);
    }
    
    public Hitbox3[] collisions(Vector3 point) {
        return root.collisions(point).toArray(new Hitbox3[0]);
    }
    
    public boolean remove(Hitbox3 box) {
        return root.remove(box);
    }
    
    @Override
    public String toString() {
        return "CollisionManager:\n" + root.toString();
    }
}
