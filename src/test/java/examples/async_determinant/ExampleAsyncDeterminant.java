package examples.async_determinant;

import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class ExampleAsyncDeterminant {
    private static Random RNG;

    @Before
    public void createNewRng() {
        RNG = new Random(123456789);
    }

    /**
     * avg time 360ms
     */
    @Test(timeout = 1500)
    public void calcDeterminant() {
        AsynchronousDeterminantCalculationMatrix example = AsynchronousDeterminantCalculationMatrix.of(10, RNG);
        assertTrue(example.isCalculatingDeterminant());
        assertEquals(-0.008249788786467094, example.getDeterminant(), 0.000000000000000000000001);
        assertFalse(example.isCalculatingDeterminant());
    }
}
