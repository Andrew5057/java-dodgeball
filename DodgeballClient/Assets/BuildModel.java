package DodgeballClient.Assets;

import java.io.File;
import java.io.IOException;
import Game3D.Model3;

public class BuildModel {
    public static void main(String[] args) throws IOException {
        String path = new File("").getAbsolutePath();
        Model3.designModel(path + "/DodgeballClient/Assets");
    }
}
