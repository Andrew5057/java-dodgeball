package DodgeballClient;

import Game3D.Camera;
import Game3D.Model3;
import Game3D.Polygon3;
import Game3D.Vector3;
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
    private int pixelsX, pixelsY;
    private int halfWidth, halfHeight; // Storing these in the Window reduces calculations later
    private double xScaleFactor;
    private double yScaleFactor;
    private Camera camera;
    private List<Model3> models; // Models that the Window should be keeping track of
    
    
    /**
     * Generate a Window that will fit a screen with the given width and height. Will attempt to 
     * maximize screen area while maintaining a ratio of 90 horizontal degrees by 60 vertical 
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
        xScaleFactor = halfWidth;
        yScaleFactor = 3 * halfHeight / Math.sqrt(3);
        
        this.camera = new Camera(0, 0, 0, 1, 0, 0); // Basic camera, nothing special
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (client != null) {
                    client.quit();
                }
                System.exit(0); // Should be updated later
            } 
        });
        
        models = new ArrayList<Model3>();
        setVisible(true);
    }
    
    /**
     * Give the Window a new Model3 to track and draw.
     * 
     * @param   model   The Model3 that the Window should begin drwaing.
     */
    public synchronized void addModel(Model3 model) {
        models.add(model);
    }
    
    public synchronized void clear() {
        models = new ArrayList<Model3>();
    }
    
    /**
     * Rotate the Camera that the Window uses to render objects.
     * 
     * @param   yaw The amount that the Camera should be rotated in the yaw (left-right) 
     *              direction.
     * @param   pitch   The amount that the Camera should be rotate in the pitch (up-down) 
     *                  direction.
     */
    public synchronized void rotateCamera(double yaw, double pitch) {
        camera.rotate(yaw, pitch);
    }
    
    
    /** 
     * @return Vector3
     */
    public synchronized Vector3 cameraPosition() {
        return camera.position();
    }
    
    public synchronized Vector3 cameraDirection() {
        return camera.direction();
    }
    public synchronized Vector3 cameraHorizontal() {
        return camera.horizontal();
    }
    public synchronized Vector3 cameraVertical() {
        return camera.vertical();
    }
    
    /**
     * Move the Camera that the Window uses to render objects.
     * 
     * @param   x   The amount the Camera should be translated in the x direction.
     * @param   y   The amount the Camera should be translated in the y direction.
     * @param   z   The amount the Camera should be translated in the z direction.
     */
    public synchronized void translateCamera(double x, double y, double z) {
        camera.translate(new Vector3(x, y, z));
    }
    /**
     * Move the Camera that the Window uses to render objects.
     * 
     * @param   displacement    A Vector3 containing the desired x, y, and z displacement, 
     *                          respectively.
     */
    public synchronized void translateCamera(Vector3 displacement) {
        camera.translate(displacement);
    }
    
    /**
     * Set the position of the Camera that the Window uses to render objects.
     * 
     * @param   position    A Vector3 containing the desired x, y, and z coordinates, 
     *                      respectively.
     */
    public synchronized void setCameraPosition(Vector3 position) {
        camera.setPosition(position);
    }
    
    /**
     * Set the direction of the Camera that the Window uses to render objects.
     * 
     * @param   direction   A Vector3 containing the desired x, y, and z directions, 
     *                      respectively.
     */
    public synchronized void setCameraDirection(Vector3 direction) {
        camera.setDirection(direction);
    }
    
    /**
     * Convert a Polygon3 to an array of x-coordinates, an array of y-coordinates, and a Color.
     * 
     * @param   polygon The 3D polygon that needs to be converted.
     * @return  An Object[] containing:
     *              - An int[] containing the x-coordinates of the vertices on the screen.
     *              - An int[] containing the y-coordinates of the vertices on the screen.
     *              - The Color of the Polygon3.
     */
    private Object[] polygonToDrawable(Polygon3 polygon) {
        int[] xCoords = new int[polygon.length()];
        int[] yCoords = new int[xCoords.length];
        
        Vector3 point;
        for (int i = 0; i < xCoords.length; i++) {
            point = polygon.point(i);
            xCoords[i] = (int) (point.X * xScaleFactor) + halfWidth;
            yCoords[i] = halfHeight - (int) (point.Y * yScaleFactor);
        }
        return new Object[]{xCoords, yCoords, polygon.color()};
    }
    
    /**
     * Draw a polygon on screen given its coordinate and color data.
     * 
     * @paramAn Object[] containing:
     *              - An int[] containing the x-coordinates of the vertices on the screen.
     *              - An int[] containing the y-coordinates of the vertices on the screen.
     *              - The Color of the Polygon3.
     */
    private synchronized void drawPolygon(Graphics g, Object[] polyData) {
        int[] xCoords = (int[]) polyData[0];
        int[] yCoords = (int[]) polyData[1];
        g.setColor((Color) polyData[2]);
        g.fillPolygon(xCoords, yCoords, xCoords.length);
    }
    
    /**
     * Draw all tracked Models on the screen.
     */
    public synchronized void paint(Graphics g) {
        List<Polygon3> polys = new ArrayList<Polygon3>();
        for (Model3 model : models) {
            polys.addAll(Arrays.asList(camera.render(model)));
        }

        Polygon3[] sortedPolys = Polygon3.sort(polys.toArray(new Polygon3[0]));
        Stream<Object[]> drawablePolys = Arrays.stream(sortedPolys).parallel()
                .filter(p -> p.minZ() >= 0).map(this::polygonToDrawable);
        
        g.clearRect(0, 0, pixelsX, pixelsY);
        drawablePolys.sequential().forEach(p -> this.drawPolygon(g, p));
    }
    
    @Override
    public synchronized void update(Graphics g) {
        paint(g);
    }
}
