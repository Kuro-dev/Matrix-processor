package org.kurodev.matrix;

import java.util.Arrays;

public record LUDecomposition(Matrix l, Matrix u, int[] pivots, int parity) {

    @Override
    public String toString() {
        return "LUDecomposition{" +
                "\n l=\n" + l +
                "\n u=\n" + u +
                "\n pivots=" + Arrays.toString(pivots) +
                "\n parity=" + parity +
                '}';
    }
}

