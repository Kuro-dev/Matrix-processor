package processor;

import java.util.function.Function;

public enum TranspositionType implements Function<Matrix, Matrix> {

    MAIN_DIAGONAL {
        void setValue(Matrix out, double val, int x, int y) {
            out.set(val, y, x);
        }
    },
    SIDE_DIAGONAL {
        void setValue(Matrix out, double val, int x, int y) {
            out.set(val, out.getWidth() - (1 + y), out.getHeight() - (1 + x));
        }
    },
    VERTICAL_LINE {
        void setValue(Matrix out, double val, int x, int y) {
            out.set(val, out.getWidth() - (1 + x), y);
        }
    },
    HORIZONTAL_LINE {
        void setValue(Matrix out, double val, int x, int y) {
            out.set(val, x, out.getHeight() - (1 + y));
        }
    };

    public static TranspositionType valueOf(int ordinal) {
        if (TranspositionType.MAIN_DIAGONAL.ordinal() == ordinal) {
            return TranspositionType.MAIN_DIAGONAL;
        }
        if (TranspositionType.SIDE_DIAGONAL.ordinal() == ordinal) {
            return TranspositionType.SIDE_DIAGONAL;
        }
        if (TranspositionType.VERTICAL_LINE.ordinal() == ordinal) {
            return TranspositionType.VERTICAL_LINE;
        }
        if (TranspositionType.HORIZONTAL_LINE.ordinal() == ordinal) {
            return TranspositionType.HORIZONTAL_LINE;
        }
        throw new IllegalArgumentException("Unknown type: " + ordinal);
    }

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
