package tests;

import org.junit.Before;
import org.junit.Test;
import org.kurodev.matrix.Matrix;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

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
        assertEquals(-0.00824978878646706d, rand.getDeterminant(), 0.000000000000000000000001);
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
