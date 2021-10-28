import org.junit.Test;
import processor.Matrix;
import processor.TranspositionType;

import static org.junit.Assert.assertEquals;

public class MatrixConsistencyTest {

    @Test
    public void checkTranspositionMain() {
        double[][] data = {
                {1, 2, 3, 4, 5},
                {1, 2, 3, 4, 5},
                {1, 2, 3, 4, 5},
                {1, 2, 3, 4, 5},
                {1, 2, 3, 4, 5},
        };
        double[][] expected = {
                {1, 1, 1, 1, 1},
                {2, 2, 2, 2, 2},
                {3, 3, 3, 3, 3},
                {4, 4, 4, 4, 4},
                {5, 5, 5, 5, 5},
        };
        var expectedMatrix = Matrix.of(expected);
        Matrix m = Matrix.of(data);
        var result = m.transpose(TranspositionType.MAIN_DIAGONAL);
        assertEquals(expectedMatrix, result);
    }

    @Test
    public void checkTranspositionSide() {
        double[][] data = {
                {1, 2, 3, 4, 5},
                {1, 2, 3, 4, 5},
                {1, 2, 3, 4, 5},
                {1, 2, 3, 4, 5},
                {1, 2, 3, 4, 5},
        };
        double[][] expected = {
                {5, 5, 5, 5, 5},
                {4, 4, 4, 4, 4},
                {3, 3, 3, 3, 3},
                {2, 2, 2, 2, 2},
                {1, 1, 1, 1, 1},
        };
        var expectedMatrix = Matrix.of(expected);
        Matrix m = Matrix.of(data);
        var result = m.transpose(TranspositionType.SIDE_DIAGONAL);
        assertEquals(expectedMatrix, result);
    }

    @Test
    public void checkTranspositionHorizontal() {
        double[][] data = {
                {10, 2, 3, 4, 5},
                {1, 20, 3, 4, 5},
                {1, 2, 30, 4, 5},
                {1, 2, 3, 40, 5},
                {1, 2, 3, 4, 50},
        };
        double[][] expected = {
                {1, 2, 3, 4, 50},
                {1, 2, 3, 40, 5},
                {1, 2, 30, 4, 5},
                {1, 20, 3, 4, 5},
                {10, 2, 3, 4, 5},
        };
        var expectedMatrix = Matrix.of(expected);
        Matrix m = Matrix.of(data);
        var result = m.transpose(TranspositionType.HORIZONTAL_LINE);
        assertEquals(expectedMatrix, result);
    }

    @Test
    public void checkTranspositionVertical() {
        double[][] data = {
                {1, 2, 3, 4, 5},
                {1, 2, 3, 4, 5},
                {1, 2, 3, 4, 5},
                {1, 2, 3, 4, 5},
                {1, 2, 3, 4, 5},
        };
        double[][] expected = {
                {5, 4, 3, 2, 1},
                {5, 4, 3, 2, 1},
                {5, 4, 3, 2, 1},
                {5, 4, 3, 2, 1},
                {5, 4, 3, 2, 1},
        };
        var expectedMatrix = Matrix.of(expected);
        Matrix m = Matrix.of(data);
        var result = m.transpose(TranspositionType.VERTICAL_LINE);
        assertEquals(expectedMatrix, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidTranspositionShouldThrowIllegalArgEx() {
        double[][] input = {
                {1, 2, 3, 4, 5},
                {1, 2, 3, 4, 5}};
        Matrix m = Matrix.of(input);
        m.transpose(TranspositionType.MAIN_DIAGONAL);
    }

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
        var result = m1.multiply(m2);
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
    public void checkDeserializationAndSerialization() {
        double[][] data = {
                {10, 2, 3, 4, 5},
                {1, 20, 3, 4, 5},
                {1, 2, 30, 4, 5},
                {1, 2, 3, 40, 5},
                {1, 2, 3, 4, 50},
        };
        var original = Matrix.of(data);
        var copy = Matrix.of(original.toString());
        assertEquals(original, copy);
        assertEquals(original, original.copy(true));
    }

    @Test(expected = IllegalArgumentException.class)
    public void parsingFaultyStringShouldThrowIllArgEx() {
        var data = """
                1 2 3 4
                1 2 3 4
                1 1.2 1.6 5 6""";
        Matrix.of(data);
    }

    @Test
    public void parsingCorrectStringShouldResultInCorrectMatrix() {
        double[][] dataExpected = {
                {1, 2, 3, 4},
                {1, 2, 3, 4},
                {1, 1.2, 1.6, 5}
        };
        var expected = Matrix.of(dataExpected);
        var data = """
                1 2 3 4
                1 2 3 4
                1 1.2 1.6 5
                """;
        var result = Matrix.of(data);

        assertEquals(expected, result);
    }
}
