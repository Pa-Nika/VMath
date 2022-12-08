package nsu.panova.Main;

import java.util.Scanner;

public class LU {
    private int columnCount;
    private double[][] matrixA;
    private double[][] matrixL;
    private double[] vectorB;
    private int count = 0;

    private void mapMem() {
        matrixA = new double[columnCount][columnCount];
        matrixL = new double[columnCount][columnCount];
        vectorB = new double[columnCount];
    }

    private void readMatrix() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n\nМетод LU разложения");
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

    private void printLUB() {
        System.out.println("\nМатрица L");
        for (int i = 0; i < columnCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                System.out.print(matrixL[i][j] + " ");
            }
            System.out.println();
        }

        System.out.println("\nМатрица U");
        for (int i = 0; i < columnCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                System.out.print(matrixA[i][j] + " ");
            }
            System.out.println();
        }

        System.out.println("\nВектор B");
        for (int i = 0; i < columnCount; i++) {
            System.out.println(vectorB[i]);
        }
    }

    private void countLUB() {
        for (int i = 0; i < columnCount; i++) {
            for (int j = i; j < columnCount; j++) {
                count++;
                if (i == j) {
                    matrixL[j][i] = 1;
                }
                else {
                    matrixL[j][i] = (-1) * matrixA[j][i] / matrixA[i][i];
                    vectorB[j] = matrixL[j][i] * vectorB[i] + vectorB[j];
                    for (int k = i; k < columnCount; k++) {
                        matrixA[j][k] = matrixA[i][k] * matrixL[j][i] + matrixA[j][k];
                        count++;
                    }
                }
            }
        }
    }

    private void countX() {
        double[] x = new double[columnCount];
        double halfMul = 0;
        for (int i  = 0; i < columnCount; i++) {
            x[i] = 1;
        }

        for (int i = columnCount - 1; i >= 0; i--) {
            for (int k = i + 1; k < columnCount; k++) {
                halfMul += matrixA[i][k] * x[k];
            }
            x[i] = (vectorB[i] - halfMul) / matrixA[i][i];
            halfMul = 0;
            count++;
        }

        System.out.println("\nВектор X");
        for (int i = 0; i < columnCount; i++) {
            System.out.println(x[i]);
        }
        System.out.println("\n\t\tВсего итераций: " + count);
    }

    public void work() {
        readMatrix();
        countLUB();
        printLUB();
        countX();
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
* */
