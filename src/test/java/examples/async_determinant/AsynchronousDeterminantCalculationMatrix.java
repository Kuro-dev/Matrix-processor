package examples.async_determinant;

import org.kurodev.matrix.Matrix;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class AsynchronousDeterminantCalculationMatrix extends Matrix {
    private final CompletableFuture<Double> detRequest = new CompletableFuture<>();

    protected AsynchronousDeterminantCalculationMatrix(int width, int height, double[][] matrix) {
        super(width, height, matrix);
        detRequest.completeAsync(super::getDeterminant);
    }

    //just a constructor method for testing purposes
    public static AsynchronousDeterminantCalculationMatrix of(int n, Random rng) {
        double[][] dataSet = new double[n][n];
        for (int y = 0; y < n; y++) {
            for (int x = 0; x < n; x++) {
                dataSet[y][x] = rng.nextDouble();
            }
        }
        return new AsynchronousDeterminantCalculationMatrix(n, n, dataSet);
    }

    //optional state checking to see if the determinant has been calculated already
    public boolean isCalculatingDeterminant() {
        return !detRequest.isDone();
    }

    @Override
    public double getDeterminant() {
        try {
            return detRequest.get();
        } catch (InterruptedException | ExecutionException e) {
            //should never happen theoretically
            throw new RuntimeException("Thread got interrupted");
        }
    }

    //if necessary override this to create your own matrix copies with your desired derived class
    //in this example not necessary

    /**
     * @see Matrix#copy(boolean)
     */
    @Override
    public Matrix copy(boolean includeValues) {
        return super.copy(includeValues);
    }
}
