package tests;

import org.junit.Assert;
import org.junit.Test;
import org.kurodev.matrix.LUDecomposition;
import org.kurodev.matrix.Matrix;

public class LUDecompositionTest {

    @Test
    public void createLUDecomposition() {
        double[][] input = {
                {1d, 2d, 0d,},
                {3d, 6d, -1d,},
                {1d, 2d, 1d,}
        };
        Matrix inputMatrix = Matrix.of(input);
        Matrix expectedL = Matrix.of("""
                1 0 0
                3 1 -0
                1 -1 1
                """);
        Matrix expectedU = Matrix.of("""
                1 2 0
                0 0 -1
                0 0 0
                """);
        Assert.assertEquals("The test data should line up", inputMatrix, expectedU.multiply(expectedL));

        int[] expectedPivots = {2, 2, 3};
        LUDecomposition lu = inputMatrix.calculateLUDecomposition();
        Assert.assertArrayEquals(expectedPivots, lu.pivots());
        Assert.assertEquals(expectedU, lu.u());
        Assert.assertEquals(expectedL, lu.l());
        Assert.assertEquals(inputMatrix, lu.l().multiply(lu.u()));
    }

    @Test
    public void createLUDecomposition2() {
        double[][] input = {
                {2d, 1d, 4d,},
                {4d, 4d, 2d,},
                {4d, 6d, 7d,}
        };
        Matrix inputMatrix = Matrix.of(input);
        Matrix expectedL = Matrix.of("""
                1.000000 0.000000 0.000000
                2.000000 1.000000 0.000000
                2.000000 2.000000 1.000000
                """);
        Matrix expectedU = Matrix.of("""
                4.000000 1.000000 4.000000
                0.000000 2.000000 -6.000000
                0.000000 0.000000 5.000000
                """);
        //TODO: Figure out why this fails: Is the Test faulty or the implementation?
        Assert.assertEquals("The test data should line up", inputMatrix, expectedU.multiply(expectedL));

        int[] expectedPivots = {2, 2, 3};
        LUDecomposition lu = inputMatrix.calculateLUDecomposition();
        Assert.assertArrayEquals(expectedPivots, lu.pivots());
        Assert.assertEquals(expectedU, lu.u());
        Assert.assertEquals(expectedL, lu.l());
        Assert.assertEquals(inputMatrix, lu.l().multiply(lu.u()));
    }
}
