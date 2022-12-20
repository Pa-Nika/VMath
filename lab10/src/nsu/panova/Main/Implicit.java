package nsu.panova.Main;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import static java.lang.Math.abs;
import static java.lang.Math.sin;

public class Implicit {
    private double[][] z_impl, z_real, z_impl_2;
    private double[][] z_impl_ab, z_real_ab, z_impl_ab_2;
    private double x_start, x_end, y_end, h, tau, a, r;
    private int SIZE = 50;
    private int SIZEY;
    private double[][] matrixA = new double[3][3];
    private final double a_ab = 3.0;
    private double b_ab = 1;
    private double[] x;
    private double[] b, c, d, k,u;
    private double[] uIterPrev, uIterCur;
    private double eps = 0.001;

    public void work() {
        fillData();

        realSolution_sin();
        impl_sin();
        impl_sin_2();
        writeFirst();
    }

    private void fillData() {
        SIZEY = SIZE;
        x_start = 0;
        x_end = 10;
        y_end = 2;
        h = (x_end - x_start) / SIZE;
        System.out.println("h = " + h);
        tau = (y_end) / SIZEY;
        System.out.println("tau = " + tau);
        a = 2;

        if (a * tau / h <= 1 && a * tau / h >= 0) {
            r = a * tau / h;
            System.out.println("r = " + r);
        } else {
            System.out.println("Поменяйте параметры, пожалуйста");
            System.exit(0);
        }

        z_impl = new double[SIZE][SIZEY];
        z_impl_2 = new double[SIZE * 2][SIZEY * 2];
        z_real = new double[SIZE][SIZEY];

        z_impl_ab = new double[SIZE][SIZEY];
        z_impl_ab_2 = new double[SIZE * 2][SIZEY * 2];
        z_real_ab = new double[SIZE][SIZEY];

        x = new double[3];
        b = new double[SIZEY * 2];
        c = new double[SIZEY * 2];
        d = new double[SIZEY * 2];
        k = new double[SIZEY * 2];
        u = new double[SIZEY * 2];
        uIterPrev = new double[SIZEY * 2];
        uIterCur = new double[SIZEY * 2];
    }

    private double func_sin(double x) {
        if (x < 1)
            return 0;
        if (x < 4)
            return sin(3.1415 * (x - 1) / 3);
        return 0;
    }
    private void realSolution_sin() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZEY; j++) {
                z_real[i][j] = func_sin((x_start + i * h) - a * (j * tau));
            }
        }
    }


    private void impl_sin_2() {
        double new_h = h / 2;
        RunThrough run = new RunThrough();
        double x_h = x_start;
        for (int i = 0; i < SIZE * 2; i++) {
            for (int j = 0; j < SIZEY * 2; j++) {
                z_impl_2[i][j] = 0;
            }
        }

        //нулевой слой. Значем краевые значения из задачи Коши
        for (int i = 0; i < SIZE * 2; i++) {
            z_impl_2[i][0] = func_sin(x_h);
            x_h += new_h;
        }

        //Остальные слои
        for (int j = 1; j < SIZEY * 2 - 1; j++) {
            for (int i = 1; i < SIZE * 2 - 1; i++) {
//                z_impl_2[i][j] = run.work(r, z_impl_2[i - 1][j - 1], z_impl_2[i][j - 1], z_impl_2[i + 1][j - 1]);
                z_impl_2[i][j] = run(r, z_impl_2[i - 1][j - 1], z_impl_2[i][j - 1], z_impl_2[i + 1][j - 1]);
            }
        }
    }

    private void impl_sin() {
        RunThrough run = new RunThrough();
        double x_h = x_start;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZEY; j++) {
                z_impl[i][j] = 0;
            }
        }

        //нулевой слой. Значем краевые значения из задачи Коши
        for (int i = 0; i < SIZE; i++) {
            z_impl[i][0] = func_sin(x_h);
            x_h += h;
        }

        //Остальные слои
        for (int j = 1; j < SIZE - 1; j++) {
            for (int i = 1; i < SIZEY - 1; i++) {
//                z_impl[i][j] = run.work(r, z_impl[i - 1][j - 1], z_impl[i][j - 1], z_impl[i + 1][j - 1]);
                z_impl[i][j] = run(r, z_impl[i - 1][j - 1], z_impl[i][j - 1], z_impl[i + 1][j - 1]);
            }
        }
    }

    private void writeFirst() {
        this.SIZE = 2;
        PrintWriter writer = null;
        try {
            File fout = new File("output_sin_impl.csv");
            writer = new PrintWriter(fout);

            writer.println("Point" + ";" + "Real Func" + ";" + "Me Func" + ";" + "Me Func x2" + ";" + "Difference");
            for (int i = 0; i < SIZEY; i++) {
                String result = String.format("%.3f;%.3f;%.3f;%.3f;%.3f\n", (i * (x_end - x_start) / SIZEY + 0.001), z_real[i][SIZE / 2], z_impl_2[i * 2][SIZE],
                        z_impl[i][SIZE / 2], abs(z_real[i][SIZEY / 2] - z_impl_2[i * 2][SIZEY])).replace('.', ',');
                writer.printf(result);
            }
            writer.close();
            SIZE = SIZEY;
        } catch (IOException e) {
            System.err.println("Error while writing file:" + e.getLocalizedMessage());
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }







    private double run(double r, double z_imp1, double z_imp2, double z_imp3) {
        for (int i = 0; i < 3; i++) {
            b[i] = c[i] = d[i] = 0;
            uIterPrev[i] = 1;
            uIterCur[i] = 0;
            k[i] = 0;
            u[i] = 0;
        }

        for (int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                matrixA[i][j] = 0;
            }
        }

        for (int i = 0; i < 3; i++) {
            if (i + 1 < 3) {
                matrixA[i][i + 1] = r / 2;
                matrixA[i + 1][i] = (-1) * r / 2;
            }
            matrixA[i][i] = 1;
        }

        uIterPrev[0] = z_imp1;
        uIterPrev[1] = z_imp2;
        uIterPrev[2] = z_imp3;

        for(int i = 0; i < 3; i++) {
            if (i != 0) {
                d[i] = matrixA[i][i - 1];
            }
            b[i] = matrixA[i][i];
            if (i != 3 - 1) {
                c[i] = matrixA[i][i + 1];
            }
        }

        k[0] -= c[0] / b[0];
        u[0] = uIterPrev[0] / b[0];
        for (int i = 1; i < 3; i++) {
            if (i != 3 - 1)
                k[i] -= c[i] / (b[i] + d[i] * k[i - 1]);
            u[i] -= (d[i] * u[i - 1] - uIterPrev[i]) / (b[i] + d[i]* k[i - 1]);
        }
        uIterCur[3 - 1] = uIterPrev[3 - 1];
        for(int i = 3 - 1; i > 0; i--) {
            uIterCur[i - 1] = (k[i - 1] * uIterCur[i] + u[i - 1]);
        }

        return uIterCur[1];
    }






//
//
//
//
//
//
//
//
//
//    private double func_ab(double x) {
//        if (x < 0)
//            return a_ab;
//        return b_ab;
//    }
//
//    private void impl_ab_2() {
//        double new_h = h / 2;
//        double x_h = x_start;
//        RunThrough run = new RunThrough();
//        for (int i = 0; i < SIZE * 2; i++) {
//            for (int j = 0; j < SIZEY * 2; j++) {
//                z_impl_ab_2[i][j] = 0;
//                z_impl_ab_2[0][j] = a_ab;
//                z_impl_ab_2[SIZE * 2 - 1][j] = b_ab;
//            }
//        }
//
//        //нулевой слой. Значем краевые значения из задачи Коши
//        for(int i = 0; i < SIZE * 2; i++) {
//            z_impl_ab_2[i][0] = func_ab(x_h);
//            x_h += new_h;
//        }
//
//        //Второй слой из правого уголка
//        for (int i = 1; i < SIZE * 2 - 1; i++) {
//            z_impl_ab_2[i][1] = z_impl_ab_2[i][0] * (1 + r) + z_impl_ab_2[i + 1][0] * (r) * (-1);
//        }
//
//        //Остальные слои
//        for(int i = 2; i < SIZEY * 2 - 1; i++) {
//            for (int j = 1; j < SIZE * 2 - 1; j++) {
//                z_impl_ab_2[i][j] = run(r, z_impl_ab_2[i - 1][j - 1], z_impl_ab_2[i][j - 1], z_impl_ab_2[i + 1][j - 1]);
////                z_impl_ab_2[j][i] = z_impl_ab_2[j][i - 2] - r * (z_impl_ab_2[j + 1][i - 1] - z_impl_ab_2[j - 1][i - 1]);
//            }
//        }
//    }
//
//
//    private void impl_ab() {
//        double x_h = x_start;
//        RunThrough run = new RunThrough();
//        for (int i = 0; i < SIZE; i++) {
//            for (int j = 0; j < SIZEY; j++) {
//                z_impl_ab[i][j] = 0;
//                z_impl_ab[0][j] = a_ab;
//                z_impl_ab[SIZE - 1][j] = b_ab;
//            }
//        }
//
//        //нулевой слой. Значем краевые значения из задачи Коши
//        for(int i = 0; i < SIZE; i++) {
//            z_impl_ab[i][0] = func_ab(x_h);
//            x_h += h;
//        }
//
//        //Остальные слои
//        for(int i = 1; i < SIZEY - 1; i++) {
//            for (int j = 1; j < SIZE - 1; j++) {
//                z_impl_ab[i][j] = run(r, z_impl_ab[i - 1][j - 1], z_impl_ab[i][j - 1], z_impl_ab[i + 1][j - 1]);
////                z_impl_ab[j][i] = z_impl_ab[j][i - 2] - r * (z_impl_ab[j + 1][i - 1] - z_impl_ab[j - 1][i - 1]);
//            }
//        }
//    }
//
//    private void realSolutionAb() {
//        for (int i = 0; i < SIZE; i++) {
//            for (int j = 0; j < SIZEY; j++) {
//                z_real_ab[i][j] = func_ab((x_start + i * h) - a * (j * tau));
////                System.out.println("Real solution = " + z[i][j]);
//            }
////            System.out.println("\n");
//        }
//    }
//
//    private void writeAb() {
//        PrintWriter writer = null;
//        try {
//            File fout = new File("output_ab_impl.csv");
//            writer = new PrintWriter(fout);
//
//            writer.println("Point"+ ";" + "Real Func" + ";" + "Me Func" + ";" + "Me Func x2" + ";" + ";Point;Difference1" + ";" + "Difference2");
//
//            for (int i = 0; i < SIZE; i++) {
//                String result = String.format("%.3f;%.3f;%.3f;%.3f;;%.3f;%.3f;%.3f;\n", (i * (x_end - x_start) / SIZE + 0.001),
//                        z_real_ab[i][SIZE / 2], z_impl_ab[i][SIZE / 5],
//                        z_impl_ab_2[i * 2][SIZE / 5], (i * (x_end - x_start) / SIZE + 0.001), abs(z_real_ab[i][SIZE / 2] - z_impl_ab[i][SIZE / 2]),
//                        abs(z_real_ab[i][SIZE / 2] - z_impl_ab_2[i*2][SIZE])).replace('.', ',');
//                writer.printf(result);
//            }
//            writer.close();
//        } catch (IOException e){
//            System.err.println("Error while writing file:" + e.getLocalizedMessage());
//        }
//        finally {
//            if (writer != null) {
//                writer.close();
//            }
//        }
//    }
}
