package tests;

import org.junit.Test;
import org.kurodev.matrix.Matrix;

import static org.junit.Assert.*;

public class MatrixIsRealTest {

    @Test
    public void matrixShouldBeReal() {
        double[][] matrixData = {
                {1, 7, 3},
                {7, 4, 5},
                {3, 5, 0},
        };
        Matrix m1 = Matrix.of(matrixData);
        assertTrue(m1.isReal());
    }
    @Test
    public void matrixShouldNotBeReal() {
        double[][] matrixData = {
                {1, 7, 3},
                {7, -4, 5},
                {3, 5, 0},
        };
        Matrix m1 = Matrix.of(matrixData);
        assertFalse(m1.isReal());
    }
}
