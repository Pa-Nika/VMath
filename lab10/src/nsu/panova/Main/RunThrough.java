package nsu.panova.Main;

import java.util.Scanner;


public class RunThrough {
    private int columnCount;
    private double[][] matrixA;
    private double[] vectorB;
    private double r;

    private void mapMem() {
        matrixA = new double[columnCount][columnCount];
        vectorB = new double[columnCount];
    }

    private void readMatrix(double r_, double z_imp0, double z_imp1, double z_imp2) {
        columnCount = 3;
        r = r_;
        mapMem();

        for (int i = 0; i < columnCount; i++) {
            for(int j = 0; j < columnCount; j++) {
                matrixA[i][j] = 0;
            }
        }

        for (int i = 0; i < columnCount; i++) {
            if (i + 1 < 3) {
                matrixA[i][i + 1] = r / 2;
                matrixA[i + 1][i] = (-1) * r / 2;
            }
            matrixA[i][i] = 1;
        }

        vectorB[0] = z_imp0;
        vectorB[1] = z_imp1;
        vectorB[2] = z_imp2;
    }

    private double algorithm() {
        double[] x = new double[columnCount];
        for (int i = 1; i < columnCount; i++) {
            matrixA[i][i] = matrixA[i][i] - (matrixA[i][i - 1] * matrixA[i - 1][i]) / matrixA[i - 1][i - 1];
            vectorB[i] = vectorB[i] - (vectorB[i - 1] * matrixA[i][i - 1]) / matrixA[i - 1][i - 1];
        }

        x[columnCount - 1] = vectorB[columnCount - 1] / matrixA[columnCount - 1][columnCount - 1];
        for (int i = columnCount - 2; i >= 0; i--) {
            x[i] = (vectorB[i] - x[i + 1] * matrixA[i][i + 1]) / matrixA[i][i];
        }

        return x[1];
    }

    public double work(double r, double z_imp1, double z_imp2, double z_imp3) {
        readMatrix(r, z_imp1, z_imp2, z_imp3);
        return algorithm();
    }
}
