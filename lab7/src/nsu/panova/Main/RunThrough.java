package nsu.panova.Main;

import java.util.Scanner;


public class RunThrough {
    private int columnCount;
    private double[][] matrixA;
    private double[] vectorB;

    private void mapMem() {
        matrixA = new double[columnCount][columnCount];
        vectorB = new double[columnCount];
    }

    private void readMatrix() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите размерность матрицы А: ");
        columnCount = scanner.nextInt();
        mapMem();

        System.out.println("Введите матрицу А:");
        for (int i = 0; i < columnCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                matrixA[i][j] = scanner.nextDouble();
            }
        }

        System.out.println("Введите вектор B:");
        for (int i = 0; i < columnCount; i++) {
            vectorB[i] = scanner.nextDouble();
        }
    }

    private boolean checkMatrix() {
        for(int i = 0; i < columnCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                if (i == j || i == j - 1 || i == j + 1 ) {
                    continue;
                }
                if (matrixA[i][j] != 0) {
                    System.out.println(matrixA[i][j] + " i = " + i + " j = " + j);
                    return false;
                }
            }
        }
        return true;
    }

    private void algorithm() {
        double[] x = new double[columnCount];
        int count = 0;
        for (int i = 1; i < columnCount; i++) {
            count++;
            matrixA[i][i] = matrixA[i][i] - (matrixA[i][i - 1] * matrixA[i - 1][i]) / matrixA[i - 1][i - 1];
            vectorB[i] = vectorB[i] - (vectorB[i - 1] * matrixA[i][i - 1]) / matrixA[i - 1][i - 1];
        }

        x[columnCount - 1] = vectorB[columnCount - 1] / matrixA[columnCount - 1][columnCount - 1];
        count++;
        for (int i = columnCount - 2; i >= 0; i--) {
            x[i] = (vectorB[i] - x[i + 1] * matrixA[i][i + 1]) / matrixA[i][i];
            count++;
        }

        System.out.println("\nВектор X:");
        for (int i = 0; i < columnCount; i++) {
            System.out.println(x[i]);
        }
        System.out.println("\n\t\tВсего итераций: " + count);
    }

    public void work() {
        System.out.println("\n\nМетод Прогонки");
        readMatrix();
        if (checkMatrix())
            System.out.println("\nRun method OK");
        else {
            System.out.println("\nBad matrix...");
            return;
        }
        algorithm();
    }
}

/*
*
1 2 3
1 3 2
2 7 4
*
4 -1 -1
-1 4 -1
-1 -1 4
*
2 -1 0
-1 2 -1
0 -1 2
*
5 1 0
1 5 1
0 1 5
*
*
*
8 -2 0 0 0
-1 5 3 0 0
0 7 -5 -9 0
0 0 4 7 9
0 0 0 -5 8
*
-7 6 9 -8 5
* */