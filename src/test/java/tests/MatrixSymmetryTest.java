package tests;

import org.junit.Test;
import org.kurodev.matrix.Matrix;

import static org.junit.Assert.*;

public class MatrixSymmetryTest {

    @Test
    public void matrixShouldBeSymmetric() {
        double[][] matrixData = {
                {1, 7, 3},
                {7, 4, 5},
                {3, 5, 0},
        };
        Matrix m1 = Matrix.of(matrixData);
        assertEquals(m1, m1.transpose());
        assertTrue(m1.isSymmetric());
    }

    @Test
    public void bigMatrixShouldAlsoBeSymmetric() {
        double[][] matrixData = {
                {1, 7, 3, 9, 8},
                {7, 2, 6, 5, 9},
                {3, 6, 3, 2, 3},
                {9, 5, 2, 4, 7},
                {8, 9, 3, 7, 5},
        };
        Matrix m1 = Matrix.of(matrixData);
        assertEquals(m1, m1.transpose());
        assertTrue(m1.isSymmetric());
    }

    @Test
    public void inverseOfMatrixShouldBeSymmetricTooIfExists() {
        double[][] matrixData = {
                {1, 7, 3, 9, 8},
                {7, 2, 6, 5, 9},
                {3, 6, 3, 2, 3},
                {9, 5, 2, 4, 7},
                {8, 9, 3, 7, 5},
        };
        Matrix invertedM1 = Matrix.of(matrixData).inverse();
        assertEquals(invertedM1, invertedM1.transpose());
        assertTrue(invertedM1.isSymmetric());
    }

    @Test
    public void matrixShouldNotBeSymmetric() {
        double[][] data = {
                {1, 7, 5},
                {7, 4, 5},
                {3, 5, 0},
        };
        Matrix m1 = Matrix.of(data);
        assertNotEquals(m1, m1.transpose());
        assertFalse(m1.isSymmetric());
    }

    @Test
    public void invalidMatrixShouldNotBeSymmetric() {
        double[][] data = {
                {1, 7},
                {7, 4},
                {3, 5},
        };
        Matrix m1 = Matrix.of(data);
        assertFalse(m1.isSymmetric());
    }
}
