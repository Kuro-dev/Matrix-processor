package org.kurodev.matrix;

import java.util.function.Function;

/**
 * Enum containing all the different types of Transpositions.
 * @see #HORIZONTAL_LINE
 * @see #MAIN_DIAGONAL
 * @see #SIDE_DIAGONAL
 * @see #VERTICAL_LINE
 */
public enum TranspositionType implements Function<Matrix, Matrix> {
    /**
     * <pre>
     * Example:
     *  input =                 expected =
     *      {1, 2, 3, 4, 5},            {1, 1, 1, 1, 1},
     *      {1, 2, 3, 4, 5},            {2, 2, 2, 2, 2},
     *      {1, 2, 3, 4, 5},            {3, 3, 3, 3, 3},
     *      {1, 2, 3, 4, 5},            {4, 4, 4, 4, 4},
     *      {1, 2, 3, 4, 5},            {5, 5, 5, 5, 5},
     * </pre>
     */
    MAIN_DIAGONAL {
        @SuppressWarnings("SuspiciousNameCombination")
            //this works as intended.
        void setValue(Matrix out, double val, int x, int y) {
            out.set(y, x, val);
        }
    },

    /**
     * <pre>
     * Example:
     *  input =                 expected =
     *      {1, 2, 3, 4, 5},            {5, 5, 5, 5, 5},
     *      {1, 2, 3, 4, 5},            {4, 4, 4, 4, 4},
     *      {1, 2, 3, 4, 5},            {3, 3, 3, 3, 3},
     *      {1, 2, 3, 4, 5},            {2, 2, 2, 2, 2},
     *      {1, 2, 3, 4, 5},            {1, 1, 1, 1, 1},
     * </pre>
     */
    SIDE_DIAGONAL {
        void setValue(Matrix out, double val, int x, int y) {
            out.set(out.getWidth() - (1 + y), out.getHeight() - (1 + x), val);
        }
    },
    /**
     * <pre>
     * Example:
     *  input =                 expected =
     *      {1, 2, 3, 4, 5},            {5, 4, 3, 2, 1},
     *      {1, 2, 3, 4, 5},            {5, 4, 3, 2, 1},
     *      {1, 2, 3, 4, 5},            {5, 4, 3, 2, 1},
     *      {1, 2, 3, 4, 5},            {5, 4, 3, 2, 1},
     *      {1, 2, 3, 4, 5},            {5, 4, 3, 2, 1},
     * </pre>
     */
    VERTICAL_LINE {
        void setValue(Matrix out, double val, int x, int y) {
            out.set(out.getWidth() - (1 + x), y, val);
        }
    },
    /**
     * <pre>
     * Example:
     *  input =                  expected =
     *      {10, 2, 3, 4, 5},           {1, 2, 3, 4, 50},
     *      {1, 20, 3, 4, 5},           {1, 2, 3, 40, 5},
     *      {1, 2, 30, 4, 5},           {1, 2, 30, 4, 5},
     *      {1, 2, 3, 40, 5},           {1, 20, 3, 4, 5},
     *      {1, 2, 3, 4, 50},           {10, 2, 3, 4, 5},
     * </pre>
     */
    HORIZONTAL_LINE {
        void setValue(Matrix out, double val, int x, int y) {
            out.set(x, out.getHeight() - (1 + y), val);
        }
    };

    @Override
    public Matrix apply(Matrix matrix) {
        Matrix out = matrix.copy(false);
        for (int x = 0; x < matrix.getWidth(); x++) {
            for (int y = 0; y < matrix.getHeight(); y++) {
                double val = matrix.get(x, y);
                setValue(out, val, x, y);
            }
        }
        return out;
    }

    abstract void setValue(Matrix out, double val, int x, int y);
}
