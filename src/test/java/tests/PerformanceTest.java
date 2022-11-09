package tests;

import org.junit.Before;
import org.junit.Test;
import org.kurodev.matrix.Matrix;

import java.util.Random;

import static org.junit.Assert.*;

public class PerformanceTest {
    private static Random RNG;

    @Before
    public void createNewRng() {
        RNG = new Random(123456789);
    }

    /**
     * <pre>
     * 10x10 matrix takes ~  200-400ms to calculate determinant
     * 11x11 matrix takes ~ 4500ms to calculate determinant
     * 12x12 matrix takes ~50600ms to calculate determinant
     * </pre>
     */
    @Test(timeout = 1000)
    public void calculateBigMatrixDeterminant() {
        Matrix rand = Matrix.of(10, 10, RNG);
        assertEquals(-0.00824978878646706d, rand.getDeterminant(), 0.000000000000000000000001d);
        assertEquals(-8.44778371734227d, rand.multiply(2).getDeterminant(), 0.000000000000000000000001d);
    }

    @Test
    public void calculateScalarDeterminant4x4() {
        Matrix matrix = Matrix.of(4, RNG);
        for (int scalar = 2; scalar < 3; scalar++) {
            Matrix multiplied = matrix.multiply(scalar);
            assertNotEquals(matrix, multiplied);
            var assumedDeterminant = Math.pow(scalar, matrix.getWidth()) * matrix.getDeterminant();
            assertEquals(assumedDeterminant, multiplied.getDeterminant(), 0.000000000000000000001d);
        }
    }


    /**
     * Given that A is an n×n matrix and given a scalar α
     * det(α*A)=α^n * det(A)
     */
    @Test
    public void calculateScalarDeterminant3x3() {
        double[][] data = {
                {2d, 3d, 4d},
                {5d, 6d, 7d},
                {8d, 9d, 1d}
        };
        Matrix matrix = Matrix.of(data);
        final double scalar = 2d;
        Matrix multiplied = matrix.multiply(scalar);
        assertNotEquals(matrix, multiplied);
        assertEquals(27d, matrix.getDeterminant(), 0);
        var assumedDeterminant = Math.pow(scalar, matrix.getWidth()) * matrix.getDeterminant();

        assertEquals(assumedDeterminant, multiplied.getDeterminant(), 0.000000000000000001d);
    }

    @Test
    public void calculateScalarDeterminantReverse3x3() {
        double[][] data = {
                {4d, 6d, 8d},
                {10d, 12d, 14d},
                {16d, 18d, 2d}
        };
        Matrix matrix = Matrix.of(data);
        final double scalar = 0.5;
        Matrix multiplied = matrix.multiply(scalar);
        assertNotEquals(matrix, multiplied);
        assertEquals(216d, matrix.getDeterminant(), 0);
        var assumedDeterminant = Math.pow(scalar, matrix.getWidth()) * matrix.getDeterminant();

        assertEquals(assumedDeterminant, multiplied.getDeterminant(), 0.000000000000000001d);
    }

    @Test
    public void calculateScalarDeterminant2x2() {
        Matrix matrix = Matrix.of(2, RNG);
        Matrix multiplied;
        final int scalar = 2;
        for (int i = 0; i < 30; i++) {
            multiplied = matrix.multiply(scalar);
            assertNotEquals(matrix, multiplied);
            var assumedDeterminant = Math.pow(scalar, matrix.getWidth()) * matrix.getDeterminant();
            assertEquals(assumedDeterminant, multiplied.getDeterminant(), 0.000000000000000000001d);
        }
    }

    /**
     * avg time: 2ms
     */
    @Test(timeout = 100)
    public void performanceOfAddition() {
        Matrix rand = Matrix.of(15, RNG);
        Matrix rand2 = Matrix.of(15, RNG);
        var res = rand.add(rand2);
        assertFalse(res.toString(), res.isError());
    }

    /**
     * avg time: 2ms
     */
    @Test(timeout = 100)
    public void performanceOfSubtraction() {
        Matrix rand = Matrix.of(15, RNG);
        Matrix rand2 = Matrix.of(15, RNG);
        var res = rand.subtract(rand2);
        assertFalse(res.toString(), res.isError());
    }

    /**
     * avg time: 3ms
     */
    @Test(timeout = 50)
    public void performanceOfMultiplication() {
        Matrix rand = Matrix.of(15, 20, RNG);
        Matrix rand2 = Matrix.of(20, 15, RNG);
        var res = rand.multiply(rand2);
        assertFalse(res.toString(), res.isError());
    }

    /**
     * avg time: 1-2ms
     */
    @Test(timeout = 50)
    public void performanceOfMinorCalculation() {
        Matrix rand = Matrix.of(20, RNG);
        var res = rand.getMinor(10, 12);
        assertFalse(res.toString(), res.isError());
    }

    /**
     * avg time: 1ms
     */
    @Test(timeout = 50)
    public void performanceOfTransposition() {
        Matrix rand = Matrix.of(20, RNG);
        var res = rand.transpose();
        assertFalse(res.toString(), res.isError());
    }

    /**
     * avg time: 2000ms
     */
    @Test(timeout = 3000)
    public void performanceOfInversion() {
        Matrix rand = Matrix.of(10, RNG);
        var res = rand.inverse();
        assertFalse(res.toString(), res.isError());
    }

    /**
     * avg time: 6ms
     */
    @Test(timeout = 30)
    public void performanceOfByteSerialization() {
        Matrix rand = Matrix.of(30, RNG);
        var res = rand.toByteArray();
        assertEquals(rand, Matrix.of(res));
    }

    /**
     * avg time: 50ms
     */
    @Test(timeout = 100)
    public void performanceOfStringSerialization() {
        Matrix rand = Matrix.of(30, RNG);
        var res = rand.toString();
        assertEquals(rand, Matrix.of(res));
    }

}
