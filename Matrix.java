import java.util.*;
import java.util.ArrayList;
import java.util.List;
public class Matrix {
    static double[][] array;
    static int rowsCount;
    static int colsCount;
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Введите количество строк ");
        int rows = 0, cols = 0;
        do {
            while (!scanner.hasNextInt()) {
                System.out.println("Введённые данные не являются числом");
                scanner.next();
            }
            rows = scanner.nextInt();
        } while (rows <= 0); //чтобы пользователь не ввел ноль
        System.out.println("Введите количество столбцов ");
        do {
            while (!scanner.hasNextInt()) {
                System.out.println("Введённые данные не являются числом");
                scanner.next(); 
            }
            cols = scanner.nextInt();
        } while (cols <= 0); //чтобы пользователь не ввел ноль
        enterMatrix(rows, cols);
        loop:
        while (true) {
            System.out.println("\nВыберите, что хотите сделать с матрицей");
            System.out.println("1. Вывести");
            System.out.println("2. Умножить на другую матрицу");
            System.out.println("3. Вычислить ранг матрицы");
            System.out.println("4. Транспонировать");
            System.out.println("5. Решить СЛАУ");
            System.out.println("6. Ввести новую матрицу");
            System.out.println("7. Заверишть");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    print(array);
                    break;
                case 2:
                    System.out.println("Введите размер матрицы B:");
                    int r = scanner.nextInt();
                    int c = scanner.nextInt();
                    double[][] arr = new double[r][c];
                    for (int i = 0; i < r; i++) {
                        System.out.printf("Введите строку %d: ", i + 1);
                        for (int j = 0; j < c; j++) {
                            if (scanner.hasNextDouble()) {
                                arr[i][j] = scanner.nextDouble();
                            }else{
                                --j;
                            }
                        }
                    }
                    if (multiply(arr) == null) {
                        System.out.println("Умножение невозможно");
                    } else if (multiply(arr) == arr) {
                        System.out.println("Умножение не было выполнено");
                    } else {
						System.out.println("\nРезультат умножения: \n");
                        print(multiply(arr));
                    }
                    break;
                case 3:
                    System.out.print("Ранг матрицы: ");
                    System.out.println(rangOfMatrix());
                    break;
				case 4:
                    System.out.println("Транспонированная матрица");
                    print(transp(array));
                    break;
                case 5:
                    System.out.printf("Введите свободные коэффициенты(%d)\n", colsCount);
                    double[] k = new double[colsCount];
                    for (int i = 0; i < colsCount; i++) {
                        if (scanner.hasNextDouble()){
                            k[i] = scanner.nextDouble();
                        }else{
                            System.out.println("Введенные данные не являются числом");
                            --i;
                        }
                    }
                    if (solve(k) != null) {
                        for (int i = 0; i < k.length; i++) {
                            System.out.println(Math.round(solve(k)[i] * 1000) / 1000);
                        }
                    }
                    break;
                case 6:
                    System.out.println("Введите размер матрицы: ");
                    if (scanner.hasNextInt() & scanner.nextInt() > 0) {
                        rows = scanner.nextInt();
                    }
                    if (scanner.hasNextInt() & scanner.nextInt() > 0) {
                        cols = scanner.nextInt();
                    }
                    enterMatrix(rows, cols);
                    break;
                case 7:
                    break loop;
            }
        }
    }

    public static double[][] multiply(double[][] another) {
        if (colsCount == another.length) {
            double[][] newMatrix = new double[rowsCount][another[0].length];
            for (int i = 0; i < newMatrix.length; i++) {
                for (int j = 0; j < newMatrix[i].length; j++) {
                    double newCell = 0;
                    for (int p = 0; p < another.length; p++) {
                        newCell += array[i][p] * another[p][j];
                    }
                    newMatrix[i][j] = newCell;
                }
            }
            return newMatrix;
        } else if (rowsCount == another[0].length) {
            System.out.println("Выполнить A*B невозможно, но возможно B*A. Хотите выполнить умножение?");
            String choice = scanner.next();
            if (choice.equalsIgnoreCase("yes") | choice.equalsIgnoreCase("y") | choice.equalsIgnoreCase("да") | choice.equalsIgnoreCase("д")) {
                double[][] newMatrix = new double[another.length][colsCount];
                for (int i = 0; i < newMatrix.length; i++) {
                    for (int j = 0; j < newMatrix[i].length; j++) {
                        double newCell = 0;
                        for (int l = 0; l < array.length; l++) {
                            newCell += another[i][l] * array[l][j];
                        }
                        newMatrix[i][j] = newCell;
                    }
                }
                return newMatrix;
            } else {
                return another;
            }
        }
        return null;
    }

    public static double[] solve(double[] koefficienty) {
        int stroki = array.length;
        int cols = array[0].length;
        if (det(array) == 0) {
            System.out.println("Матрица не обратимая");
            return null;
        }
        double[][] edinic = new double[stroki][2 * cols];
        for (int i = 0; i < stroki; i++) {
            for (int j = 0; j < 2 * cols; j++) {
                if (j < cols) {
                    edinic[i][j] = array[i][j];
                } else {
                    if (j - cols == i) {
                        edinic[i][j] = 1;
                    } else {
                        edinic[i][j] = 0;
                    }
                }
            }
        }
        for (int i = 0; i < stroki; i++) {
            if (edinic[i][i] != 0) {
                for (int m = i + 1; m < stroki; m++) {
                    double koef = -(edinic[m][i] / edinic[i][i]);
                    for (int j = 0; j < 2 * cols; j++) {
                        edinic[m][j] = edinic[m][j] + koef * edinic[i][j];
                    }
                }
            } else {
                for (int l = i + 1; l < stroki; l++) {
                    if (edinic[l][i] != 0) {
                        double[] n = edinic[i];
                        edinic[i] = edinic[l];
                        edinic[l] = n;
                    }
                }
            }
        }
        for (int i = stroki - 1; i >= 0; --i) {
            if (edinic[i][i] != 0) {
                for (int m = i - 1; m >= 0; --m) {
                    double koef = -(edinic[m][i] / edinic[i][i]);
                    for (int j = 2 * cols - 1; j >= 0; --j) {
                        edinic[m][j] = edinic[m][j] + koef * edinic[i][j];
                    }
                }
            } else {
                for (int l = stroki - 1; l >= i + 1; --l) {
                    if (edinic[l][i] != 0) {
                        double[] n = edinic[i];
                        edinic[i] = edinic[l];
                        edinic[l] = n;
                    }
                }
            }
        }
        for (int i = 0; i < stroki; i++) {
            double k = edinic[i][i];
            for (int j = 0; j < 2 * cols; j++) {
                edinic[i][j] = edinic[i][j] / k;
            }
        }
        double[] answer = new double[koefficienty.length];
        for (int i = 0; i < stroki; i++) {
            for (int j = cols; j < 2 * cols; j++) {
                double cell = 0;
                for (int l = 0; l < koefficienty.length; l++) {

                    cell += edinic[i][l + cols] * koefficienty[l];
                }
                answer[i] = cell;
            }
        }
        return answer;
    }

    public static double det(double[][] matrix) {
        int rows = matrix.length;
        if (rows == matrix[0].length) {
            double[][] newMatrix = new double[rows][matrix[0].length];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < matrix[0].length; j++) {
                    newMatrix[i][j] = matrix[i][j];
                }
            }
            for (int i = 0; i < rows; i++) {
                if (newMatrix[i][i] != 0) {
                    for (int m = i + 1; m < rows; m++) {
                        double koef = -(newMatrix[m][i] / newMatrix[i][i]);
                        for (int j = 0; j < rows; j++) {
                            newMatrix[m][j] += koef * newMatrix[i][j];
                        }
                    }
                } else {
                    for (int p = i + 1; p < rows; p++) {
                        if (newMatrix[p][i] != 0) {
                            double[] swap = newMatrix[i];
                            newMatrix[i] = newMatrix[p];
                            newMatrix[p] = swap;
                        }
                    }
                }
            }
            double det = 1;
            for (int i = 0; i < rows; i++) {
                det *= newMatrix[i][i];
            }
            return Math.round(det * 10000) / 10000d;
        }
        return 0;
    }

    public static void enterMatrix(int rows, int cols) {
        colsCount = cols;
        rowsCount = rows;
        double[][] array = new double[rows][cols];
        System.out.println("Как хотите ввести матрицу:");
        System.out.println("1. По элементам");
        System.out.println("2. По строке");
        int ch = scanner.nextInt();
        if (ch == 1) {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    System.out.printf("Введите элемент a[%d][%d]: ", i + 1, j + 1);
                    String element = scanner.next();
                    if (element.contains("/")) {
                        String[] p = element.split("/");
                        if (Double.parseDouble(p[1]) != 0)
                            array[i][j] = Double.parseDouble(p[0]) / Double.parseDouble(p[1]);
                        else{
                            System.out.println("Деление на ноль невозможно");
                            --j;
                        }
                    } else {
                        array[i][j] = Double.parseDouble(element);
                    }
                }
            }
        } else if (ch == 2) {
            System.out.println("Ведите разделитель:");
            String del = scanner.next();
            Scanner scanner2 = new Scanner(System.in);
            if (del.toLowerCase() != "пробел"){
                scanner2 = scanner2.useDelimiter("[\\s" + del + "]+");
            }
            for (int i = 0; i < rows; i++) {
                System.out.printf("Введите строку %d: ", i + 1);
                for (int j = 0; j < cols; j++) {
                    array[i][j] = scanner2.nextDouble();
                }
            }
        }

       Matrix.array = array;
    }

    public static void print(double[][] arr) {
        System.out.printf("Матрица [%d * %d] \n", arr.length, arr[0].length);
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                double a = Math.round(arr[i][j] * 100) / 100d;//округление до двух десятичных знаков
                if (a % 1 == 0.0) { //если число целое
                    System.out.print((int) a + "\t");
                } else {
                    System.out.print(a + "\t");
                }
            }
            System.out.println();
        }
    }

    public static double[][] transp(double [][] arr) {
        double[][] newArr = new double[arr[0].length][arr.length];
        for (int i = 0; i < arr[0].length; i++) {
            double[] arr2 = new double[arr.length];
            for (int j = 0; j < arr.length; j++) {
                arr2[j] = arr[j][i];
				newArr[i] = arr2;
			}
        }
        return newArr;
    }

	 public static int rangOfMatrix(){
        //Matrix a = new Matrix();
        int m = rowsCount;
        int n = colsCount;
        double arra [][]  = new double[rowsCount][colsCount];
        for (int i=0; i < rowsCount; i++){
            for (int j = 0; j < colsCount; j++){
                arra[i][j] = array[i][j];
            }
        }
        double e = 0.00000001;
        int k;
        int l = 0;
        double r;
        int i = 0;
        int j = 0;
        while (i < m && j < n){
            r = 0.0;
            for (k =  i; k < m; ++k){
                if (Math.abs(arra[k][j])>r){
                    l = k;
                    r = Math.abs(arra[k][j]);
                }
            }
            if (r <= e){
                for (k = i; k < m;++k){
                    arra[k][j] = 0.0;
                }
                ++j;
                continue;
            }
            if (l != i){
                for (k = j;k < n;++k){
                    r = arra[i][k];
                    arra[i][k] = arra[l][k];
                    arra[l][k] = r * (-1);
                }
            }
            r = arra[i][j];


            for (k = i + 1;k < m;++k){
                double c = (-1) * arra[k][j]/r;
                arra[k][j] = 0.0;

                for (l = j+1; l<n; ++l){
                    arra[k][l] +=c * arra[i][l];
                }
            }
            ++i;
            ++j;

        }

        return i;
    }
}