package processor;

public class ErrorMatrix extends Matrix {

    private final String message;

    public ErrorMatrix(String message) {
        super(0, 0);
        this.message = message;
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
    public Matrix inverse() {
        throw new UnsupportedOperationException(message);

    }

    @Override
    public boolean equals(Object o) {
        throw new UnsupportedOperationException(message);

    }

    @Override
    public String toString(int digits) {
        return message;
    }
}
