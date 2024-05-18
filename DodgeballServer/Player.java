package DodgeballServer;

import Game3D.Camera;
import Game3D.EllipsoidBox3;
import Game3D.Projectile3;
import Game3D.Vector3;

/**
 * A Dodgeball player, which can move, jump, and throw Dodgeball objects.
 *
 * @author Andrew Yim
 * @version 3-1-2024
 */
public class Player extends EllipsoidBox3 {
    public static final double HEIGHT = 2.0;
    public static final double BASE_SIZE = Math.sqrt(1.0/2.0);
    public static final double WALK_SPEED = 1.0;
    public static final double JUMP_POWER = -1.0 * Projectile3.GRAVITY;
    public static final double THROW_STRENGTH = 5.0;
    private static final Vector3 DIMENSIONS = new Vector3(BASE_SIZE, HEIGHT, BASE_SIZE);
    private static final Vector3 centerToHead = new Vector3(0, HEIGHT / 2.0, 0);
    
    private Vector3 feetLocation;
    private Vector3 lookVector;
    private Projectile3 jumpTrajectory;
    private InputData inputData;
    
    public Player() {
        super(DIMENSIONS, new Vector3(0, HEIGHT/2.0, 0));
        feetLocation = Vector3.ZERO;
        lookVector = Vector3.I;
        inputData = new InputData();
    }
    
    public Player(Vector3 position, InputData inputData) {
        super(DIMENSIONS, position);
        this.feetLocation = position.add(new Vector3(0, -HEIGHT/2.0, 0));
        lookVector = Vector3.I;
        this.inputData = inputData;
    }

    public static Vector3 centerToHead(Vector3 center) {
        return center.add(centerToHead);
    }
    
    
    /** 
     * @return Vector3
     */
    public Vector3 lookVector() {
        return lookVector;
    }
    
    public InputData inputData() {
        return inputData;
    }
    
    public void update(double seconds) {
        if (jumpTrajectory == null) {
            return;
        }
        jumpTrajectory.update(seconds);
        Vector3 newPos = jumpTrajectory.position();
        if (newPos.Y <= 0) {
            feetLocation = new Vector3(newPos.X, 0, newPos.Z);
            jumpTrajectory = null;
        } else {
            feetLocation = newPos;
        }
        center = newPos.add(new Vector3(0, HEIGHT/2, 0));
    }
    
    public void move(double x, double z) {
        if (jumpTrajectory != null) {
            return;
        }
        feetLocation = feetLocation.add(new Vector3(x, 0, z));
        center = feetLocation.add(new Vector3(0, HEIGHT/2.0, 0));
    }
    
    public void jump(double xVelocity, double zVelocity) {
        if (jumpTrajectory != null) {
            return;
        }
        
        // Fine-tune, either here or in GameManager
        jumpTrajectory = new Projectile3(feetLocation, 
                new Vector3(xVelocity, zVelocity, JUMP_POWER));
    }
    
    public void rotate(double yaw, double pitch) {
        lookVector = Camera.rotateLookVector(lookVector, yaw, pitch);
    }
    
    public void onDodgeballHit() {
        
    }
}
