import org.junit.Test;
import processor.Matrix;
import processor.TranspositionType;

import static org.junit.Assert.assertEquals;

public class TranspositionTest {

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

}
