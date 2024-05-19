package dodgeball.client.assets;

import dodgeball.game.Model3;
import java.io.File;
import java.io.IOException;

/**
 * Construct a <code>dodgeball.game.Model3</code> from the command line.
 */
public class BuildModel {
  public static void main(String[] args) throws IOException {
    String path = new File("").getAbsolutePath();
    Model3.designModel(path + "/DodgeballClient/Assets");
  }
}
