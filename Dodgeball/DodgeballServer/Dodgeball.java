package DodgeballServer;

import Game3D.Projectile3;
import Game3D.Vector3;

/**
 * Represents a dodgeball as it moves through space. 
 *
 * @author Andrew Yim
 * @version 3-1-2024
 */
public class Dodgeball extends Projectile3 {
    Player thrower;

    public Dodgeball(Vector3 position, Vector3 velocity, Player thrower) {
        super(position, velocity);
        this.thrower = thrower;
    }
    
    
    /** 
     * @return boolean
     */
    public boolean aboveGround() {
        return position().Y >= 0;
    }
}
