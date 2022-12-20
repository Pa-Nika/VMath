package nsu.panova.Main.NonLineral;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import static java.lang.Math.abs;
import static java.lang.Math.sin;

public class CrossAB {
    private double[][] z_cross, z_real, z_cross_2;
    private double[][] z_cross_ab, z_real_ab, z_cross_ab_2;
    private double x_start, x_end, y_end, h, tau, a, r;
    private final int SIZE = 50;
    private final int SIZEY = 50;
    private final double a_ab = 3.0;
    private double b_ab = 1;

    public void work() {
        fillData();
        cross_ab();
        cross_ab_2();

        realSolution_ab();
        writeFirst();
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

    private double func_ab(double x) {
        if (x < 0)
            return a_ab;
        return b_ab;
    }

    private void realSolution_ab() {
        for (int i = 0; i < SIZE; i++) {
            z_real_ab[i][0] = func_ab((x_start + i * h));
            z_real_ab[0][i] = func_ab((x_start));
            z_real_ab[SIZE - 1][i] = func_ab((x_end));
        }

        for (int i = 1; i < SIZE; i++) {
            for (int j = 0; j < SIZEY; j++) {
                z_real_ab[j][i] = func_ab((x_start + j * h) - f(z_real_ab[j][i - 1]) * (i * tau));
//                System.out.println("AB: " + z_real_ab[j][i]);
            }
        }
    }

    private boolean findMaxConst(int y) {
        double max = 0;
        while (tau * max / h >= 1) {
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j <= y; j++) {
                    double tmp = abs(f(z_cross[i][j]));
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
            for (int i = 0; i < SIZE * 2; i++) {
                for (int j = 0; j <= y; j++) {
                    double tmp = abs(f(z_cross_2[i][j]));
                    if (tmp > max) {
                        max = tmp;
                    }
                }
            }
            tau -= 0.05;
        }

        return true;
    }

    private void cross_ab_2() {
        double new_h = h / 2;
        double x_h = x_start;
        for (int i = 0; i < SIZE * 2; i++) {
            for (int j = 0; j < SIZEY * 2; j++) {
                z_cross_2[i][j] = 0;
                z_cross_2[0][j] = a_ab;
                z_cross_2[SIZE * 2 - 1][j] = b_ab;
            }
        }

        //нулевой слой. Значем краевые значения из задачи Коши
        for (int i = 0; i < SIZE * 2; i++) {
            z_cross_2[i][0] = func_ab(x_h);
            x_h += new_h;
        }

        //Второй слой из правого уголка
        if (findMaxConst2(0)) {
            for (int i = 1; i < SIZE * 2 - 1; i++) {
                z_cross_2[i][1] = z_cross_2[i][0] - (tau / h) * (f(z_cross_2[i][0]) - f(z_cross_2[i - 1][0]));
            }
        } else {
            System.out.println("Нелинейный крест в синусом. Удвоенный случай. Не выполнилось условие на число курента...");
            return;
        }


        //Остальные слои
        if (!findMaxConst2(1)) {
            System.out.println("Нелинейный крест в AB. Удвоенный случай. Не выполнилось условие на число курента...");
            return;
        }
        for (int i = 2; i < SIZEY * 2; i++) {
            for (int j = 1; j < SIZE * 2 - 1; j++) {
                z_cross_2[j][i] = z_cross_2[j][i - 2] - (tau / h) * (f(z_cross_2[j + 1][i - 1]) - f(z_cross_2[j - 1][i - 1]));
            }
            if (!findMaxConst2(i - 1)) {
                System.out.println("Нелинейный крест в AB. Удвоенный случай. Не выполнилось условие на число курента...");
                return;
            }
        }
    }

    private void cross_ab() {
        double x_h = x_start;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZEY; j++) {
                z_cross[i][j] = 0;
                z_cross[0][j] = a_ab;
                z_cross[SIZE - 1][j] = b_ab;
            }
        }

        //нулевой слой. Значем краевые значения из задачи Коши
        for (int i = 0; i < SIZE; i++) {
            z_cross[i][0] = func_ab(x_h);
            x_h += h;
        }

        //Второй слой из правого уголка
        if (findMaxConst(0)) {
            for (int i = 1; i < SIZE - 1; i++) {
                z_cross[i][1] = z_cross[i][0] - (tau / h) * (f(z_cross[i][0]) - f(z_cross[i - 1][0]));
            }
        } else {
            System.out.println("Нелинейный крест в AB. Обычный случай. Не выполнилось условие на число курента... Хнык");
            return;
        }


        //Остальные слои
        if (!findMaxConst(1)) {
            System.out.println("Нелинейный крест в AB. Обычный случай. Не выполнилось условие на число курента...");
            return;
        }
        for (int i = 2; i < SIZEY; i++) {
            for (int j = 1; j < SIZE - 1; j++) {
//                z_cross[j][i] = z_cross[j][i - 2] - r * (z_cross[j + 1][i - 1] - z_cross[j - 1][i - 1]);
                z_cross[j][i] = z_cross[j][i - 2] - (tau / h) * (f(z_cross[j + 1][i - 1]) - f(z_cross[j - 1][i - 1]));
            }
            if (!findMaxConst(i - 1)) {
                System.out.println("Нелинейный крест в AB. Обычный случай. Не выполнилось условие на число курента...");
                return;
            }
        }
    }


    private void fillData() {
        x_start = 0;
        x_end = 9;
        y_end = 2;
        h = (x_end - x_start) / SIZE;
        System.out.println("h = " + h);
        tau = (y_end) / SIZEY;
        a = 2;

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

        z_cross_ab = new double[SIZE][SIZEY];
        z_cross_ab_2 = new double[SIZE * 2][SIZEY * 2];
        z_real_ab = new double[SIZE][SIZEY];
    }

    private void writeFirst() {
        PrintWriter writer = null;
        try {
            File fout = new File("output_ab_non_lin.csv");
            writer = new PrintWriter(fout);

//            writer.println("Point" + ";" + "Me Func" + ";" + "Me Func x2" + ";" + "Difference");
            writer.println("Point"+ ";" + "Real Func" + ";" + "Me Func" + ";" + "Me Func x2");

            for (int i = 0; i < SIZE; i++) {
                String result = String.format("%.3f;%.3f;%.3f;%.3f;\n", (i * (x_end - x_start) / SIZE + 0.001),
                        z_real_ab[i][SIZE * 2 / 5], z_cross_2[i * 2][SIZE * 2 / 5],
                        z_cross[i][SIZE / 5]).replace('.', ',');
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
