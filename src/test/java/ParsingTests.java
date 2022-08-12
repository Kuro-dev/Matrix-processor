import org.junit.Test;
import org.kurodev.matrix.Matrix;

import java.util.Random;

import static org.junit.Assert.assertEquals;

public class ParsingTests {
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
                1 1.2 1.6 5 6
                """;
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

    @Test
    public void anyMatrixShouldWorkFromString() {
        Random random = new Random();
        for (int i = 0; i < 1000; i++) {
            Matrix m1 = Matrix.of(random, 20, 20);
            Matrix m2 = Matrix.of(m1.toString());
            assertEquals(m1, m2);
        }
    }
}
