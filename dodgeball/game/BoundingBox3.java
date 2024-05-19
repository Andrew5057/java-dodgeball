package dodgeball.game;

import java.util.LinkedList;
import java.util.List;

/**
 * Detect collisions between <code>Hitbox3</code> objects and points in 3D space. Implements the
 * Axis-Aligned Bounding Box protocol.
 */
public class BoundingBox3 extends RectangleBox3 {
  private Hitbox3 child1;

  private Hitbox3 child2;

  private double volume;
  private boolean volumeKnown = false;

  public BoundingBox3(Vector3 dimensions, Vector3 center) {
    super(dimensions, center);
  }

  public Hitbox3 child1() {
    return child1;
  }

  public Hitbox3 child2() {
    return child2;
  }

  public void setChild1(Hitbox3 child) {
    child1 = child;
  }

  public void setChild2(Hitbox3 child) {
    child2 = child;
  }
  
  /**
   * The volume of this bounding box.
   */
  public double volume() {
    if (volumeKnown) {
      return volume;
    }
    volume = dimensions.xcoord * dimensions.ycoord * dimensions.zcoord;
    volumeKnown = true;
    return volume;
  }

  /**
   * Determine whether this completely contains a given <code>game.Hitbox3</code>.
   *
   * @param box The hitbox that should be tested.
   * @return <code>true</code> if this completely contains <code>box</code>; false otherwise.
   */
  public boolean contains(Hitbox3 box) {
    if (minX() > box.minX() || maxX() < box.maxX()) {
      return false;
    }
    if (minY() > box.minY() || maxY() < box.maxY()) {
      return false;
    }
    if (minZ() > box.minZ() || maxZ() < box.maxZ()) {
      return false;
    }
    return true;
  }
  
  /**
   * Add a <code>game.Hitbox3</code> to this bounding box. If it completely fits inside one of
   * this box's children, add it there. If it cannot completely fit inside of one child but there's
   * an empty space, fill the empty space. If neither is possible, create a new
   * <code>game.BoundingBox3</code> that will fit two of the boxes to create more room.
   *
   * @param child The <code>game.Hitbox3</code> that should be added.
   */
  public void add(Hitbox3 child) {
    // If either child is a BoundingBox3 that contains the hitbox, put the hitbox in
    // the
    // BoundingBox3.
    if (child1 instanceof BoundingBox3) {
      BoundingBox3 c1 = (BoundingBox3) child1;
      if (c1.contains(child1)) {
        c1.add(child);
        return;
      }
    }
    if (child2 instanceof BoundingBox3) {
      BoundingBox3 c2 = (BoundingBox3) child2;
      if (c2.contains(child)) {
        c2.add(child);
        return;
      }
    }

    // If there's an empty spot, fill it
    if (child1 == null) {
      child1 = child;
      return;
    } else if (child2 == null) {
      child2 = child;
      return;
    }

    // Create a new, optimally-small bounding box to contain two children:
    // 1. Create all possible bounding boxes
    BoundingBox3 box12 = container(child1, child2);
    BoundingBox3 box1N = container(child1, child);
    BoundingBox3 box2N = container(child2, child);

    // 2. Choose the smallest box
    BoundingBox3 newBox;
    Hitbox3 oddOneOut;
    if (box12.volume() <= box1N.volume()) {
      newBox = box12;
      oddOneOut = child;
    } else {
      newBox = box1N;
      oddOneOut = child2;
    }
    if (box2N.volume() <= newBox.volume()) {
      newBox = box2N;
      oddOneOut = child1;
    }

    child1 = oddOneOut;
    child2 = newBox;
  }

  /**
   * Determine all <code>game.Hitbox3</code> objects that will collide with the given point.
   *
   * @param point A <code>game.Vector3</code> representing the point to be tested.
   * @return A <code>List&lt;game.Hitbox3%gt;</code> containing all of the hitboxes that contain
   *     the given point.
   */

  public List<Hitbox3> collisions(Vector3 point) {
    // Store all collisions, not just the first
    List<Hitbox3> colls = new LinkedList<Hitbox3>();

    if (child1 != null && child1.contains(point)) {
      if (child1 instanceof BoundingBox3) {
        // Go down to the next level
        BoundingBox3 c1 = (BoundingBox3) child1;
        colls.addAll(c1.collisions(point));
      } else {
        // We're at a leaf so we're done
        colls.add(child1);
      }
    }
    if (child2 != null && child2.contains(point)) {
      if (child2 instanceof BoundingBox3) {
        // Go down to the next level
        BoundingBox3 c2 = (BoundingBox3) child2;
        colls.addAll(c2.collisions(point));
      } else {
        // We're at a leaf so we're done
        colls.add(child2);
      }
    }

    return colls;
  }

  /**
   * Remove a given <code>game.Hitbox3</code> from the bounding box tree.
   *
   * @param box The hitbox to be removed.
   * @return <code>true</code> if the hitbox was found and removed; false otherwise.
   */
  public boolean remove(Hitbox3 box) {
    // Check if immediate children match the box.
    if (child1 != null && child1.equals(box)) {
      setChild1(null);
      // If the other child is a BoundingBox, pull its children up a level for
      // efficiency.
      if (child2() != null && child2() instanceof BoundingBox3) {
        BoundingBox3 c2 = (BoundingBox3) child2();
        setChild1(c2.child1());
        setChild2(c2.child2());
      }
      return true;
    } else if (child2 != null && child2.equals(box)) {
      setChild2(null);
      // If the other child is a BoundingBox, pull its children up a level for
      // efficiency.
      if (child1 != null && child1 instanceof BoundingBox3) {
        BoundingBox3 c1 = (BoundingBox3) child1;
        setChild1(c1.child1());
        setChild2(c1.child2());
      }
      return true;
    }
    // Check if grandchildren match the box
    return rm(box);
  }

  private boolean rm(Hitbox3 box) {
    // POSTCONDITIONS: (a) box isn't in the tree anymore (b) box's sibling replaces
    // box's parent
    if (child1 instanceof BoundingBox3) {
      BoundingBox3 c1 = (BoundingBox3) child1;
      if (c1.contains(box)) {
        if (c1.child1.equals(box)) {
          c1.child1 = null;
          Hitbox3 temp = c1.child2;
          child1 = null;
          child1 = temp;
          return true;
        } else if (c1.child2.equals(box)) {
          c1.child2 = null;
          Hitbox3 temp = c1.child1;
          child1 = null;
          child1 = temp;
          return true;
        } else if (c1.rm(box)) {
          return true;
        }
      }
    }
    if (child2 instanceof BoundingBox3) {
      BoundingBox3 c2 = (BoundingBox3) child2;
      if (c2.contains(box)) {
        if (c2.child1.equals(box)) {
          c2.child1 = null;
          Hitbox3 temp = c2.child2;
          child2 = null;
          child2 = temp;
          return true;
        } else if (c2.child2.equals(box)) {
          c2.child2 = null;
          Hitbox3 temp = c2.child1;
          child2 = null;
          child2 = temp;
          return true;
        } else if (c2.rm(box)) {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public String toString() {
    return "BoundingBox3" + super.toString().substring(14)
        + "\nLeft:\n" + child1 + "\nRight:\n" + child2;
  }

  // -------------------STATICS-------------------

  /**
   * Construct the smallest <code>BoundingBox3</code> that will fit two given <code>Hitbox3</code>
   * objects.
   *
   * @param box1 One <code>Hitbox3</code> that the box must fit.
   * @param box2 Another <code>Hitbox3</code> that the box must fit.
   * @return  A new <code>BoundingBox3</code> that will fit both boxes.
   */
  public static BoundingBox3 container(Hitbox3 box1, Hitbox3 box2) {
    double minX = Math.min(box1.minX(), box2.minX());
    double maxX = Math.max(box1.maxX(), box2.maxX());
    double minY = Math.min(box1.minY(), box2.minY());
    double maxY = Math.max(box1.maxY(), box2.maxY());
    double minZ = Math.min(box1.minZ(), box2.minZ());
    double maxZ = Math.max(box1.maxZ(), box2.maxZ());
    Vector3 dimensions = new Vector3(maxX - minX, maxY - minY, maxZ - minZ);
    Vector3 center = new Vector3(minX + maxX, minY + maxY, minZ + maxZ).multiply(0.5);
    BoundingBox3 box = new BoundingBox3(dimensions, center);
    box.add(box1);
    box.add(box2);
    return box;
  }
}
