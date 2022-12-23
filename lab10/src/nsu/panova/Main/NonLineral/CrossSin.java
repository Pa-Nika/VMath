package nsu.panova.Main.NonLineral;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import static java.lang.Math.*;

public class CrossSin {
    private double[][] z_cross, z_real, z_cross_2;
    private double x_start, x_end, y_end, h, tau, a, r;
    private int SIZE = 50;
    private int SIZEY = 25;

    public void work() {
        fillData();
        cross_sin();
        cross_sin_2();
        writeFirst();
    }

    private void fillData() {
        SIZE = 50;
        SIZEY = 25;
        h = 0.2;
        a = 2;
        tau = 0.075;
        x_start = -1;
        x_end = (h * SIZE) + x_start;
        y_end = tau * SIZEY;
        System.out.println("Нелинейный крест синус. x_end = " + x_end + " y_end = " + y_end);
        if (a * tau / h <= 1 && a * tau / h >= 0) {
            r = a * tau / h;
            System.out.println("r = " + r);
        } else {
            System.out.println("Поменяйте параметры, пожалуйста");
            System.exit(0);
        }

        z_cross = new double[SIZE][SIZEY];
        z_cross_2 = new double[SIZE * 2][SIZEY * 2];
        z_real = new double[SIZE][SIZEY];
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

    private void realSolution_sin() {
        for (int i = 0; i < SIZE; i++) {
            z_real[i][0] = func_sin((x_start + i * h));
        }

        for (int i = 1; i < SIZEY; i++) {
            for (int j = 0; j < SIZE; j++) {
                z_real[j][i] = func_sin((x_start + j * h) - f(z_real[j][i - 1]) * (i * tau));
            }
        }
    }

    private double findMaxTau(int y) {
        double max = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j <= y; j++) {
                double tmp = abs(f(z_cross[i][j]));
                if (tmp > max) {
                    max = tmp;
                }
            }
        }
        double tmp = h * r / max;
        return tau;
    }

    private double findMaxTau2(int y) {
        double max = 0;
        for (int i = 0; i < SIZE * 2; i++) {
            for (int j = 0; j <= y; j++) {
                double tmp = abs(f(z_cross_2[i][j]));
                if (tmp > max) {
                    max = tmp;
                }
            }
        }
        double tmp = h * r / max;
        return tau;
    }

    private void cross_sin_2() {
        double new_h = h / 2;
        double x_h = x_start;
        for (int i = 0; i < SIZE * 2; i++) {
            for (int j = 0; j < SIZEY * 2; j++) {
                z_cross_2[i][j] = 0;
            }
        }

        //нулевой слой. Значем краевые значения из задачи Коши
        for (int i = 0; i < SIZE * 2; i++) {
            z_cross_2[i][0] = func_sin(x_h);
            x_h += new_h;
        }

        for (int i = 1; i < SIZEY * 2; i++) {
            z_cross_2[0][i] = z_cross_2[0][0];
            z_cross_2[SIZE * 2 - 1][i] = z_cross_2[SIZE * 2 - 1][0];
        }


        //Второй слой из правого уголка
        tau = findMaxTau2(0);
        for (int i = 1; i < SIZE * 2; i++) {
            z_cross_2[i][1] = z_cross_2[i][0] - (tau / h) * (f(z_cross_2[i][0]) - f(z_cross_2[i - 1][0]));
        }


        //Остальные слои
        tau = findMaxTau2(1);
        for (int i = 2; i < SIZEY * 2; i++) {
            for (int j = 1; j < SIZE * 2 - 1; j++) {
                z_cross_2[j][i] = z_cross_2[j][i - 2] - (tau / h) * (f(z_cross_2[j + 1][i - 1]) - f(z_cross_2[j - 1][i - 1]));
            }
            tau = findMaxTau2(i);
        }
    }

    private void cross_sin() {
        double x_h = x_start;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZEY; j++) {
                z_cross[i][j] = 0;
            }
        }

        //нулевой слой. Значем краевые значения из задачи Коши
        for (int i = 0; i < SIZE; i++) {
            z_cross[i][0] = func_sin(x_h);
            x_h += h;
        }

        for (int i = 1; i < SIZEY; i++) {
            z_cross[0][i] = z_cross[0][0];
            z_cross[SIZE - 1][i] = z_cross[SIZE - 1][0];
        }

        //Второй слой из правого уголка
        tau = findMaxTau(0);
        for (int i = 1; i < SIZE; i++) {
            z_cross[i][1] = z_cross[i][0] - (tau / h) * (f(z_cross[i][0]) - f(z_cross[i - 1][0]));
        }

        //Остальные слои
        tau = findMaxTau(1);
        for (int i = 2; i < SIZEY; i++) {
            for (int j = 1; j < SIZE - 1; j++) {
                z_cross[j][i] = z_cross[j][i - 2] - (tau / h) * (f(z_cross[j + 1][i - 1]) - f(z_cross[j - 1][i - 1]));
            }
            tau = findMaxTau(i);
        }
    }

    private void writeFirst() {
        PrintWriter writer = null;
        try {
            File fout = new File("output_sin_non_lin.csv");
            writer = new PrintWriter(fout);

            writer.println("Point" + ";" + "Me Func" + ";" + "Me Func x2");

            for (int i = 0; i < SIZE; i++) {
                String result = String.format("%.3f;%.3f;%.3f;%.3f\n", (i * (x_end - x_start) / SIZE + 0.001),
                        z_cross[i][SIZEY - 10],
                        z_cross_2[i * 2][SIZEY * 2 - 10], tau * (SIZEY * 2 - 10)).replace('.', ',');
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
