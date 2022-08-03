import org.junit.Test;
import processor.Matrix;

import static org.junit.Assert.assertEquals;

public class MatrixCalculationTest {
    @Test(expected = IllegalArgumentException.class)
    public void multiplicationOfDifferentSizeMatrizesShouldThrowIllArgEx() {
        double[][] input1 = {
                {1, 2, 3, 4, 5},
                {1, 2, 3, 4, 5}};
        Matrix m1 = Matrix.of(input1);
        double[][] input2 = {
                {1, 2, 3, 4, 5},
                {1, 2, 3, 4, 5}};
        Matrix m2 = Matrix.of(input2);
        m1.multiply(m2); //here exception
    }

    @Test
    public void multiplicationOfMatrixWithMultiplicantTest() {
        double[][] input1 = {
                {1, 2, 3, 4, 5},
                {1, 2, 3, 4, 5}};
        Matrix m1 = Matrix.of(input1);
        var result = m1.multiply(3);
        double[][] expectedData = {
                {3, 6, 9, 12, 15},
                {3, 6, 9, 12, 15},};
        Matrix expected = Matrix.of(expectedData);
        assertEquals(expected, result);
    }

    @Test
    public void multiplicationOfValidMatrizesTest() {
        double[][] input1 = {
                {1, 2, 3, 4, 5},
                {1, 2, 3, 4, 5}};
        Matrix m1 = Matrix.of(input1);
        double[][] input2 = {
                {1, 2},
                {3, 4},
                {5, 5},
                {4, 3},
                {2, 1}
        };
        double[][] expectedData = {
                {48, 42},
                {48, 42},
        };
        var mExpected = Matrix.of(expectedData);
        var m2 = Matrix.of(input2);
        var result = m1.multiply(m2);
        assertEquals(mExpected, result);
    }

    @Test
    public void subtractionTest() {
        double[][] input1 = {
                {1, 2, 3, 4, 5},
                {1, 2, 3, 4, 5}
        };
        double[][] input2 = {
                {2, 3, 3, 5, 6},
                {3, 4, 3, 6, 7}
        };
        double[][] expectedData = {
                {-1, -1, 0, -1, -1},
                {-2, -2, 0, -2, -2}
        };
        Matrix m1 = Matrix.of(input1);
        var mExpected = Matrix.of(expectedData);
        var m2 = Matrix.of(input2);
        var result = m1.subtract(m2);
        assertEquals(mExpected, result);
    }

    @Test
    public void inverseOf2By2GivesCorrectResult() {
        double[][] input1 = {
                {4, 7},
                {2, 6}
        };
        double[][] expectedData = {
                {0.6, -0.7},
                {-0.2, 0.4}
        };
        double[][] identityMatrix = {
                {1, 0},
                {0, 1}
        };
        Matrix m1 = Matrix.of(input1);
        assertEquals(10.0d, m1.getDeterminant(), 0.0);
        var mExpected = Matrix.of(expectedData);
        var m2 = m1.inverse();
        assertEquals(mExpected, m2);
        var identity = Matrix.of(identityMatrix);
        assertEquals(identity, m1.multiply(m1.inverse()));
    }

    @Test
    public void inverseOf3By3GivesCorrectResult() {
        double[][] input1 = {
                {1, 2, -1},
                {2, 1, 2},
                {-1, 2, 1}
        };
        double[][] expectedData = {
                {3d / 16d, 1d / 4d, -5d / 16d},
                {1d / 4d, 0, 1d / 4d},
                {-5d / 16, 1d / 4d, 3d / 16d}
        };
        Matrix m1 = Matrix.of(input1);
        var mExpected = Matrix.of(expectedData);
        var m2 = m1.inverse();
        assertEquals(mExpected, m2);
    }

    @Test
    public void matrixAdditionTest() {
        double[][] input1 = {
                {4, 7},
                {2, 6}
        };
        double[][] input2 = {
                {1, 7},
                {-3, 1}
        };
        double[][] expectedData = {
                {5, 14},
                {-1, 7}
        };
        Matrix m1 = Matrix.of(input1);
        Matrix m2 = Matrix.of(input2);
        Matrix mExpected = Matrix.of(expectedData);
        Matrix mResult = m1.add(m2);
        assertEquals(mExpected, mResult);
    }

}
