package tests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.kurodev.matrix.Matrix;

import java.util.Arrays;
import java.util.Random;

public class LUDecompositionTest {
    private static final double DELTA = 0.000000000000000001d;
    private static Random RNG;

    @Before
    public void createNewRng() {
        RNG = new Random(582375269);
    }

    @Test
    public void createLUDecomposition() {
        double[][] input = {
                {2d, 1d, 4d,},
                {4d, 4d, 2d,},
                {4d, 6d, 7d,}
        };
        Matrix inputMatrix = Matrix.of(input);
        double[][] expectedU = {
                {2d, 1d, 4d},
                {0d, 2d, -6d},
                {0d, 0d, 5d},
        };
        double[][] expectedL = {
                {1d, 0d, 0d},
                {2d, 1d, 0d},
                {2d, 3d / 2d, 1d},
        };
        Matrix expectedUMatrix = Matrix.of(expectedU);
        Matrix expectedLMatrix = Matrix.of(expectedL);
        Assert.assertEquals(expectedLMatrix, inputMatrix.calculateLUDecomposition().l());
        Assert.assertEquals(expectedUMatrix, inputMatrix.calculateLUDecomposition().u());
    }

}
