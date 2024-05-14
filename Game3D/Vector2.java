package Game3D;


/**
 * Represents two-dimensional vectors in rectangular form. Vector2 objects are immutable.
 * 
 * @author Andrew Yim
 * @version 11-11-2023
 */
public final class Vector2 {
    /**
     * The component of the Vector2 parallel to the x-axis.
     */
    public final double X;
    /**
     * The component of the Vector2 parallel to the y-axis.
     */
    public final double Y;
    
    private double length;
    
    private boolean lengthKnown = false;

    /**
     * Instantiate a new Vector2. x and y represent the components of the vector parallel to i and j, 
     * respectively.
     */
    public Vector2(double x, double y) {
        X = x;
        Y = y;
    }
    
    /**
     * toString method for Vector2. Returns a string in the form "Vector2(x, y)".
     * 
     * @return "Vector2(x, y)", where x, y are <code>this</code>'s x and y values, 
     * respectively.
     */
    @Override
    public String toString() {
        return "Vector2(" + X + ", " + Y + ")";
    }
    
    
    /** 
     * @return double
     */
    public double length() {
        if (lengthKnown) { // Dynamic programming go brrrrrr
            return length;
        }
        length = Math.sqrt(X*X + Y*Y);
        lengthKnown = true;
        return length;
    }
    
    /**
     * Find a Vector2 parallel to <code>this</code> with a length of one.
     * 
     * @return  A Vector2 object with a length of one and the same direction as <code>this</code>.
     */
    public Vector2 unit() {
        if (equals(ZERO)) {
            return ZERO;
        }
        return multiply(1/length());
    }
    
    /**
     * Determine whether two Vector2s' coordinates are identical. That is, for any vectors (x1, y1) 
     * and (x2, y2), the first vector equals the second if and only if x1=x2 and y1=y2.
     * 
     * @param   vector  The Vector2 to which <code>this</code> should be compared.
     * @return  <code>true</code> if the coordinates are identical, <code>false</code> otherwise.
     */
    public boolean equals(Vector2 vector) {
        return (X == vector.X && Y == vector.Y);
    }
    
    /**
     * Determine whether two Vector2s are parallel. For any vectors v1 and v2, v1 is parallel to v2 if 
     * and only if there is some value r such that rv1 = v2.
     * 
     * @param   vector  The Vector2 to which <code>this</code> should be compared.
     * @return  <code>true</code> if the vectors are parallel, <code>false</code> otherwise.
     */
    public boolean isParallel(Vector2 vector) {
        double r = X/vector.X;
        return Math.abs(Y/vector.Y - r) <= 0.00001d;
    }
    
    /**
     * Determine whether two Vector2s are perpendicular. Two vectors are parallel if and only if 
     * their dot product is zero.
     * 
     * @param   vector  The Vector2 to which <code>this</code> should be compared.
     * @return  <code>true</code> if the vectors are perpendicular, <code>false</code> otherwise.
     */
    public boolean isPerpendicular(Vector2 vector) {
        return Math.abs(dot(vector)) <= 0.00001d;
    }
    
    /**
     * Determine the sum of two Vector2s. The sum of two vectors is a vector equal to 
     * (x1+x2, y1+y2), where (x1, y1) and (x2, y2) are both vectors.
     * 
     * @param   vector  The Vector2 to be added to <code>this</code>.
     * @return  The sum <code>this + vector</code> as a Vector2.
     */
    public Vector2 add(Vector2 vector) {
        return new Vector2(X+vector.X, Y+vector.Y);
    }
    
    /**
     * Determine the difference of two Vector2s. The difference of two vectors is a vector equal to 
     * (x1-x2, y1-y2), where (x1, y1) and (x2, y2) are both vectors. Uses 
     * <code>this</code> as the first vector.
     * 
     * @param   vector  The Vector2 to be subtracted from <code>this</code>.
     * @return  The difference <code>this - vector</code> as a Vector2.
     */
    public Vector2 subtract(Vector2 vector) {
        return new Vector2(X-vector.X, Y-vector.Y);
    }
    
    /**
     * Determine the scalar product of a Vector2 and an double. The scalar product is a vector equal to 
     * (k*a, k*b) for a scalar k and a vector (x, y).
     * 
     * @param   factor  The scalar factor to be multiplied with <code>this</code>.
     * @return  The product <code>factor(this)</code> as a Vector2.
     */
    public Vector2 multiply(double factor) {
        return new Vector2(factor*X, factor*Y);
    }
    
    /**
     * Determine the Euclidean distance to another Vector2.
     * 
     * @param   point   The other point.
     * @return  The Euclidean distance between this point and the other in 3D space.
     */
    public double distanceTo(Vector2 point) {
        return subtract(point).length;
    }
    
    /**
     * Determine the dot product of two Vector2s. The dot product is a scalar equal to 
     * x1*x2 + y1*y2, where the first vector is (x1, y1) and the second is (x2, y2).
     *
     * @param   vector  The Vector2 whose dot product with <code>this</code> is to be taken.
     * @return  The dot product <code>this ⋅ vector</code> as an double.
     */
    public double dot(Vector2 vector)
    {
        return X*vector.X + Y*vector.Y;
    }
    
    /**
     * Determine the vector component of <code>this</code> parallel to another Vector2. Read as 
     * "comp <code>this</code> on (other vector)".
     * 
     * @param   vector  A vector parallel to the desired vector component.
     * @return  The vector <code>comp this on vector</code> as a Vector2.
     */
    public Vector2 comp(Vector2 vector) {
        double k = dot(vector) / vector.dot(vector); // v dot t over t dot t
        return vector.multiply(k);
    }
    
    /**
     * Determine the length of the vector component of <code>this</code> parallel to another Vector2. 
     * Read as "scalar comp <code>this</code> on (other vector)".
     * 
     * @param   vector  A vector parallel to the desired vector component.
     * @return  The length of the vector <code>comp this on vector</code> as a double.
     */
    public double scalarComp(Vector2 vector) {
        return comp(vector).length();
    }
    
    //===================================STATICS===================================
    
    /**
     * The zero vector.
     */
    public static final Vector2 ZERO = new Vector2(0, 0);
    /**
     * The unit vector in the direction of the positive x-axis.
     */
    public static final Vector2 I = new Vector2(1, 0);
    /**
     * The unit vector in the direction of the positive y-axis.
     */
    public static final Vector2 J = new Vector2(0, 1);
}
