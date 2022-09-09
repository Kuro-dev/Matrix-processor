import org.junit.Test;
import org.kurodev.matrix.Matrix;

import static org.junit.Assert.*;

public class SerialisationTest {

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
    }
}
