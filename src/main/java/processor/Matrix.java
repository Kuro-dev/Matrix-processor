package processor;

import java.io.Serial;
import java.io.Serializable;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Pattern;

public class Matrix implements Serializable {
    private static final Pattern NUMBER_PATTERN = Pattern.compile("(\\d+[.,]\\d+)|\\d+");
    @Serial
    private static final long serialVersionUID = 3945716045575686061L;

    private final int width;
    private final int height;
    private final double[][] matrix;


    public Matrix(int width, int height) {
        this(width, height, new double[height][width]);
    }

    private Matrix(int width, int height, double[][] matrix) {
        this.width = width;
        this.height = height;
        this.matrix = matrix;
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
                if (datum.length == width) {
                    return new Matrix(width, height, data);
                } else {
                    throw new IllegalArgumentException("All rows must have the same length");
                }
            }
        }
        throw new IllegalArgumentException("Matrix must have at least one row.");
    }

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

    private static Matrix error(String msg) {
        return new ErrorMatrix(msg);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Deprecated(forRemoval = true)
    public boolean fill(Scanner input) {
        try {
            System.out.printf("Reading %dx%d matrix%n", width, height);
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    double val = input.nextDouble();
                    set(val, x, y);
                }
            }
            return true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

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
     * @return adds the 2 given matrizes together
     * @apiNote may return an error instance.
     * @see #isError()
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
        return error("Dimensions of the 2 matrizes are different."
                + getDimension() + " != " + other.getDimension());
    }

    void set(double val, int x, int y) {
        matrix[y][x] = val;
    }

    double get(int x, int y) {
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

    @Override
    public String toString() {
        var out = new StringBuilder();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                out.append(get(x, y)).append(" ");
            }
            out.append("\n");
        }
        return out.toString();
    }

    public Matrix multiply(double factor) {
        Matrix out = new Matrix(width, height);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                out.set(get(x, y) * factor, x, y);
            }
        }
        return out;
    }

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
        throw new IllegalArgumentException("Matrizes must be of the same size");
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
     * @param type the type of the transposition. See {@link TranspositionType}
     * @return a transposed matrix
     * @throws IllegalArgumentException if the matrix width != matrix height
     * @see TranspositionType
     */
    public Matrix transpose(TranspositionType type) {
        if (width == height)
            return type.apply(this);
        else {
            throw new IllegalArgumentException("Matrix width and height must be the same");
        }
    }

    /**
     * @return The determinant of the given matrix or -1 if it cannot be computed.
     * A determinant can only be computed if:
     * <p>The width and height of the matrix are equal</p>
     */
    public double getDeterminant() {
        if (width - height == 0) {
            if (width == 2) {
                return (get(0, 0) * get(1, 1)) - (get(1, 0) * get(0, 1));
            } else {
                double result = 0;
                for (int x = 0; x < width; x++) {
                    int factor = (x & 1) == 0 ? 1 : -1;
                    double val = get(x, 0);
                    result += (val * getCofactor(x)) * factor;
                }
                return result;
            }
        }
        return -1;
    }

    public Matrix getMinor(int excludedX) {
        return getMinor(excludedX, 0);
    }

    public Matrix getMinor(int excludedX, int excludedY) {
        Matrix cofactor = new Matrix(width - 1, width - 1);
        int cofactorY = 0;
        int cofactorX = 0;
        for (int row = 0; row < width; row++) {
            for (int col = 0; col < width; col++) {
                if (row != excludedX && col != excludedY) {
                    cofactor.set(get(row, col), cofactorX++, cofactorY);
                    if (cofactorX == width - 1) {
                        cofactorX = 0;
                        cofactorY++;
                    }
                }
            }
        }
        return cofactor;
    }

    public double getCofactor(int x, int y) {
        return getMinor(x, y).getDeterminant();
    }

    public double getCofactor(int x) {
        return getMinor(x).getDeterminant();
    }

    /**
     * @return The inverse of the given matrix.
     * Cannot compute if the determinant computes to 0
     * @apiNote may return an error instance.
     * @see #isError()
     */
    public Matrix inverse() {
        double det = getDeterminant();
        if (det == 0) {
            return error("determinant of the given matrix is 0," +
                    " this matrix does not have an inverse");
        }
        return getAdjoint().multiply(1 / det).transpose(TranspositionType.MAIN_DIAGONAL);
    }

    private Matrix getAdjoint() {
        Matrix adjoint = new Matrix(width, height);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // sign of adj[j][i] positive if sum of row
                // and column indexes is even.
                int sign = (((y + x) & 1) == 0) ? 1 : -1;
                // Interchanging rows and columns to get the
                // transpose of the cofactor matrix
                adjoint.set(sign * getCofactor(x, y), x, y);
            }
        }
        return adjoint;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Matrix matrix1 = (Matrix) o;
        return width == matrix1.width && height == matrix1.height && Arrays.deepEquals(matrix, matrix1.matrix);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(width, height);
        result = 31 * result + Arrays.deepHashCode(matrix);
        return result;
    }

    /**
     * @param digits The number of decimal places for each value
     * @return a visual String representation of the matrix
     */
    public String toString(int digits) {
        final String format = "%." + digits + "f";
        var out = new StringBuilder();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                String number = String.format(format, get(x, y));
                out.append(number).append(" ");
            }
            out.append("\n");
        }
        return out.toString();
    }

    public boolean isError() {
        return this instanceof ErrorMatrix;
    }
}
