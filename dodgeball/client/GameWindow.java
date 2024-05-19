package dodgeball.client;

import dodgeball.game.Camera;
import dodgeball.game.Model3;
import dodgeball.game.Polygon3;
import dodgeball.game.Vector3;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * The game window used to show a Dodgeball game to the client.
 *
 * @author Andrew Yim
 * @version 1-23-2024
 */
public class GameWindow extends Frame {
  private int pixelsX;
  private int pixelsY;
  private int halfWidth;
  private int halfHeight;
  private double scaleFactorX;
  private double scaleFactorY;
  private Camera camera;
  private List<Model3> models; // Models that the Window should be keeping track of

  /**
   * Generate a Window that will fit a screen with the given width and height.
   * Will attempt to
   * maximize screen area while maintaining a ratio of 90 horizontal degrees by 60
   * vertical
   * degrees.
   */
  public GameWindow(int screenWidth, int screenHeight, Client client) {
    if (screenWidth <= Math.sqrt(3) * screenHeight) {
      this.pixelsX = screenWidth;
      this.pixelsY = (int) (screenWidth / Math.sqrt(3));
    } else {
      this.pixelsY = screenHeight;
      this.pixelsX = (int) (screenHeight * Math.sqrt(3));
    }
    setFocusable(true);
    setResizable(false);
    setSize(pixelsX, pixelsY);
    halfWidth = pixelsX / 2;
    halfHeight = pixelsY / 2;

    // 90 degree by 60 degree aspect ratio
    scaleFactorX = halfWidth;
    scaleFactorY = 3 * halfHeight / Math.sqrt(3);

    this.camera = new Camera(0, 0, 0, 1, 0, 0); // Basic camera, nothing special

    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        if (client != null) {
          client.quit();
        } else {
          System.exit(0);
        }
      }
    });

    models = new ArrayList<Model3>();
    setVisible(true);
  }

  /**
   * Give the Window a new Model3 to track and draw.
   *
   * @param model The Model3 that the Window should begin drwaing.
   */
  public void addModel(Model3 model) {
    models.add(model);
  }

  public void clear() {
    models.clear();
  }

  public void setModels(List<Model3> models) {
    this.models = new ArrayList<Model3>(models);
  }

  /**
   * Rotate the Camera that the Window uses to render objects.
   *
   * @param yaw   The amount that the Camera should be rotated in the yaw
   *              (left-right)
   *              direction.
   * @param pitch The amount that the Camera should be rotate in the pitch
   *              (up-down)
   *              direction.
   */
  public void rotateCamera(double yaw, double pitch) {
    camera.rotate(yaw, pitch);
  }

  public Vector3 cameraPosition() {
    return camera.position();
  }

  public Vector3 cameraDirection() {
    return camera.direction();
  }

  public Vector3 cameraHorizontal() {
    return camera.horizontal();
  }

  public Vector3 cameraVertical() {
    return camera.vertical();
  }

  /**
   * Set the position of the Camera that the Window uses to render objects.
   *
   * @param position A Vector3 containing the desired x, y, and z coordinates,
   *                 respectively.
   */
  public void setCameraPosition(Vector3 position) {
    camera.setPosition(position);
  }
  
  public void setCameraDirection(Vector3 direction) {
    camera.setDirection(direction);
  }

  /**
   * Move the Camera that the Window uses to render objects.
   *
   * @param x The amount the Camera should be translated in the x direction.
   * @param y The amount the Camera should be translated in the y direction.
   * @param z The amount the Camera should be translated in the z direction.
   */
  public void translateCamera(double x, double y, double z) {
    camera.translate(new Vector3(x, y, z));
  }

  /**
   * Move the Camera that the Window uses to render objects.
   *
   * @param displacement A Vector3 containing the desired x, y, and z
   *                     displacement,
   *                     respectively.
   */
  public void translateCamera(Vector3 displacement) {
    camera.translate(displacement);
  }

  /**
   * Convert a Polygon3 to an array of x-coordinates, an array of y-coordinates,
   * and a Color.
   *
   * @param polygon The 3D polygon that needs to be converted.
   * @return An Object[] containing:
   *         - An int[] containing the x-coordinates of the vertices on the
   *         screen.
   *         - An int[] containing the y-coordinates of the vertices on the
   *         screen.
   *         - The Color of the Polygon3.
   */
  private Object[] polygonToDrawable(Polygon3 polygon) {
    int[] xcoords = new int[polygon.length()];
    int[] ycoords = new int[xcoords.length];

    Vector3 point;
    for (int i = 0; i < xcoords.length; i++) {
      point = polygon.point(i);
      xcoords[i] = (int) (point.xcoord * scaleFactorX) + halfWidth;
      ycoords[i] = halfHeight - (int) (point.ycoord * scaleFactorY);
    }
    return new Object[] { xcoords, ycoords, polygon.color() };
  }

  /**
   * Draw a polygon on screen given its coordinate and color data.
   *
   * @param An Object[] containing:
   *          - An int[] containing the x-coordinates of the vertices on the
   *          screen.
   *          - An int[] containing the y-coordinates of the vertices on the
   *          screen.
   *          - The Color of the Polygon3.
   */
  private void drawPolygon(Graphics g, Object[] polyData) {
    int[] xcoords = (int[]) polyData[0];
    int[] ycoords = (int[]) polyData[1];
    g.setColor((Color) polyData[2]);
    g.fillPolygon(xcoords, ycoords, xcoords.length);
  }

  /**
   * Draw all tracked Models on the screen.
   */
  public void paint(Graphics g) {
    List<Model3> drawables = new ArrayList<Model3>(models);
    List<Polygon3> polys = new ArrayList<Polygon3>();
    for (Model3 model : drawables) {
      polys.addAll(Arrays.asList(camera.render(model)));
    }

    Polygon3[] sortedPolys = Polygon3.sort(polys.toArray(new Polygon3[0]));
    Stream<Object[]> drawablePolys = Arrays.stream(sortedPolys).parallel()
        .filter(p -> p.minZ() >= 0).map(this::polygonToDrawable);

    g.clearRect(0, 0, pixelsX, pixelsY);
    drawablePolys.sequential().forEach(p -> this.drawPolygon(g, p));
  }
}
