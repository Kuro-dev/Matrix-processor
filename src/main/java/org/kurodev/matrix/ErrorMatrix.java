package org.kurodev.matrix;

/**
 * A Matrix indicating an invalid or impossible calculation.
 * Will always have an error message in {@link #toString()} and {@link #toString(int)}
 * Does store the original matrix
 *
 * @see #getOriginal()
 */
public class ErrorMatrix extends Matrix {

    private final String message;
    private final Matrix original;

    ErrorMatrix(String message, Matrix original) {
        super(0, 0);
        this.message = message;
        this.original = original;
    }

    @Override
    public Matrix multiply(double factor) {
        throw new UnsupportedOperationException(message);
    }

    @Override
    public Matrix multiply(Matrix other) {
        throw new UnsupportedOperationException(message);

    }

    @Override
    public Matrix copy(boolean includeValues) {
        throw new UnsupportedOperationException(message);

    }

    @Override
    public Matrix transpose(TranspositionType type) {
        throw new UnsupportedOperationException(message);

    }

    @Override
    public double getDeterminant() {
        throw new UnsupportedOperationException(message);

    }

    @Override
    public Matrix getMinor(int excludedX) {
        throw new UnsupportedOperationException(message);

    }

    @Override
    public Matrix getMinor(int excludedX, int excludedY) {
        throw new UnsupportedOperationException(message);

    }

    @Override
    public double getCofactor(int x, int y) {
        throw new UnsupportedOperationException(message);

    }

    @Override
    public double getCofactor(int x) {
        throw new UnsupportedOperationException(message);
    }

    @Override
    public boolean equals(Object o, double delta) {
        return false;
    }

    @Override
    public double[] toArray() {
        throw new UnsupportedOperationException(message);
    }

    @Override
    public Matrix inverse() {
        throw new UnsupportedOperationException(message);

    }

    @Override
    public boolean equals(Object o) {
        throw new UnsupportedOperationException(message);

    }

    @Override
    public String toString() {
        return message;
    }

    @Override
    public String toString(int digits) {
        return message;
    }

    @Override
    public boolean dimensionMatches(Matrix other) {
        throw new UnsupportedOperationException(message);
    }

    @Override
    public Matrix add(Matrix other) {
        throw new UnsupportedOperationException(message);
    }

    @Override
    void set(double val, int x, int y) {
        throw new UnsupportedOperationException(message);
    }

    @Override
    public double get(int x, int y) {
        throw new UnsupportedOperationException(message);
    }

    @Override
    double[] getRow(int y) {
        throw new UnsupportedOperationException(message);
    }

    @Override
    double[] getColumn(int x) {
        throw new UnsupportedOperationException(message);
    }

    @Override
    public Matrix subtract(Matrix value) {
        throw new UnsupportedOperationException(message);
    }

    /**
     * @return the original matrix that created this instance
     */
    public Matrix getOriginal() {
        return original;
    }
}
