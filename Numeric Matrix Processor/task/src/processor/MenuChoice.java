package processor;

public enum MenuChoice {
    EXIT,
    ADD,
    MULTIPLY_CONSTANT,
    MULTIPLY_MATRIX,
    TRANSPOSE,
    DETERMINANT,
    INVERSE_MATRIX;

    public static MenuChoice valueOf(int ordinal) {
        for (MenuChoice value : MenuChoice.values()) {
            if (value.ordinal() == ordinal) {
                return value;
            }
        }
        throw new IllegalArgumentException("Unknown type: " + ordinal);
    }

}
