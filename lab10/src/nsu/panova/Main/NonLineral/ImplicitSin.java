package nsu.panova.Main.NonLineral;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import static java.lang.Math.abs;
import static java.lang.Math.sin;

public class ImplicitSin {
    private double[][] z_impl, z_real, z_impl_2;
    private double[][] z_impl_ab, z_real_ab, z_impl_ab_2;
    private double x_start, x_end, y_end, h, tau, a, r;
    private int SIZE = 50;
    private int SIZEY;
    private final double a_ab = 3.0;
    private double b_ab = 1;
    private double[] x;
    private double[] b, c, d, k,u;
    private double[] uIterPrev, uIterCur;
    private double eps = 0.001;

    public void work() {
        SIZEY = SIZE;
        fillData();
        initZeroLevel();
        for (int i = 1; i < SIZEY; i++) {
            if (findMaxConst(i - 1))
                impl_sin(i);
            else
                System.out.println("ААААААААААА ВСЕ ПЛОХО Я УСТАЛА АААААААААААА");
        }

        initZeroLevel2();
        for (int i = 1; i < SIZE * 2; i++) {
            if (findMaxConst2(i - 1))
                impl_sin_2(i);
            else
                System.out.println("ААААААААААА ВСЕ ПЛОХО Я УСТАЛА АААААААААААА");
        }
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

    private void initZeroLevel() {
        double x_h = x_start;
        for (int i = 0; i < SIZEY; i++) {
            for (int j = 0; j < SIZE; j++) {
                z_impl[i][j] = 0;
            }
        }

        //нулевой слой. Знаем краевые значения из задачи Коши
        for (int i = 0; i < SIZE; i++) {
            z_impl[0][i] = func_sin(x_h);
            x_h += h;
        }
    }


    private boolean findMaxConst(int y) {
        double max = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j <= y; j++) {
                double tmp = abs(f(z_impl[i][j]));
                if (tmp > max) {
                    max = tmp;
                }
            }
        }

        return tau * max / h <= 1;
    }

    private boolean findMaxConst2(int y) {
        double max = 0;
        for (int i = 0; i < SIZE * 2; i++) {
            for (int j = 0; j <= y; j++) {
                double tmp = abs(f(z_impl_2[i][j]));
                if (tmp > max) {
                    max = tmp;
                }
            }
        }

        return tau * max / h <= 1;
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
            z_impl_2[0][i] = func_sin(x_h);
            x_h += new_h;
        }
    }

    private void impl_sin_2(int n) {
        for (int i = 0; i < SIZE * 2; i++) {
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
                    b[i] = (-1) * a * tau * uIterPrev[i - 1] / h;
                }
                d[i] = z_impl_2[n - 1][i];
                if (i != SIZE * 2 - 1) {
                    c[i] = a * tau * uIterPrev[i + 1] / h;
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
            for(int i = SIZE * 2 - 1; i > 0; i--) {
                uIterCur[i - 1] = (k[i - 1] * uIterCur[i] + u[i - 1]);
            }
        }

        if (SIZE * 2 >= 0) System.arraycopy(uIterCur, 0, z_impl_2[n], 0, SIZE * 2);
    }

    private void impl_sin(int n) {
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
                    b[i] = (-1) * a * uIterPrev[i - 1] * tau / h;
                }
                d[i] = z_impl[n - 1][i];
                if (i != SIZE - 1) {
                    c[i] = uIterPrev[i + 1] * a * tau / h;
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

        if (SIZE >= 0) System.arraycopy(uIterCur, 0, z_impl[n], 0, SIZE);
    }

    private void writeFirst() {
        PrintWriter writer = null;
        try {
            File fout = new File("output_sin_impl_non_lin.csv");
            writer = new PrintWriter(fout);

            writer.println("Point" + ";"  + "Me Func" + ";" + "Me Func x2" );

            for (int i = 0; i < SIZEY; i++) {
                String result = String.format("%.3f;%.3f;%.3f;\n", (i * (x_end - x_start) / SIZEY + 0.001), z_impl[SIZE / 4][i],
                        z_impl_2[SIZE / 2][i * 2]).replace('.', ',');
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

}
