import org.junit.Before;
import org.junit.Test;
import org.kurodev.matrix.Matrix;

import java.util.Random;

public class PerformanceTest {
    private static Random RNG;
@Before
public void createNewRng(){
    RNG = new Random(123456789);
}
    /**
     * <pre>
     * 10x10 matrix takes ~  400ms to calculate determinant
     * 11x11 matrix takes ~ 4500ms to calculate determinant
     * 12x12 matrix takes ~50600ms to calculate determinant
     * </pre>
     */
    @Test
    public void calculateBigMatrixDeterminant() {
        Matrix rand = Matrix.of(10, 10, RNG);
        System.out.println(rand.getDeterminant());
    }

}
