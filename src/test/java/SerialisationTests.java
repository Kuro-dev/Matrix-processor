import org.junit.Test;
import org.kurodev.matrix.Matrix;

import java.util.Random;

import static org.junit.Assert.*;

public class SerialisationTests {
    public static final Random RANDOM = new Random();

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
        for (int i = 0; i < 1000; i++) {
            Matrix m1 = Matrix.of(5, 5, RANDOM);
            Matrix m2 = Matrix.of(m1.toString());
            assertEquals(m1, m2);
        }
    }

    @Test
    public void matrixToArrayTest() {
        var data = """
                1 2
                3 4
                """;
        var m1 = Matrix.of(data);
        double[] expected = {1d, 2d, 3d, 4d};
        assertArrayEquals(expected, m1.toArray(), 0.000001);
    }

    @Test
    public void anyMatrixShouldWorkFromByteArray() {
        for (int i = 0; i < 1000; i++) {
            Matrix m1 = Matrix.of(5, 5, RANDOM);
            Matrix m2 = Matrix.of(m1.toByteArray());
            assertEquals(m1, m2);
        }
    }

    @Test
    public void testMatrixToByteArray() {
        String data = """
                1 1
                2 2
                """;
        byte[] expected = {0, 0, 0, 2, 0, 0, 0, 2, 0x3F,
                (byte) 0xF0, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x3F, (byte) 0xF0, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x40, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x40, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        Matrix m = Matrix.of(data);
        assertArrayEquals(expected, m.toByteArray());
    }

    @Test
    public void testMatrixFromByteArray() {
        byte[] data = {0, 0, 0, 2, 0, 0, 0, 2,
                0x3F, (byte) 0xF0, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x3F, (byte) 0xF0, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x40, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x40, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        String expected = """
                1 1
                2 2
                """;
        Matrix m = Matrix.of(data);
        Matrix mExpected = Matrix.of(expected);
        assertEquals(mExpected, m);
    }

    @Test
    public void testMatrixFromInvalidByteArrayResultsInErrorMatrix() {

        byte[] data = {0, 0, 0, 2, 0, 0, 0, 2,
                0x3F, (byte) 0xF0, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x3F, (byte) 0xF0, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x40, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x40, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        Matrix m = Matrix.of(data);
        assertTrue(m.isError());
    }

    @Test
    public void testMatrixSerialisingAndDeserializing() {
        String data = """
                1.32451 1.314512
                2.56437 23.52531
                """;
        Matrix m = Matrix.of(data);
        assertEquals(m, Matrix.of(m.toByteArray()));
    }}
