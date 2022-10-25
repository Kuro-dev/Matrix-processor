package org.kurodev.matrix;

/**
 * Lu Decomposition
 */
public record LuDecomp(Matrix upper, Matrix lower) {
    @Override
    public String toString() {
        String delim = "\t".repeat(3);
        StringBuilder sb = new StringBuilder();
        sb.append("Upper").append(delim).append("lower").append(System.lineSeparator());
        for (int n = 0; n < upper.getWidth(); n++) {
            var upperRow = upper.getRow(n);
            var lowerRow = lower.getRow(n);
            sb.append(printRow(upperRow)).append(delim).append(printRow(lowerRow));
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }

    private String printRow(double[] row) {
        StringBuilder sb = new StringBuilder();
        String delim = " ";
        for (double x : row) {
            sb.append(x).append(delim);
        }
        return sb.toString().trim();
    }
}
