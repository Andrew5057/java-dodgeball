package Game3D;


/**
 * Represents three-dimensional vectors in rectangular form. Vector3 objects are immutable.
 * 
 * @author Andrew Yim
 * @version 11-11-2023
 */
public final class Vector3 {
    /**
     * The component of the Vector3 parallel to the x-axis.
     */
    public final double X;
    /**
     * The component of the Vector3 parallel to the y-axis.
     */
    public final double Y;    
    /**
     * The component of the Vector3 parallel to the z-axis.
     */
    public final double Z;
    
    private double length;
    
    private boolean lengthKnown = false;

    /**
     * Instantiate a new Vector3. x, y, and z represent the components of the vector parallel to i, j, 
     * and k respectively.
     */
    public Vector3(double x, double y, double z) {
        X = x;
        Y = y;
        Z = z;
    }
    
    /**
     * Convert a Vector2 to a Vector3. X and Y will be preserved; Z will be set to 0.
     */
    public Vector3(Vector2 vector) {
        X = vector.X;
        Y = vector.Y;
        Z = 0;
    }
    
    /**
     * toString method for Vector3. Returns a string in the form "Vector3(x, y, z)".
     * 
     * @return "Vector3(x, y, z)", where x, y, and z are <code>this</code>'s x, y, and z values, 
     * respectively.
     */
    @Override
    public String toString() {
        return "Vector3(" + X + ", " + Y + ", " + Z + ")";
    }
    
    /**
     * Convert a Vector3 to a Vector2. X and Z will be preserved; Y will be ignored.
     */
    public Vector2 flatten() {
        return new Vector2(X, Z);
    }
    
    public double length() {
        if (lengthKnown) { // Dynamic programming go brrrrrr
            return length;
        }
        length = Math.sqrt(X*X + Y*Y + Z*Z);
        lengthKnown = true;
        return length;
    }
    
    /**
     * Find a Vector3 parallel to <code>this</code> with a length of one.
     * 
     * @return  A Vector3 object with a length of one and the same direction as <code>this</code>.
     */
    public Vector3 unit() {
        if (equals(ZERO)) {
            return ZERO;
        }
        return multiply(1/length());
    }
    
    /**
     * Determine whether two Vector3s' coordinates are identical. That is, for any vectors (x1, y1, z1) 
     * and (x2, y2, z2), the first vector equals the second if and only if x1=x2, y1=y2, and z1=z2.
     * 
     * @param   vector  The Vector3 to which <code>this</code> should be compared.
     * @return  <code>true</code> if the coordinates are identical, <code>false</code> otherwise.
     */
    public boolean equals(Vector3 vector) {
        return (X == vector.X && Y == vector.Y && Z == vector.Z);
    }
    
    /**
     * Determine whether two Vector3s are parallel. For any vectors v1 and v2, v1 is parallel to v2 if 
     * and only if there is some value r such that rv1 = v2.
     * 
     * @param   vector  The Vector3 to which <code>this</code> should be compared.
     * @return  <code>true</code> if the vectors are parallel, <code>false</code> otherwise.
     */
    public boolean isParallel(Vector3 vector) {
        double r = X/vector.X;
        if (Math.abs(Y/vector.Y - r) > 0.00001d) {
            return false;
        }
        if (Math.abs(Z/vector.Z - r) > 0.00001d) {
            return false;
        }
        return true;
    }
    
    /**
     * Determine whether two Vector3s are perpendicular. Two vectors are parallel if and only if 
     * their dot product is ZERO.
     * 
     * @param   vector  The Vector3 to which <code>this</code> should be compared.
     * @return  <code>true</code> if the vectors are perpendicular, <code>false</code> otherwise.
     */
    public boolean isPerpendicular(Vector3 vector) {
        return Math.abs(dot(vector)) <= 0.00001d;
    }
    
    /**
     * Determine the sum of two Vector3s. The sum of two vectors is a vector equal to 
     * (x1+x2, y1+y2, z1+z2), where (x1, y1, z1) and (x2, y2, z2) are both vectors.
     * 
     * @param   vector  The Vector3 to be added to <code>this</code>.
     * @return  The sum <code>this + vector</code> as a Vector3.
     */
    public Vector3 add(Vector3 vector) {
        return new Vector3(X+vector.X, Y+vector.Y, Z+vector.Z);
    }
    
    /**
     * Determine the difference of two Vector3s. The difference of two vectors is a vector equal to 
     * (x1-x2, y1-y2, z1-z2), where (x1, y1, z1) and (x2, y2, z2) are both vectors. Uses 
     * <code>this</code> as the first vector.
     * 
     * @param   vector  The Vector3 to be subtracted from <code>this</code>.
     * @return  The difference <code>this - vector</code> as a Vector3.
     */
    public Vector3 subtract(Vector3 vector) {
        return new Vector3(X-vector.X, Y-vector.Y, Z-vector.Z);
    }
    
    /**
     * Determine the scalar product of a Vector3 and an double. The scalar product is a vector equal to 
     * (k*a, k*b, k*c) for a scalar k and a vector (x, y, z).
     * 
     * @param   factor  The scalar factor to be multiplied with <code>this</code>.
     * @return  The product <code>factor(this)</code> as a Vector3.
     */
    public Vector3 multiply(double factor) {
        return new Vector3(factor*X, factor*Y, factor*Z);
    }
    
    /**
     * Determine the Euclidean distance to another Vector3.
     * 
     * @param   point   The other point.
     * @return  The Euclidean distance between this point and the other in 3D space.
     */
    public double distanceTo(Vector3 point) {
        return subtract(point).length;
    }
    
    /**
     * Determine the dot product of two Vector3s. The dot product is a scalar equal to 
     * x1*x2 + y1*y2 + z1*z2, where the first vector is (x1, y1, z1) and the second is (x2, y2, z2).
     *
     * @param   vector  The Vector3 whose dot product with <code>this</code> is to be taken.
     * @return  The dot product <code>this ⋅ vector</code> as an double.
     */
    public double dot(Vector3 vector) {
        return X*vector.X + Y*vector.Y + Z*vector.Z;
    }
    
    /**
     * Determine the cross product of two Vector3s. The cross product is a vector equal to the 
     * discriminant of the 3x3 matrix [ [i, j, k], [x1, y1, z1], [x2, y2, z2] ], where the first 
     * vector is (x1, y1, z1) and the second is (x2, y2, z2). Uses <code>this</code> as the first vector.
     * 
     * @param   vector  The Vector3 whose cross product with <code>this</code> is to be taken.
     * @return  The cross product <code>this x vector</code> as a Vector3.
     */
    public Vector3 cross(Vector3 vector) {
        return new Vector3(Y*vector.Z - Z*vector.Y, Z*vector.X - X*vector.Z, X*vector.Y-Y*vector.X);
    }
    
    /**
     * Determine the vector component of <code>this</code> parallel to another Vector3. Read as 
     * "comp <code>this</code> on (other vector)".
     * 
     * @param   vector  A vector parallel to the desired vector component.
     * @return  The vector <code>comp this on vector</code> as a Vector3.
     */
    public Vector3 comp(Vector3 vector) {
        double tDotT = vector.dot(vector);
        if (tDotT == 0) {
            return ZERO;
        }
        double k = dot(vector) / tDotT; // v dot t over t dot t
        return vector.multiply(k);
    }
    
    /**
     * Determine the length of the vector component of <code>this</code> parallel to another Vector3. 
     * Read as "scalar comp <code>this</code> on (other vector)".
     * 
     * @param   vector  A vector parallel to the desired vector component.
     * @return  The length of the vector <code>comp this on vector</code> as a double.
     */
    public double scalarComp(Vector3 vector) {
        return comp(vector).length();
    }
    
    //===================================STATICS===================================
    
    /**
     * The zero vector.
     */
    public static final Vector3 ZERO = new Vector3(0, 0, 0);
    /**
     * The unit vector in the direction of the positive x-axis.
     */
    public static final Vector3 I = new Vector3(1, 0, 0);
    /**
     * The unit vector in the direction of the positive y-axis.
     */
    public static final Vector3 J = new Vector3(0, 1, 0);
    /**
     * The unit vector in the direction of the positive z-axis.
     */
    public static final Vector3 K = new Vector3(0, 0, 1);
}
