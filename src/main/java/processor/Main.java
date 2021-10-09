package processor;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    private static final String RESULT_STRING = "The result is:";
    private static final String menu;

    static {
        StringBuilder builder = new StringBuilder();
        for (MenuChoice value : MenuChoice.values()) {
            builder.append(String.format("%d. %s%n", value.ordinal(), value.name().replaceAll("_", " ").toLowerCase()));
        }
        menu = builder.toString();
    }

    public static void main(String[] args) throws IOException {
        var scanner = new Scanner(System.in);
        System.out.println("Available options:\n" + menu);
        while (scanner.hasNext()) {
            int opt = scanner.nextInt();
            MenuChoice choice = MenuChoice.valueOf(opt);
            System.out.printf("your choice: %d(%s)%n", opt, choice.name());
            switch (choice) {
                case ADD:
                    addMatrix(scanner);
                    break;
                case MULTIPLY_CONSTANT:
                    multiplyByConstant(scanner);
                    break;
                case MULTIPLY_MATRIX:
                    multiplyByMatrix(scanner);
                    break;
                case TRANSPOSE:
                    showTransposeMenu(scanner);
                    break;
                case DETERMINANT:
                    calcDeterminant(scanner);
                    break;
                case INVERSE_MATRIX:
                    inverseMatrix(scanner);
                    break;
                case EXIT:
                    System.exit(0);
            }
        }

    }

    private static void inverseMatrix(Scanner in) {
        Matrix a = readMatrix(in);
        Matrix b = a.inverse();
        if (b.isError()) {
            System.out.println("This matrix doesn't have an inverse.");
        } else {
            System.out.println(b.toString(3));
        }
    }

    private static void calcDeterminant(Scanner scanner) {
        Matrix a = readMatrix(scanner);
        System.out.println(a.getDeterminant());
    }

    private static void showTransposeMenu(Scanner in) {
        System.out.print("1. Main diagonal\n" +
                "2. Side diagonal\n" +
                "3. Vertical line\n" +
                "4. Horizontal line\n" +
                "0. Exit\n");
        int choice = in.nextInt();
        if (choice > 0) {
            var matrix = readMatrix(in);
            var type = TranspositionType.valueOf(choice - 1);
            Matrix result = matrix.transpose(type);
            System.out.println(RESULT_STRING);
            System.out.println(result);
        }
    }

    private static void multiplyByMatrix(Scanner scanner) {
        Matrix a = readMatrix(scanner);
        Matrix b = readMatrix(scanner);
        Matrix c = a.multiply(b);
        System.out.println(RESULT_STRING);
        System.out.println(c);
    }

    private static void multiplyByConstant(Scanner scanner) {
        Matrix a = readMatrix(scanner);
        System.out.println("Enter Constant");
        double factor = scanner.nextInt();
        Matrix result = a.multiply(factor);
        System.out.println(RESULT_STRING);
        if (!result.isError())
            System.out.println(result);
        else
            System.out.println("ERROR");
    }

    private static void addMatrix(Scanner scanner) {
        Matrix a = readMatrix(scanner);
        Matrix b = readMatrix(scanner);
        Matrix c = a.add(b);
        System.out.println(RESULT_STRING);
        if (!c.isError())
            System.out.println(c);
        else
            System.out.println("ERROR");
    }

    private static Matrix readMatrix(Scanner in) {
        System.out.println("Enter size of Matrix");
        int y = in.nextInt();
        int x = in.nextInt();
        Matrix matrix = new Matrix(x, y);
        matrix.fill(in);
        return matrix;
    }
}
