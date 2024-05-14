package Game3D;

import java.awt.Color;
import java.util.List;
import java.util.ArrayList;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.*;

/**
 * A 3D model composed of several 3D polygons.
 *
 * @author Andrew Yim
 * @version 11-13-2023
 */
public class Model3 implements Cloneable {
    private Vector3 center;
    private Polygon3[] polygons;
    private int length;

    /**
     * Define a Model3 in terms of an array containing its polygons as Polygon3s.
     */
    public Model3(Vector3 center, Polygon3[] polygons) {
        this.center = center;
        length = polygons.length;
        this.polygons = new Polygon3[length];
        for (int i = 0; i < length; i++) {
            this.polygons[i] = polygons[i].clone();
        }
    }
    
    /**
     * Import a Model3 object from a file. The first number in the file should be the 
     * number of Polygon3s that make up the Model3. Each line after should start with the number 
     * of points in the next Polygon3, then list the x, y, and z coordinates of each point, then 
     * the R, G, and B values of its color, all separated by spaces.
     * 
     * @param   file    A File object containing the desired data.
     */
    public Model3(File file) throws FileNotFoundException, IOException {
        center = Vector3.ZERO;
        DataInputStream input = new DataInputStream(new BufferedInputStream(
                new FileInputStream(file)));
        Vector3[] points;
        List<Polygon3> polys = new ArrayList<Polygon3>();
        length = input.readInt();
        int numPoints;
        double x, y, z;
        for (int i = 0; i < length; i++) {
            numPoints = input.readInt();
            points = new Vector3[numPoints];
            for (int j = 0; j < numPoints; j++) {
                x = input.readDouble();
                y = input.readDouble();
                z = input.readDouble();
                points[j] = new Vector3(x, y, z);
            }
            int r = input.readInt();
            int g = input.readInt();
            int b = input.readInt();
            Color color = new Color(r, g, b);
            polys.add(new Polygon3(points, color));
        }
        polygons = polys.toArray(new Polygon3[0]);
        input.close();
    }
    
    /**
     * Deep copy a Model3 object.
     */
    @Override
    public Model3 clone() {
        return new Model3(center, polygons);
    }
    
    /**
     * Get the number of Polygon3s contained by the Model3.
     * 
     * @return  The number of Polygon3s in the Model3 as an int.
     */
    public int length() {
        return length;
    }
    /**
     * Get an array containing all the Polygon3s in the Model3.
     * 
     * @return  A Polygon3[] containing all the Polygon3s in the Model3.
     */
    public Polygon3[] polygons() {
        polygons = new Polygon3[length];
        for (int i = 0; i < length; i++) {
            this.polygons[i] = polygons[i].clone();
        }
        return polygons;
    }
    /**
     * Get a specific Polygon3 in the Model3.
     * 
     * @param   index   The index of the desired Polygon3.
     * @return  The Polygon3 at that index.
     */
    public Polygon3 polygon(int index) {
        if (index < 0 || index >= length) {
            throw new IndexOutOfBoundsException("Index " + index + " is out of the range [0, " + 
                    (length-1) + "]");
        }
        return polygons[index].clone();
    }
    
    /**
     * Translates the Model3 along the given Vector3. Operates in-place; does not return a new 
     * Model3 object.
     * 
     * @param   vector  The vector along which the Model3 should be translated.
     */
    public void translate(Vector3 vector) {
        center = center.add(vector);
        for (int i = 0; i < length; i++) {
            polygons[i].translate(vector);
        }
    }
    
    /**
     * Rotates the Model3 around its center.
     * 
     * @param   yaw The number of degrees the Model3 should be rotated clockwise by.
     */
    public void rotate(double yaw) {
        yaw = -Math.toRadians(yaw);
        double sinYaw = Math.sin(yaw);
        double cosYaw = Math.cos(yaw);
        for (Polygon3 poly : polygons) {
            poly.rotate(center, sinYaw, cosYaw);
        }
    }
    
    /**
     * Write a Model3 to a .md3 file, given user input.
     */
    public static void designModel(String path) throws IOException {
        Scanner reader = new Scanner(System.in);
        System.out.print("Enter the name of the model: ");
        File outputFile = new File(path + "\\" + reader.nextLine() + ".md3");
        outputFile.createNewFile();
        
        DataOutputStream output = new DataOutputStream(new BufferedOutputStream(
                new FileOutputStream(outputFile)));
        
        System.out.print("Enter the number of polygons: ");
        int numPolys = reader.nextInt();
        System.out.println();
        output.writeInt(numPolys);
        
        for (int i = 0; i < numPolys; i++) {
            System.out.print("Enter the number of vertices: ");
            int vertices = reader.nextInt();
            System.out.println();
            output.writeInt(vertices);
            for (int j = 0; j < vertices; j++) {
                System.out.print("x-coordinate: ");
                output.writeDouble(reader.nextDouble());
                System.out.println();
                System.out.print("y-coordinate: ");
                output.writeDouble(reader.nextDouble());
                System.out.println();
                System.out.print("z-coordinate: ");
                output.writeDouble(reader.nextDouble());
                System.out.println();
            }
            System.out.print("Red color value: ");
            output.writeInt(reader.nextInt());
            System.out.println();
            System.out.print("Green color value: ");
            output.writeInt(reader.nextInt());
            System.out.println();
            System.out.print("Blue color value: ");
            output.writeInt(reader.nextInt());
            System.out.println();
        }
        reader.close();
        output.flush();
        output.close();
    }
}
