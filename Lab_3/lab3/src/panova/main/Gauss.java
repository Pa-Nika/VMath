package panova.main;

import static java.lang.Math.abs;
import static java.lang.Math.max;

public class Gauss {
    private double[][] matrixA;
    private double[] matrixB;
    private double[] matrixY;
    private double[] matrixHelp;

    public void findAnswer() {
        System.out.println("Method Gauss (a_11 != 0)");
        fillMatrixA();
        countGauss();
        triangularMatrixSolution();
    }

    private void fillMatrixA() {
        matrixA = new double[][]{{20, 20, 0}, {15, 15, 5}, {0, 1, 1}};
        matrixB = new double[]{40.0, 35.0, 2.0};
        matrixHelp = new double[2];
        matrixY = new double[3];
    }

    private void countGauss() {
        System.out.println("Let's multiply the first system formula on (-ai1/a11) and add together with the formula\n");

        double tmp = matrixA[0][0];
        double matrixCoef = matrixA[1][0];
        double matrixCoef2 = matrixA[2][0];
        for (int i = 0; i < 3; i++) {
            double mulNum1 = matrixA[0][i] * (-1) * matrixCoef / tmp;
            matrixA[1][i] +=  mulNum1;

            double mulNum2 = matrixA[0][i] * (-1) * matrixCoef2 / tmp;
            matrixA[2][i] +=  mulNum2;
        }
        matrixB[1] += matrixB[0] * (-1) * matrixCoef / tmp;
        matrixB[2] += matrixB[0] * (-1) * matrixCoef2 / tmp;
        printMatrix();
        printB();

        System.out.println("Element a_22 = 0. Change rows\n");
        for (int i = 1; i < 3; i++) {
            matrixHelp[i - 1] = matrixA[1][i];
            matrixA[1][i] = matrixA[2][i];
            matrixA[2][i] = matrixHelp[i - 1];
        }
        matrixY[1] = matrixB[1];
        matrixB[1] = matrixB[2];
        matrixB[2] = matrixY[1];

        printMatrix();
        printB();
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

    private void printB() {
        System.out.println("B:");
        for (int i = 0; i < 3; i++) {
            System.out.print(matrixB[i] + " ");
        }
        System.out.println("\n");
    }

    private void triangularMatrixSolution() {
        System.out.println("ANSWER");

        double z = matrixB[2] / matrixA[2][2];
        System.out.println("z = " + z);
        double y;
        y = (matrixB[1] - matrixA[1][2] * z) / matrixA[1][1];
        System.out.println("y = " + y);
        double x;
        x = (matrixB[0] - matrixA[0][2] * z - matrixA[0][1] * y) / matrixA[0][0];
        System.out.println("x = " + x);
    }











//    private double norma() {
//        double norma[]  = new double[3];
//        for(int i = 0; i < 3; i++) {
//            for( int j = 0; j < 3; j++) {
//                norma[i] += abs(matrixA[i][j]);
//            }
//        }
//        double max = max(norma[0],norma[1]);
//        return max(max, norma[2]);
//    }



}
