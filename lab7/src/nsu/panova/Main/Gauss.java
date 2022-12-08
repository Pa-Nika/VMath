package nsu.panova.Main;

import java.util.Scanner;

import static java.lang.Math.abs;
import static java.lang.Math.max;

public class Gauss {
    private int columnCount;
    private double[][] matrixA;
    private double[] vectorB;
    private double[] vectorX;
    private double[] vectorXNew;
    private double eps = 0.01;

    private void initMem() {
        vectorX = new double[columnCount];
        vectorXNew = new double[columnCount];
        matrixA = new double[columnCount][columnCount];
        vectorB = new double[columnCount];
    }

    private double countExit() {
        double max = 0;
        for (int i = 0; i < columnCount; i++) {
            max = max(abs(vectorXNew[i] - vectorX[i]), max);
        }
        return max;
    }

    private void iteration() {
        double flag = countExit();
        double firstSum = 0;
        double secondSum = 0;
        int count = 0;
        while(flag > eps) {
            for (int i = 0; i < columnCount; i++) {
                for (int j = 0; j < i; j++) {
                    firstSum += matrixA[i][j] * vectorXNew[j];
                }
                for (int j = i + 1; j < columnCount; j++) {
                    secondSum += matrixA[i][j] * vectorX[j];
                }

                vectorXNew[i] = (vectorB[i] - firstSum - secondSum) / matrixA[i][i];
                firstSum = secondSum = 0;
                count++;
            }

            flag = countExit();
            System.arraycopy(vectorXNew, 0, vectorX, 0, columnCount);
        }

        System.out.println("\nВектор X");
        for (int i = 0; i < columnCount; i++) {
            System.out.println(vectorXNew[i]);
        }
        System.out.println("\n\t\tВсего итераций: " + count);
    }

    private void readMatrix() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите размерность матрицы А: ");
        columnCount = scanner.nextInt();
        initMem();
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

        for (int i = 0; i < columnCount; i++) {
            vectorX[i] = vectorB[i] / matrixA[i][i];
        }
    }

    private boolean checkMatrix() {
        double sumDiag = 0;
        for(int i = 0; i < columnCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                if (i != j)
                    sumDiag += abs(matrixA[i][j]);
            }
            if (sumDiag > abs(matrixA[i][i])) {
                return false;
            }
            sumDiag = 0;
        }
        return true;
    }

    public void work() {
        System.out.println("\n\nМетод Гаусса-Зейделя");
        readMatrix();
        if (checkMatrix())
            System.out.println("\nGauss method. Diagonal elements > sum of not diagonal. It's OK");
        else {
            System.out.println("\nBad matrix...");
            iteration();
            return;
        }

        iteration();
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
* */
