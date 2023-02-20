package org.kurodev.matrix;

public record LUDecomposition(Matrix l, Matrix u, int[] pivots, int parity) {

}

