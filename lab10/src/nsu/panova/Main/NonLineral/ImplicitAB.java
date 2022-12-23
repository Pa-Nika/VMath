package nsu.panova.Main.NonLineral;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import static java.lang.Math.abs;
import static java.lang.Math.pow;


public class ImplicitAB {
    private double[][] z_impl, z_real, z_impl_2;
    private double[][] z_impl_ab, z_real_ab, z_impl_ab_2;
    private double x_start, x_end, y_end, h, tau, a, r;
    private int SIZE = 50;
    private int SIZEY = 25;
    private final double a_ab = 3.0;
    private double b_ab = 1;
    private double[] x;
    private double[] b, c, d, k,u;
    private double[] uIterPrev, uIterCur;
    private double eps = 0.001;

    public void work() {
        double tmp;
        fillData();
        initZeroLevel();
        realSolution_ab();
        for (int i = 1; i < SIZEY; i++) {
            tmp = countTau(i);
            impl_ab(i, tmp);

        }

        initZeroLevel2();
        for (int i = 1; i < SIZEY * 2; i++) {
            tmp = countTau2(i);
            impl_ab_2(i, tmp);
        }
        writeFirst();
    }

    private void fillData() {
        SIZE = 50;
        SIZEY = 25;
        h = 0.2;
        a = 2;
        tau = 0.1;
        x_start = -1;
        x_end = (h * SIZE) + x_start;
        y_end = tau * SIZEY;
        System.out.println("Нелинейная неявная схема AB. x_end = " + x_end + " y_end = " + y_end);
        r = a * tau / h;
        System.out.println("r = " + r);

        z_impl = new double[SIZEY][SIZE];
        z_impl_2 = new double[SIZEY * 2][SIZE * 2];
        z_real_ab = new double[SIZEY][SIZE];

        x = new double[3];
        b = new double[SIZE * 2];
        c = new double[SIZE * 2];
        d = new double[SIZE * 2];
        k = new double[SIZE * 2];
        u = new double[SIZE * 2];
        uIterPrev = new double[SIZE * 2];
        uIterCur = new double[SIZE * 2];
    }

    private double func_ab(double x) {
        if (x < 0)
            return a_ab;
        return b_ab;
    }

    private double f(double u) {
        return u * u / 2;
    }

    private boolean convergenceCondition() {
        for (int i = 0; i < SIZE; i++) {
            if (abs(uIterPrev[i] - uIterCur[i]) > eps)
                return true;
        }
        return false;
    }

    private double initAnswerLevel(int i, int n, double[][] u) {
        if (i == 0) {
            return r * (u[n - 1][i + 1]) + u[n - 1][i];
        }
        if (i == SIZE) {
            return r * (u[n - 1][i - 1]) + u[n - 1][i];
        }
        return r * (u[n - 1][i - 1] - u[n - 1][i + 1]) + u[n - 1][i];
    }

    private double countTau(int n) {
        double max = 0;
        for (int i = 0; i < SIZE; i++) {
            if(abs(pow(z_impl[n - 1][i], 2)) > max) {
                max = abs(f(z_impl[n - 1][i]));
            }
        }
        return 0.2 * h / max;
    }

    private double countTau2( int n) {
        double max = 0;
        for (int i = 0; i < SIZE * 2; i++) {
            if(abs(pow(z_impl_2[n - 1][i], 2)) > max) {
                max = abs(f(z_impl_2[n - 1][i]));
            }
        }
        return 0.2 * h / max;
    }

    private void realSolution_ab() {
        for (int i = 0; i < SIZE; i++) {
            z_real_ab[0][i] = func_ab((x_start + i * h));
        }

        for (int i = 0; i < SIZEY; i++) {
            z_real_ab[i][0] = func_ab((x_start));
            z_real_ab[i][SIZE - 1] = func_ab((x_end));
        }

        for (int i = 1; i < SIZEY; i++) {
            for (int j = 0; j < SIZE; j++) {
                z_real_ab[i][j] = func_ab((x_start + j * h) - f(z_real_ab[i - 1][j]) * (i * tau));
            }
        }
    }

    private void initZeroLevel() {
        double x_h = x_start;
        for (int i = 0; i < SIZEY; i++) {
            for (int j = 0; j < SIZE; j++) {
                z_impl[i][j] = 0;
            }
        }

        //нулевой слой. Знаем краевые значения из задачи Коши
        for (int i = 0; i < SIZE; i++) {
            z_impl[0][i] = func_ab(x_h);
            x_h += h;
        }
        for (int i = 1; i < SIZEY; i++) {
            z_impl[i][0] = z_impl[0][0];
            z_impl[i][SIZE - 1] = z_impl[0][SIZE - 1];
        }
    }

    private boolean findMaxConst(int y) {
        double max = 0;
        while (tau * max / h >= 1) {
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j <= y; j++) {
                    double tmp = abs(f(z_impl[i][j]));
                    if (tmp > max) {
                        max = tmp;
                    }
                }
            }
            tau -= 0.05;
        }

        return true;
    }

    private boolean findMaxConst2(int y) {
        double max = 0;
        while (tau * max / h >= 1) {
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j <= y; j++) {
                    double tmp = abs(f(z_impl_2[i][j]));
                    if (tmp > max) {
                        max = tmp;
                    }
                }
            }
            tau -= 0.05;
        }

        return true;
    }

    private void initZeroLevel2() {
        double x_h = x_start;
        double new_h = h / 2;
        for (int i = 0; i < SIZEY * 2; i++) {
            for (int j = 0; j < SIZE * 2; j++) {
                z_impl_2[i][j] = 0;
            }
        }

        //нулевой слой. Знаем краевые значения из задачи Коши
        for (int i = 0; i < SIZE * 2; i++) {
            z_impl_2[0][i] = func_ab(x_h);
            x_h += new_h;
        }
        for (int i = 1; i < SIZEY * 2; i++) {
            z_impl_2[i][0] = z_impl_2[0][0];
            z_impl_2[i][SIZE * 2 - 1] = z_impl_2[0][SIZE * 2 - 1];
        }
    }

    private void impl_ab_2(int n, double tau_) {
        for (int i = 0; i < SIZE; i++) {
            b[i] = c[i] = d[i] = 0;
            uIterPrev[i] = 1;
            uIterCur[i] = 0;
        }

        while(convergenceCondition()) {
            for (int i = 0; i < SIZE * 2; i++) {
                k[i] = 0;
                u[i] = 0;
            }

            for(int i = 0; i < SIZE * 2; i++) {
                if (i != 0) {
                    b[i] = (-1) * tau_ * uIterPrev[i - 1] / (2 * h);
                }
                d[i] = z_impl_2[n - 1][i];
                if (i != SIZE * 2 - 1) {
                    c[i] = tau_ * uIterPrev[i + 1] /(2 *  h);
                }
            }

            k[0] -= c[0];
            u[0] = d[0];
            for (int i = 1; i < SIZE * 2; i++) {
                if (i != SIZE * 2 - 1)
                    k[i] -= c[i] / (1 + b[i] * k[i - 1]);
                u[i] -= (b[i] * u[i - 1] - d[i]) / (1 + b[i] * k[i - 1]);
            }

            if (SIZE * 2 >= 0) System.arraycopy(uIterCur, 0, uIterPrev, 0, SIZE * 2);

            uIterCur[SIZE * 2 - 1] = uIterPrev[SIZE * 2 - 1];
            for (int i = SIZE * 2 - 1; i > 0; i--) {
                uIterCur[i - 1] = (k[i - 1] * uIterCur[i] + u[i - 1]);
            }
        }

        for (int i = 1; i < SIZE * 2 - 1; i++) {
            z_impl_2[n][i] = uIterCur[i];
        }
    }

    private void impl_ab(int n, double tau_) {
        for (int i = 0; i < SIZE; i++) {
            b[i] = c[i] = d[i] = 0;
            uIterPrev[i] = 1;
            uIterCur[i] = 0;
        }

        while(convergenceCondition()) {
            for (int i = 0; i < SIZE; i++) {
                k[i] = 0;
                u[i] = 0;
            }

            for(int i = 0; i < SIZE; i++) {
                if (i != 0) {
                    b[i] = (-0.2) * uIterPrev[i - 1] * tau_ / (h);
                }
                d[i] = z_impl[n - 1][i];
                if (i != SIZE - 1) {
                    c[i] = 0.2 * uIterPrev[i + 1] * tau_ / (h);
                }
            }

            k[0] -= c[0];
            u[0] = d[0];
            for (int i = 1; i < SIZE; i++) {
                if (i != SIZE - 1)
                    k[i] -= c[i] / (1 + b[i] * k[i - 1]);
                u[i] -= (b[i] * u[i - 1] - d[i]) / (1 + b[i] * k[i - 1]);
            }

            if (SIZE >= 0) System.arraycopy(uIterCur, 0, uIterPrev, 0, SIZE);

            uIterCur[SIZE - 1] = uIterPrev[SIZE - 1];
            for(int i = SIZE - 1; i > 0; i--) {
                uIterCur[i - 1] = (k[i - 1] * uIterCur[i] + u[i - 1]);
            }
        }

        for (int i = 1; i < SIZE - 1; i++) {
            z_impl[n][i] = uIterCur[i];
        }
    }

    private void writeFirst() {
        PrintWriter writer = null;
        try {
            File fout = new File("output_ab_impl_non_lin.csv");
            writer = new PrintWriter(fout);

            writer.println("Point" + ";Real;"  + "Me Func" + ";" + "Me Func x2" );

            for (int i = 0; i < SIZE; i++) {
                String result = String.format("%.3f;%.3f;%.3f;%.3f;\n", (i * (x_end - x_start) / SIZE + 0.001),
                        z_real_ab[SIZEY / 3][i],
                        z_impl[SIZEY / 3][i], z_impl_2[SIZEY * 2 / 3][i * 2]).replace('.', ',');
                writer.printf(result);
            }
            writer.close();
        } catch (IOException e) {
            System.err.println("Error while writing file:" + e.getLocalizedMessage());
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
}
