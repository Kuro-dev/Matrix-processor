package processor;

import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class Matrix {
    public static final Matrix ERROR = new Matrix(0, 0) {
        @Override
        public String toString() {
            return "ERROR";
        }
    };
    private final int width;
    private final int height;
    private final double[][] matrix;


    public Matrix(int width, int height) {
        this(width, height, new double[height][width]);
    }

    public Matrix(int width, int height, double[][] matrix) {
        this.width = width;
        this.height = height;
        this.matrix = matrix;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

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
        return ERROR;
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
        return ERROR;
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
     * @param includeValues if true will copy all values of the 2dim array.
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

    public Matrix transpose(TranspositionType type) {
        return type.apply(this);
    }

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
        Matrix cofactor = new Matrix(width-1,width-1);
        int cofactorY = 0;
        int cofactorX = 0;
        for (int row = 0; row < width; row++) {
            for (int col = 0; col < width; col++) {
                if (row != excludedX && col != excludedY) {
                    cofactor.set(get(row,col),cofactorX++,cofactorY);
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

    public Matrix inverse() {
        double det = getDeterminant();
        if (det == 0) {
            System.out.print("Singular matrix, can't find its inverse");
            return ERROR;
        }
        return getAdjoint().multiply(1/det).transpose(TranspositionType.MAIN_DIAGONAL);
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
}
