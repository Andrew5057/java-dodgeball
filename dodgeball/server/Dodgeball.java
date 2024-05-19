package dodgeball.server;

import dodgeball.game.Projectile3;
import dodgeball.game.Vector3;

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
   * Determine whether the dodgeball is above ground or not.
   *
   * @return boolean <code>true</code> if the y coordinate is at least zero; <code>false</code>
   *      otherwise.
   */
  public boolean aboveGround() {
    return position().y >= 0;
  }
}
