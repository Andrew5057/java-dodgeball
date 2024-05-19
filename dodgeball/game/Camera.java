package dodgeball.game;

import java.util.stream.Stream;

/**
 * Determines the projection of objects in 3D space onto a 2D viewpane.
 *
 * @author Andrew Yim
 * @version 11-12-2023
 */
public class Camera {
  private Vector3 position;
  private Vector3 direction;
  private Vector3 horizontal;
  private Vector3 vertical;

  /**
   * Define a Camera in terms of its current position in the 3D space and the
   * direction in
   * which it is currently looking.
   */
  public Camera(Vector3 position, Vector3 direction) {
    this.position = position;
    setDirection(direction);
  }

  /**
   * Define a Camera in terms of the coordintes of its current position and the
   * components of its
   * direction vector.
   */
  public Camera(int positionX, int positionY, int positionZ,
      int directionX, int directionY, int directionZ) {
    this(new Vector3(positionX, positionY, positionZ),
        new Vector3(directionX, directionY, directionZ));
  }

  /**
   * Get the Vector3 representing the Camera's x, y, and z coordinates.
   *
   * @return A Vector3 containing the Camera's x, y, and z coordinates,
   *         respectively.
   */
  public Vector3 position() {
    return position;
  }

  /**
   * Get the Vector3 representing the Camera's look vector. A Camera's look vector
   * is the vector that
   * it looks straight through.
   *
   * @return A Vector3 containing the x, y, and z coordinates of the Camera's look
   *         vector,
   *         respectively.
   */
  public Vector3 direction() {
    return direction;
  }

  public Vector3 horizontal() {
    return horizontal;
  }

  public Vector3 vertical() {
    return vertical;
  }

  /**
   * Set the Camera's position, given a Vector3 object that represents its new x,
   * y, and z.
   *
   * @param position A Vector3 containing the desired x, y, and z coordinates,
   *                 respectively.
   */
  public void setPosition(Vector3 position) {
    this.position = position;
  }

  /**
   * Set the Camera's look vector, given a Vector3 object that represents its new
   * x, y, and z.
   *
   * @param position A Vector3 containing the desired x, y, and z coordinates,
   *                 respectively.
   */
  public void setDirection(Vector3 direction) {
    this.direction = direction.unit();
    horizontal = direction.cross(Vector3.J).unit();
    vertical = horizontal.cross(direction).unit();
  }

  /**
   * Translate the camera. Equivalent to adding the given vector to the camera's
   * position vector.
   *
   * @param displacement The vector by which the camera should be translated.
   */
  public void translate(Vector3 displacement) {
    position = position.add(displacement);
  }

  /**
   * Rotate the camera. Only available along the pitch (up-down) and yaw
   * (left-right) axes.
   *
   * @param yaw   The number of degrees to yaw (side-side) by.
   * @param pitch The number of degrees to pitch (up-down) by.
   */
  public void rotate(double yaw, double pitch) {
    setDirection(rotateLookVector(direction, yaw, pitch));
  }

  /**
   * Render a 3D point as a point on a 2D plane, according to the current position
   * and direction
   * of the camera.
   *
   * @param point A Vector3 containing the point's coordinates.
   * @return The coordinates of the projection of the point on the viewplane and
   *         its distance from
   *         the viewplane as a Vector3.
   */
  public Vector3 render(Vector3 point) {
    if (point.equals(position)) {
      return Vector3.ZERO;
    }

    Vector3 vector = point.subtract(position);
    // scaledVector is vector projected onto the plane that is 1 unit from the
    // camera.
    Vector3 scaledVector = vector.multiply(1 / vector.scalarComp(direction));

    double x = scaledVector.dot(horizontal) / horizontal.dot(horizontal);
    double y = scaledVector.dot(vertical) / vertical.dot(vertical);

    Vector3 compVector = vector.comp(direction);
    // This obviously isn't the distance formula but since we're only using the
    // depth
    // comparatively, it's ok to square all the depth values and save ourselves the
    // time of
    // square rooting the values.
    double depth = compVector.x * compVector.x + compVector.y * compVector.y + compVector.z * compVector.z;
    // Check if the point is behind the camera
    if ((compVector.x <= 0) ^ (direction.x <= 0)) {
      depth *= -1;
    }

    return new Vector3(x, y, depth);
  }

  /**
   * Render a 3D polygon onto a 2D plane, according to the current position and
   * direction of the
   * camera.
   *
   * @param polygon A Polygon3 to be rendered.
   * @return A Polygon3 where x- and y-values are coordinates on a plane and
   *         z-values are
   *         distances from the viewplane.
   */
  public Polygon3 render(Polygon3 polygon) {
    Vector3[] renderedPoints = Stream.of(polygon.points()).parallel().map(point -> render(point))
        .toArray(Vector3[]::new);
    return new Polygon3(renderedPoints, polygon.color());
  }

  /**
   * Render a 3D model onto a 2D plane, according to the current position and
   * direction of the
   * camera. Returns an array of Polygon3s, not a new Model3 object.
   */
  public Polygon3[] render(Model3 model) {
    Polygon3[] renderedPolys = new Polygon3[model.length()];
    for (int i = 0; i < model.length(); i++) {
      renderedPolys[i] = render(model.polygon(i));
    }
    return renderedPolys;
  }

  // --------------------------STATICS------------------------------
  /**
   * Rotate the Vector3 about the origin. Only available along the pitch (up-down)
   * and yaw (left-right) axes. Pitch is limited to +-45 degrees.
   *
   * @param yaw   The number of degrees to yaw (side-side) by.
   * @param pitch The number of degrees to pitch (up-down) by.
   */
  public static Vector3 rotateLookVector(Vector3 vector, double yaw, double pitch) {
    yaw = Math.toRadians(yaw) * -1; // Shifts to clockwise instead of CCW rotation
    pitch = Math.toRadians(pitch);

    double baseLength = Math.sqrt(vector.x * vector.x + vector.y * vector.y);
    double currPitch = Math.atan2(vector.y, baseLength);

    // Set the pitch, restricting it to a certain range
    double newPitch = currPitch + pitch;
    if (newPitch > Math.PI / 4.0) {
      newPitch = Math.PI / 4.0;
    } else if (newPitch < -Math.PI / 4.0) {
      newPitch = -Math.PI / 4.0;
    }
    double newY = Math.tan(newPitch) * baseLength;

    // Saves time in the next step
    double sinYaw = Math.sin(yaw);
    double cosYaw = Math.cos(yaw);
    double newX = vector.x * cosYaw + vector.z * sinYaw;
    double newZ = -vector.x * sinYaw + vector.z * cosYaw;

    return new Vector3(newX, newY, newZ);
  }
}
