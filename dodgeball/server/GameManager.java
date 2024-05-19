package dodgeball.server;

import dodgeball.game.CollisionManager;
import dodgeball.game.Hitbox3;
import dodgeball.game.Vector2;
import dodgeball.game.Vector3;
import java.util.ArrayList;
import java.util.List;

/**
 * The top-level manager for a game of dodgeball.
 *
 * @author Andrew Yim
 * @version 3-1-2024
 */
public class GameManager implements Runnable {
  public static final int MAX_PLAYERS = Integer.MAX_VALUE;

  private DodgeballDaemon daemon;
  private CollisionManager collManager;
  private List<Player> players;
  private List<Dodgeball> dodgeballs;
  
  /**
   * Instantiate a new game manager. Defaults to using port 8080.
   */
  public GameManager() {
    daemon = new DodgeballDaemon(8080, this);
    collManager = new CollisionManager();
    players = new ArrayList<Player>();
    dodgeballs = new ArrayList<Dodgeball>();
  }

  /**
   * Add a new player to the game handler.
   *
   * @param player The player to be added.
   */
  public void addPlayer(Player player) {
    players.add(player);
    collManager.add(player);

    dodgeballs.add(new Dodgeball(new Vector3(0, 1.5, 0), Vector3.ZERO, player));
  }

  public void removePlayer(Player player) {
    players.remove(player);
    collManager.remove(player);
  }

  public List<Player> players() {
    return players;
  }

  public List<Dodgeball> dodgeballs() {
    return dodgeballs;
  }

  @Override
  public void run() {
    new Thread(daemon).start();
    long time = System.currentTimeMillis();
    while (true) {
      try {
        long ping = System.currentTimeMillis() - time;
        if (ping < 33) {
          Thread.sleep(33 - ping);
        }
      } catch (Exception e) {
        e.printStackTrace();
        return;
      }
      time = System.currentTimeMillis();
      update(0.033);
    }
  }

  /**
   * Update all objects handled by this game manager. Calculate projectile motion, determine 
   * collisions, and handle player movement.
   *
   * @param seconds The number of seconds that have elapsed since the last <code>update</code>
   *      call.
   */
  public void update(double seconds) {
    updateProjectiles(seconds);
    removeDroppedDodgeballs();

    for (Player player : hitPlayers()) {
      player.onDodgeballHit();
    }

    // Deal with player inputs
    for (Player player : players) {
      handlePlayerInput(player);
    }
  }

  /**
   * Track projectile motion for all dodgeballs and jumping players.
   *
   * @param seconds The number of seconds elapsed since the last call to
   *      <code>updateProjectiles</code>.
   */
  private void updateProjectiles(double seconds) {
    for (Player player : players) {
      collManager.remove(player);
      collManager.add(player);
      player.update(seconds);
    }

    for (Dodgeball dodgeball : dodgeballs) {
      dodgeball.update(seconds);
    }
  }

  /**
   * Remove all dodgeballs that have fallen below ground level.
   */
  private void removeDroppedDodgeballs() {
    List<Dodgeball> removables = new ArrayList<Dodgeball>();
    for (Dodgeball dodgeball : dodgeballs) {
      if (!dodgeball.aboveGround()) {
        removables.add(dodgeball);
      }
    }
    removables.removeAll(removables);
  }

  private List<Player> hitPlayers() {
    List<Dodgeball> removables = new ArrayList<Dodgeball>();
    List<Player> hit = new ArrayList<Player>();
    for (Dodgeball dodgeball : dodgeballs) {
      Hitbox3[] collisions = collManager.collisions(dodgeball.position());
      for (Hitbox3 hitbox : collisions) {
        if (hitbox instanceof Player && dodgeball.thrower != hitbox) {
          hit.add((Player) hitbox);
          removables.add(dodgeball);
          break;
        }
      }
    }
    dodgeballs.removeAll(removables);
    return hit;
  }

  private void handlePlayerInput(Player player) {
    InputData data = player.inputData();

    // Deal with movement.
    int forward = 0;
    if (data.wdown()) {
      forward++;
    }
    if (data.sdown()) {
      forward--;
    }
    int right = 0;
    if (data.ddown()) {
      right++;
    }
    if (data.adown()) {
      right--;
    }
    Vector2 moveVelocity = new Vector2(forward, right);
    moveVelocity = moveVelocity.unit();
    moveVelocity = moveVelocity.multiply(0.033 * Player.WALK_SPEED);

    if (data.spaceDown()) {
      player.jump(moveVelocity.xcoord, moveVelocity.ycoord);
    } else {
      player.move(moveVelocity.xcoord, moveVelocity.ycoord);
    }

    // Deal with look vectors.
    double yaw = data.mouseX() / 50.0;
    double pitch = data.mouseY() / 50.0;
    player.rotate(yaw, pitch);

    // Throw dodgeballs
    if (data.throwingDodgeball()) {
      // Throw the dodgeball
      Vector3 velocity = player.lookVector().multiply(Player.THROW_STRENGTH);
      Dodgeball ball = new Dodgeball(player.headPosition(), velocity, player);
      dodgeballs.add(ball);
      // Prevent double-throws
      data.setThrowingDodgeball(false);
    }
  }
}
