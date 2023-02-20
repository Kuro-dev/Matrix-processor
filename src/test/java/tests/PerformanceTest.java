package tests;

import org.junit.Before;
import org.junit.Test;
import org.kurodev.matrix.Matrix;

import java.lang.reflect.Field;
import java.util.Random;

import static org.junit.Assert.*;

public class PerformanceTest {
    private static final double DELTA = 0.000000000000000001d;
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
        assertEquals(-0.008249788786467078, rand.getDeterminant(), DELTA);
        assertEquals(-8.447783717342288, rand.multiply(2).getDeterminant(), DELTA);
    }

    @Test
    public void calculateScalarDeterminant4x4() {
        Matrix matrix = Matrix.of(4, RNG);
        for (int scalar = 2; scalar < 3; scalar++) {
            Matrix multiplied = matrix.multiply(scalar);
            assertNotEquals(matrix, multiplied);
            var assumedDeterminant = Math.pow(scalar, matrix.getWidth()) * matrix.getDeterminant();
            assertEquals(assumedDeterminant, multiplied.getDeterminant(), DELTA);
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

        assertEquals(assumedDeterminant, multiplied.getDeterminant(), DELTA);
    }

    @Test
    public void calculateDeterminantInMatrixCalculation() throws NoSuchFieldException, IllegalAccessException {
        Matrix a = Matrix.of(4, 4, RNG);
        Matrix b = Matrix.of(4, 4, RNG);
        assertFalse(Double.isNaN(a.getDeterminant()));
        assertFalse(Double.isNaN(b.getDeterminant()));
        assertEquals(0.006853144421231317, a.getDeterminant(), DELTA);
        assertEquals(-0.019656427259345577, b.getDeterminant(), DELTA);
        double assumedDeterminant = a.getDeterminant() * b.getDeterminant();


        var result = a.multiply(b);

        assertEquals(assumedDeterminant, result.getDeterminant(), 0);

        //remove precomputed determinant from object
        Field f = a.getClass().getDeclaredField("determinant");
        f.setAccessible(true);
        f.set(result, null);
        f.setAccessible(false);
        double calculatedDeterminant = result.getDeterminant();

        double precisionTrue = 1.0E-17;
        double precisionFalse = 1.0E-18;
        //small rounding issue, due to double bit limitations and rounding.
        assertEquals(assumedDeterminant, calculatedDeterminant, precisionTrue);
        assertNotEquals(assumedDeterminant, calculatedDeterminant, precisionFalse);

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

        assertEquals(assumedDeterminant, multiplied.getDeterminant(), DELTA);
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
            assertEquals(assumedDeterminant, multiplied.getDeterminant(), DELTA);
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
