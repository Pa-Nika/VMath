package panova.main;

import static java.lang.Math.abs;
import static java.lang.Math.max;

public class Jacobi {
    private double[][] matrixA;
    private double[] matrixB;
    private double[] matrixXNew;
    private double[] matrixHelp;
    private double eps = 0.01;
    private double[] matrixX;

    public void findAnswer() {
        fillMatrixA();
        if (checkMatrix())
            System.out.println("\n\nJacobi method. Diagonal elements > sum of not diagonal. It's OK");
        else {
            System.out.println("\n\nBad matrix...");
            return;
        }
        countJacobi();
    }

    private boolean checkMatrix() {
        double sumDiag = 0;
        for(int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (i != j)
                    sumDiag += abs(matrixA[i][j]);
            }
            if (sumDiag >= abs(matrixA[i][i])) {
                return false;
            }
            sumDiag = 0;
        }
        return true;
    }

    private void fillMatrixA() {
        matrixA = new double[][]{{1, 0.5, 0.33}, {0.5, 0.33, 0.25}, {0.33, 0.25, 0.2}};
        matrixB = new double[]{2, 2, 2};
        matrixHelp = new double[3];
        matrixXNew = new double[3];
        matrixX = new double[]{matrixB[0] / matrixA[0][0], matrixB[1] / matrixA[1][1], matrixB[2] / matrixA[2][2]};
    }

    private void countJacobi() {
        double flag = countExit();
        int count = 0;
        while(flag > eps) {
            count++;
            matrixXNew[0] = (1 / matrixA[0][0]) * (matrixB[0] - matrixA[0][1] * matrixX[1] - matrixA[0][2] * matrixX[2]);
            matrixXNew[1] = (1 / matrixA[1][1]) * (matrixB[1] - matrixA[1][0] * matrixX[0] - matrixA[1][2] * matrixX[2]);
            matrixXNew[2] = (1 / matrixA[2][2]) * (matrixB[2] - matrixA[2][0] * matrixX[0] - matrixA[2][1] * matrixX[1]);
            flag = countExit();
//            System.out.println(flag);
//            System.out.println(count);
            for(int i = 0; i < 3; i++) {
                System.out.print(matrixXNew[i] + " ");
            }
            System.out.println("\n");

            System.arraycopy(matrixXNew, 0, matrixX, 0, 3);
            if (count > 50)
                return;
        }

        System.out.println("FLAG = " + flag);
        for(int i = 0; i < 3; i++) {
            System.out.println(matrixXNew[i]);
        }

    }

    private double countExit() {
        double a = abs(matrixXNew[0] - matrixX[0]);
        double b = abs(matrixXNew[1] - matrixX[1]);
        double c = abs(matrixXNew[2] - matrixX[2]);

        double max = max(a, b);
        max = max(max, c);
        return max;
    }

    private void printB() {
        System.out.println("B:");
        for (int i = 0; i < 3; i++) {
            System.out.print(matrixB[i] + " ");
        }
        System.out.println("\n");
    }

    private void printMatrix() {
        System.out.println("A:");
        for (int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                System.out.print(matrixA[i][j] + " ");
            }
            System.out.println("\n");
        }
    }

}
