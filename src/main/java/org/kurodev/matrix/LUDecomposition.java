package org.kurodev.matrix;

public record LUDecomposition(Matrix l, Matrix u, int[] pivots) {
    public static LUDecomposition fromMatrix(Matrix matrix) {
        int n = matrix.getWidth();

        // Create LU decomposition matrix
        double[][] lu = new double[n][n];
        for (int i = 0; i < n; i++) {
            System.arraycopy(matrix.getRow(i), 0, lu[i], 0, n);
        }

        // Create pivot array
        int[] pivots = new int[n];
        for (int i = 0; i < n; i++) {
            pivots[i] = i;
        }

        // Perform LU decomposition
        for (int k = 0; k < n - 1; k++) {
            int p = k;
            double max = Math.abs(lu[k][k]);
            for (int i = k + 1; i < n; i++) {
                double abs = Math.abs(lu[i][k]);
                if (abs > max) {
                    p = i;
                    max = abs;
                }
            }
            if (p != k) {
                double[] temp = lu[p];
                lu[p] = lu[k];
                lu[k] = temp;

                int t = pivots[p];
                pivots[p] = pivots[k];
                pivots[k] = t;
            }

            for (int i = k + 1; i < n; i++) {
                double factor = lu[i][k] / lu[k][k];
                lu[i][k] = factor;
                for (int j = k + 1; j < n; j++) {
                    lu[i][j] -= factor * lu[k][j];
                }
            }
        }

        // Create L and U matrices
        double[][] l = new double[n][n];
        double[][] u = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i > j) {
                    l[i][j] = lu[i][j];
                    u[i][j] = 0;
                } else if (i == j) {
                    l[i][j] = 1;
                    u[i][j] = lu[i][j];
                } else {
                    l[i][j] = 0;
                    u[i][j] = lu[i][j];
                }
            }
        }

        return new LUDecomposition(Matrix.of(l), Matrix.of(u), pivots);
    }
}

