package tests;

import org.junit.Before;
import org.junit.Test;
import org.kurodev.matrix.Matrix;

import java.util.Random;

public class LUDecompositionTest {
    private static final double DELTA = 0.000000000000000001d;
    private static Random RNG;

    @Before
    public void createNewRng() {
        RNG = new Random(582375269);
    }

    @Test
    public void testLuCreation() {
        double[][] input = {
                {2d, 1d, 4d,},
                {4d, 4d, 2d,},
                {4d, 6d, 7d,}
        };
        Matrix a = Matrix.of(input);
        System.out.println(a.calculateLUDecomposition());
    }

}
