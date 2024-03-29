# This project is fairly simple.

Create a matrix object, fill it with data, either through a string of space-separated data or through a 2 dimensional
array of double values.

you can also leave it blank and simply specify the target size. 
There are tests written for every function in the Matrix object. 
If you encounter bugs or spot potential issues, give me an example case, and I shall add it as a test and resolve it as
soon as I get to it.

# Example
```java
import org.kurodev.matrix.Matrix;

public class Example {
    public void createMatrixFromArray() {
        double[][] data = {
                {10, 2, 3, 4, 5},
                {1, 20, 3, 4, 5},
                {1, 2, 30, 4, 5},
                {1, 2, 3, 40, 5},
                {1, 2, 3, 4, 50},
        };
        Matrix m = Matrix.of(data);
    }

    //values with decimal point are also valid. 
    // (though arguably less readable as a string)
    public void createMatrixFromString() {
        var data = """
                1 2 3 4
                1 2 3 4
                1 1.2 1.6 5 6
                """;
        Matrix m = Matrix.of(data);
    }

    public void createMatrixWithoutData() {
        int width = 3, height = 4;
        Matrix obj = Matrix.of(width, height);
    }
}

```
By default, Matrices are immutable.
if necessary another class may Inherit the Matrix class and use
the protected access Matrix#set method to change the contents of the matrix.

Any Matrix method that returns a Matrix will return a new copy with its own separate dataset. 
Therefore, multiple calculations on the same Matrix object will not corrupt its data in any way.
(unless of course one uses a derived class that invokes the Matrix#set method)
