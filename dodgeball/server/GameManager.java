package dodgeball.server;

import dodgeball.game.CollisionManager;
import dodgeball.game.Hitbox3;
import dodgeball.game.Vector2;
import dodgeball.game.Vector3;
import java.util.ArrayList;
import java.util.List;

/**
 * 
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

  public GameManager() {
    daemon = new DodgeballDaemon(8080, this);
    collManager = new CollisionManager();
    players = new ArrayList<Player>();
    dodgeballs = new ArrayList<Dodgeball>();
  }

  public void addPlayer(Player player) {
    players.add(player);
    collManager.add(player);
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

  public void update(double seconds) {
    // Deal with basic physics
    for (Player player : players) {
      collManager.remove(player);
      collManager.add(player);
      player.update(seconds);
    }
    // Check for player-dodgeball collisions
    List<Dodgeball> removables = new ArrayList<Dodgeball>();
    for (Dodgeball dodgeball : dodgeballs) {
      dodgeball.update(seconds);
      if (!dodgeball.aboveGround()) {
        removables.add(dodgeball);
        continue;
      }
      Hitbox3[] colls = collManager.collisions(dodgeball.position());
      if (colls.length > 0) {
        for (Hitbox3 box : colls) {
          if (box instanceof Player && dodgeball.thrower != box) {
            Player pl = (Player) box;
            pl.onDodgeballHit();
            removables.add(dodgeball);
            break;
          }
        }
      }
    }
    // Delete all the collided dodgeballs
    for (Dodgeball dodgeball : removables) {
      dodgeballs.remove(dodgeball);
    }

    // Deal with player inputs
    for (Player player : players) {
      if (players == null) {
        continue;
      }

      InputData data = player.inputData();

      // Deal with movement.
      int forward = 0;
      if (data.w()) {
        forward++;
      }
      if (data.s()) {
        forward--;
      }
      int right = 0;
      if (data.d()) {
        right++;
      }
      if (data.a()) {
        right--;
      }
      Vector2 moveVelocity = new Vector2(forward, right);
      moveVelocity = moveVelocity.unit();
      moveVelocity = moveVelocity.multiply(0.033 * Player.WALK_SPEED);

      if (data.space()) {
        player.jump(moveVelocity.x, moveVelocity.y);
      } else {
        player.move(moveVelocity.x, moveVelocity.y);
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
}
