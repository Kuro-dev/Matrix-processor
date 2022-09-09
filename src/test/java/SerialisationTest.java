import org.junit.Test;
import org.kurodev.matrix.Matrix;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

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

        byte[] data = {0, 0, 0, 2, 0, 0, 0, 2, 0x3F,
                (byte) 0xF0, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
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
}
