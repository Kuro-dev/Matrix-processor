package org.kurodev.matrix;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

public class Matrix {
    private static final Pattern NUMBER_PATTERN = Pattern.compile("(\\d+[.,]\\d+)|\\d+");

    private final int width;
    private final int height;
    private final double[][] matrix;
    private Double determinant = null;

    protected Matrix(int width, int height) {
        this(width, height, new double[height][width]);
    }

    protected Matrix(int width, int height, double[][] matrix) {
        this.width = width;
        this.height = height;
        this.matrix = matrix;
    }

    public static Matrix of(int width, int height) {
        return new Matrix(width, height);
    }

    /**
     * @param width  Width of the matrix
     * @param height Height of the matrix
     * @param rng    Random instance
     * @return A randomized instance
     */
    public static Matrix of(int width, int height, Random rng) {
        double[][] dataSet = new double[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                dataSet[y][x] = rng.nextDouble();
            }
        }
        return new Matrix(width, height, dataSet);
    }

    /**
     * @param data the two-dimensional matrix.
     * @return a matrix filled with the given data.
     * @throws IllegalArgumentException if the data is empty or the length if the rows differs
     */
    public static Matrix of(double[][] data) throws IllegalArgumentException {
        int height = data.length;
        if (height > 0) {
            int width = data[0].length;
            for (double[] datum : data) {
                if (datum.length != width) {
                    throw new IllegalArgumentException("All rows must have the same length");
                }
            }
            return new Matrix(width, height, data);
        }
        throw new IllegalArgumentException("Matrix must have at least one row.");
    }

    /**
     * Example String:
     * <p>n n n n</p>
     * <p>n n n n</p>
     * <p>n n n n</p>
     *
     * @param matrixData The matrix string representation
     * @return A parsed matrix object
     */
    public static Matrix of(String matrixData) {
        List<List<Double>> doubleLines = new ArrayList<>();
        String[] lines = matrixData.split("[\n]");
        int height = lines.length;
        int width = -1;
        for (String line : lines) {
            List<Double> lineDataSet = new ArrayList<>();
            var data = NUMBER_PATTERN.matcher(line);
            while (data.find()) {
                String number = data.group();
                lineDataSet.add(Double.parseDouble(number));
            }
            doubleLines.add(lineDataSet);
            if (width == -1) {
                width = lineDataSet.size();
            } else if (width != lineDataSet.size()) {
                throw new IllegalArgumentException("Matrix has differing line lengths");
            }
        }
        double[][] dataSet = new double[height][width];
        for (int y = 0; y < doubleLines.size(); y++) {
            var line = doubleLines.get(y);
            for (int x = 0; x < line.size(); x++) {
                dataSet[y][x] = line.get(x);
            }
        }
        return of(dataSet);
    }

    public static Matrix identityMatrix(int size) {
        var out = new Matrix(size, size);
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                if (x == y) out.set(1, x, y);
                else out.set(0, x, y);
            }
        }
        return out;
    }

    public static Matrix of(byte[] data) {
        byte[] buf = new byte[Double.BYTES];
        System.arraycopy(data, 0, buf, 0, Integer.BYTES);
        int width = ByteUtils.toInt(buf);
        System.arraycopy(data, Integer.BYTES, buf, 0, Integer.BYTES);
        int height = ByteUtils.toInt(buf);
        int expectedLength = (width * height * Double.BYTES) + Integer.BYTES * 2;
        if (expectedLength > data.length) {
            String msg = "Expected " + expectedLength + "bytes but got " + data.length;
            return new ErrorMatrix(msg, null);
        }
        int pos = (Integer.BYTES * 2);
        Matrix result = new Matrix(width, height);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int dataPos = ((x * height) + y) * Double.BYTES;
                System.arraycopy(data, pos, buf, 0, Double.BYTES);
                pos += Double.BYTES;
                double value = ByteUtils.toDouble(buf);
                result.set(value, x, y);
            }
        }
        return result;
    }

    private Matrix error(String msg) {
        return new ErrorMatrix(msg, this);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    /**
     * @param other Other matrix to check against
     * @return true if width and height are equal
     */
    public boolean dimensionMatches(Matrix other) {
        return width == other.width && height == other.height;
    }

    /**
     * @return the dimensions of this matrix in the format "width x height"
     */
    public String getDimension() {
        return width + "x" + height;
    }

    /**
     * adds the 2 matrices together.
     * In order for this operation to complete successfully the dimensions of the matrices must match.
     * <p>widthA == widthB</p>
     * <p>heightA == heightB</p>
     *
     * @return A new matrix with resulting values
     * @apiNote May return an {@link ErrorMatrix}
     * @see #isError()
     * @see #dimensionMatches(Matrix)
     */
    public Matrix add(Matrix other) {
        if (dimensionMatches(other)) {
            Matrix output = new Matrix(width, height);
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    output.set(get(x, y) + other.get(x, y), x, y);
                }
            }
            return output;
        }
        return error("Dimensions of the 2 matrices are different."
                + getDimension() + " != " + other.getDimension());
    }

   protected final void set(double val, int x, int y) {
       determinant = null;
       matrix[y][x] = val;
    }

    /**
     * @param x row indicator
     * @param y column indicator
     * @return the value at that position.
     * @throws IndexOutOfBoundsException if the given integers are out of bounds of the matrix.
     */
    public double get(int x, int y) {
        return matrix[y][x];
    }

    double[] getRow(int y) {
        double[] out = new double[width];
        for (int i = 0; i < out.length; i++) {
            out[i] = get(i, y);
        }
        return out;
    }

    double[] getColumn(int x) {
        double[] out = new double[height];
        for (int i = 0; i < out.length; i++) {
            out[i] = get(x, i);
        }
        return out;
    }

    /**
     * Multiplies every value in the matrix by the given factor.
     *
     * @param scalar The multiplicator
     * @return A new matrix with the multiplied values
     */
    public Matrix multiply(double scalar) {
        Matrix out = new Matrix(width, height);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                out.set(get(x, y) * scalar, x, y);
            }
        }
        return out;
    }

    /**
     * Multiplies to matrices together. In order for this operation to complete successfully
     * Matrix {@code a.width} must equal {@code b.height} and vis versa
     *
     * @param other Matrix to multiply with
     * @return A new matrix with the multiplied values
     * @apiNote May return an {@link ErrorMatrix} if the width and height of the matrices do not fit.
     * @see #isError()
     */
    public Matrix multiply(Matrix other) {
        if (checkForMultiply(other)) {
            Matrix output = new Matrix(other.width, height);
            for (int y = 0; y < height; y++) {
                double[] row = getRow(y);
                for (int x = 0; x < other.width; x++) {
                    double[] column = other.getColumn(x);
                    output.set(multiply(row, column), x, y);
                }
            }
            return output;
        } else if (other.checkForMultiply(this)) {
            return other.multiply(this);
        }
        return error("Width and height do not match.");
    }

    private double multiply(double[] row, double[] column) {
        double result = 0;
        for (int i = 0; i < row.length; i++) {
            result += row[i] * column[i];
        }
        return result;
    }

    private boolean checkForMultiply(Matrix other) {
        return this.width == other.height;
    }

    /**
     * @param includeValues if {@code true} will copy all values of the matrix.
     *                      if {@code false} will simply instantiate another matrix of the same dimension
     * @return a matrix of the same size
     */
    public Matrix copy(boolean includeValues) {
        Matrix m = new Matrix(width, height);
        if (includeValues) {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    m.set(this.get(x, y), x, y);
                }
            }
        }
        return m;
    }

    /**
     * @return a transposed matrix along the main diagonal
     * @apiNote May return an {@link ErrorMatrix} if the width and height of the matrix differ
     * @see #isError()
     * @see TranspositionType#MAIN_DIAGONAL
     */
    public Matrix transpose() {
        return transpose(TranspositionType.MAIN_DIAGONAL);
    }

    /**
     * @param type the type of the transposition. See {@link TranspositionType}
     * @return a transposed matrix
     * @apiNote May return an {@link ErrorMatrix} if the width and height of the matrix differ
     * @see #isError()
     * @see TranspositionType
     */
    public Matrix transpose(TranspositionType type) {
        if (width == height)
            return type.apply(this);
        else {
            return error("Matrix width and height must be the same");
        }
    }

    /**
     * @return The determinant of the given matrix or {@link Double#NaN} if it cannot be computed.
     * A determinant can only be computed if:
     * <p>The width and height of the matrix are equal</p>
     * <p>May return {@link Double#NaN}</p>
     */
    public double getDeterminant() {
        if (determinant == null) {
            if (width == height) {
                if (width == 2) {
                    determinant = (get(0, 0) * get(1, 1)) - (get(1, 0) * get(0, 1));
                } else {
                    double result = 0;
                    for (int x = 0; x < width; x++) {
                        int factor = (x & 1) == 0 ? 1 : -1;
                        double val = get(x, 0);
                        result += (val * getCofactor(x)) * factor;
                    }
                    determinant = result;
                }
            } else {
                determinant = Double.NaN;
            }
        }
        return determinant;
    }

    /**
     * Generates a minor matrix of dimension x-1/y-1
     *
     * @param excludedX The row to exclude from the old matrix when copying values
     */
    public Matrix getMinor(int excludedX) {
        return getMinor(excludedX, 0);
    }

    /**
     * Generates a minor matrix of dimension x-1/y-1
     *
     * @param excludedX The row to exclude from the old matrix when copying values
     * @param excludedY The column to exclude from the old matrix when copying values.
     *                  Default: {@code 0}
     */
    public Matrix getMinor(int excludedX, int excludedY) {
        Matrix minor = new Matrix(width - 1, width - 1);
        int cofactorY = 0;
        int cofactorX = 0;
        for (int row = 0; row < width; row++) {
            for (int col = 0; col < width; col++) {
                if (row != excludedX && col != excludedY) {
                    minor.set(get(row, col), cofactorX++, cofactorY);
                    if (cofactorX == width - 1) {
                        cofactorX = 0;
                        cofactorY++;
                    }
                }
            }
        }
        return minor;
    }

    /**
     * @apiNote May return an {@link ErrorMatrix} if the given matrices minor does not have a determinant
     * @see #isError()
     */
    public double getCofactor(int x, int y) {
        return getMinor(x, y).getDeterminant();
    }

    /**
     * @apiNote May return an {@link ErrorMatrix} if the given matrices minor does not have a determinant
     * @see #isError()
     */
    public double getCofactor(int x) {
        return getMinor(x).getDeterminant();
    }

    /**
     * @return The inverse of the given matrix.
     * Cannot compute if the determinant computes to 0
     * @apiNote May return an {@link ErrorMatrix} if the given matrix does not have a determinant
     * @see #isError()
     */
    public Matrix inverse() {
        double det = getDeterminant();
        if (det == 0 || Double.isNaN(det)) {
            return error("this matrix does not have an inverse");
        }
        var result = copy(false);
        if (width == 2 && height == 2) {
            double a = get(0, 0);
            result.set(a, 1, 1);
            double d = get(1, 1);
            result.set(d, 0, 0);

            double b = get(1, 0);
            result.set(b * -1, 1, 0);
            double c = get(0, 1);
            result.set(c * -1, 0, 1);
            return result.multiply(1 / det);
        }
        return getAdjoint().multiply(1 / det).transpose();
    }

    private Matrix getAdjoint() {
        Matrix adjoint = new Matrix(width, height);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // sign of adj[y][x] positive if sum of row
                // and column indexes is even.
                int sign = (((y + x) & 1) == 0) ? 1 : -1;
                // Interchanging rows and columns to get the
                // transpose of the cofactor matrix
                adjoint.set(sign * getCofactor(x, y), x, y);
            }
        }
        return adjoint;
    }

    /**
     * @see #equals(Object, double)
     */
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override
    public boolean equals(Object obj) {
        return equals(obj, 0.01d);
    }

    /**
     * @param o     other object to compare to
     * @param delta the allowed divergence between 2 different matrix values. higher values mean less accuracy.
     *              Default: {@code 0.01d}
     * @return true if the 2 objects are equal given the delta
     */
    public boolean equals(Object o, double delta) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Matrix other = (Matrix) o;
        if (width == other.width && height == other.height) {
            boolean equal = true;
            for (int y = 0; y < height; y++)
                for (int x = 0; x < width; x++) {
                    if (equal) {
                        equal = !doubleIsDifferent(get(x, y), other.get(x, y), delta);
                    } else {
                        return false;
                    }
                }
            return equal;
        }
        return false;
    }

    private boolean doubleIsDifferent(double d1, double d2, double delta) {
        if (Double.compare(d1, d2) == 0) {
            return false;
        } else {
            return !(Math.abs(d1 - d2) <= delta);
        }
    }

    /**
     * @return An array with length of
     * {@link #width} * {@link #height}
     */
    public double[] toArray() {
        return Stream.of(matrix).flatMapToDouble(DoubleStream::of).toArray();
    }

    //untested
    public void sigmoid() {
        Matrix temp = copy(false);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                temp.set(1 / (1 + Math.exp(-get(x, y))), x, y);
            }
        }
    }

    //untested
    public void dsigmoid() {
        Matrix temp = copy(false);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                temp.set(get(x, y) * (1 - get(x, y)), x, y);
            }
        }
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(width, height);
        result = 31 * result + Arrays.deepHashCode(matrix);
        return result;
    }

    @Override
    public String toString() {
        return toString(-1);
    }

    /**
     * @param digits The number of decimal places for each value
     * @return a visual String representation of the matrix
     * @implNote This string is parsable using {@link #of(String) Matrix.of(String)} method
     */
    public String toString(int digits) {
        final String format;
        if (digits == -1) {
            format = "%f";
        } else {
            format = "%." + digits + "f";
        }
        var out = new StringBuilder();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                String number = String.format(format, get(x, y));
                out.append(number).append(" ");
            }
            out.append("\n");
        }
        return out.toString().replace(",", ".");
    }

    public boolean isError() {
        return this instanceof ErrorMatrix;
    }

    public byte[] toByteArray() {
        final byte[] out = new byte[(Integer.BYTES * 2) + (width * height * Double.BYTES)];
        System.arraycopy(ByteUtils.toByteArray(width), 0, out, 0, Integer.BYTES);
        System.arraycopy(ByteUtils.toByteArray(height), 0, out, Integer.BYTES, Integer.BYTES);
        int startPos = (Integer.BYTES * 2);
        double[] data = toArray();
        for (int i = 0; i < data.length; i++) {
            int arrPos = startPos + (i * Double.BYTES);
            System.arraycopy(ByteUtils.toByteArray(data[i]), 0, out, arrPos, Double.BYTES);
        }
        return out;
    }

    /**
     * Calculates this - value
     *
     * @param value The value-matrix to subtract from this instance
     * @return the new instance
     * @apiNote May return an {@link ErrorMatrix} if the matrices are of different dimensions.
     * @see #isError()
     */
    public Matrix subtract(Matrix value) {
        if (dimensionMatches(value)) {
            Matrix res = copy(false);
            for (int y = 0; y < height; y++)
                for (int x = 0; x < width; x++) {
                    double result = get(x, y) - value.get(x, y);
                    res.set(result, x, y);
                }
            return res;
        }
        return error("Matrices must be of same dimensions");
    }

    /**
     * determines whether or not a matrix is symmetric or not.
     * In linear algebra, a symmetric matrix is a square matrix that is equal to its transpose.
     *
     * @implNote will return {@code false} if the matrix is not square.
     */
    public boolean isSymmetric() {
        return equals(transpose());
    }

    /**
     * @return true if the matrix is a real matrix.
     * A matrix is real only if all the values are positive real numbers.
     */
    public boolean isReal() {
        boolean isReal = true;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (isReal) {
                    isReal = get(x, y) > 0;
                } else {
                    break;
                }
            }
        }
        return isReal;
    }
}
