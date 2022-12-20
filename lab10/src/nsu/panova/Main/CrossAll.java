package nsu.panova.Main;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import static java.lang.Math.abs;
import static java.lang.Math.sin;

public class CrossAll {
    private double[][] z_cross, z_real, z_cross_2;
    private double[][] z_cross_ab, z_real_ab, z_cross_ab_2;
    private double x_start, x_end, y_end, h, tau, a, r;
    private final int SIZE = 50;
    private final int SIZEY = 50;
    private final double a_ab = 3.0;
    private double b_ab = 1;


    public void work(){
        fillData();

        realSolution_sin();
        cross_sin();
        cross_sin_2();
        writeFirst();

        cross_ab();
        cross_ab_2();
        realSolutionAb();
        writeAb();
    }

    private double func_sin(double x) {
        if (x < 1)
            return 0;
        if (x < 4)
            return sin(3.1415 * (x - 1) / 3);
        return 0;
    }

    private double func_ab(double x) {
        if (x < 0)
          return a_ab;
        return b_ab;
    }

    private void realSolution_sin() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZEY; j++) {
                z_real[i][j] = func_sin((x_start + i * h) - a * (j * tau));
//                System.out.println("Real solution = " + z[i][j]);
            }
//            System.out.println("\n");
        }
    }

    private void cross_sin_2() {
        double new_h = h / 2;
        double new_tau = tau / 2;
        double x_h = x_start;
        for (int i = 0; i < SIZE * 2; i++) {
            for (int j = 0; j < SIZEY * 2; j++) {
                z_cross_2[i][j] = 0;
            }
        }

        //нулевой слой. Значем краевые значения из задачи Коши
        for(int i = 0; i < SIZE * 2; i++) {
            z_cross_2[i][0] = func_sin(x_h);
            x_h += new_h;
        }

        //Второй слой из правого уголка
        for (int i = 1; i < SIZE * 2 - 1; i++) {
            z_cross_2[i][1] = z_cross_2[i][0] * (1 + r) + z_cross_2[i + 1][0] * (r) * (-1);
        }

        //Остальные слои
        for(int i = 2; i < SIZEY * 2; i++) {
            for (int j = 1; j < SIZE * 2 - 1; j++) {
                z_cross_2[j][i] = z_cross_2[j][i - 2] - r * (z_cross_2[j + 1][i - 1] - z_cross_2[j - 1][i - 1]);
            }
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
        for(int i = 0; i < SIZE; i++) {
            z_cross[i][0] = func_sin(x_h);
            x_h += h;
        }

        //Второй слой из правого уголка
        for (int i = 1; i < SIZE - 1; i++) {
            z_cross[i][1] = z_cross[i][0] * (1 + r) + z_cross[i + 1][0] * (r) * (-1);
        }

        //Остальные слои
        for(int i = 2; i < SIZEY; i++) {
            for (int j = 1; j < SIZE - 1; j++) {
                z_cross[j][i] = z_cross[j][i - 2] - r * (z_cross[j + 1][i - 1] - z_cross[j - 1][i - 1]);
            }
        }
    }


    private void fillData() {
        x_start = 0;
        x_end = 10;
        y_end = 2;
        h = (x_end - x_start) / SIZE;
        System.out.println("h = " + h);
        tau = (y_end) / SIZEY;
        a = 2;

        if (a * tau / h <= 1 && a * tau / h >= 0) {
            r = a * tau / h;
            System.out.println("r = " + r);
        }
        else {
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
            File fout = new File("output_sin.csv");
            writer = new PrintWriter(fout);

            writer.println("Point"+ ";" + "Real Func" + ";" + "Me Func" + ";" + "Me Func x2" + ";" + "Difference");

            for (int i = 0; i < SIZE; i++) {
                String result = String.format("%.3f;%.3f;%.3f;%.3f;%.3f\n", (i * (x_end - x_start) / SIZE + 0.001), z_real[i][SIZE / 2], z_cross[i][SIZE / 2],
                        z_cross_2[i * 2][SIZE], abs(z_real[i][SIZE / 2] - z_cross[i][SIZE / 2])).replace('.', ',');
                writer.printf(result);
            }
            writer.close();
        } catch (IOException e){
            System.err.println("Error while writing file:" + e.getLocalizedMessage());
        }
        finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

















    private void cross_ab_2() {
        double new_h = h / 2;
        double x_h = x_start;
        for (int i = 0; i < SIZE * 2; i++) {
            for (int j = 0; j < SIZEY * 2; j++) {
                z_cross_ab_2[i][j] = 0;
                z_cross_ab_2[0][j] = a_ab;
                z_cross_ab_2[SIZE * 2 - 1][j] = b_ab;
            }
        }

        //нулевой слой. Значем краевые значения из задачи Коши
        for(int i = 0; i < SIZE * 2; i++) {
            z_cross_ab_2[i][0] = func_ab(x_h);
            x_h += new_h;
        }

        //Второй слой из правого уголка
        for (int i = 1; i < SIZE * 2 - 1; i++) {
            z_cross_ab_2[i][1] = z_cross_ab_2[i][0] * (1 + r) + z_cross_ab_2[i + 1][0] * (r) * (-1);
        }

        //Остальные слои
        for(int i = 2; i < SIZEY * 2; i++) {
            for (int j = 1; j < SIZE * 2 - 1; j++) {
                z_cross_ab_2[j][i] = z_cross_ab_2[j][i - 2] - r * (z_cross_ab_2[j + 1][i - 1] - z_cross_ab_2[j - 1][i - 1]);
            }
        }
    }


    private void cross_ab() {
        double x_h = x_start;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZEY; j++) {
                z_cross_ab[i][j] = 0;
                z_cross_ab[0][j] = a_ab;
                z_cross_ab[SIZE - 1][j] = b_ab;
            }
        }

        //нулевой слой. Значем краевые значения из задачи Коши
        for(int i = 0; i < SIZE; i++) {
            z_cross_ab[i][0] = func_ab(x_h);
            x_h += h;
        }

        //Второй слой из правого уголка
        for (int i = 1; i < SIZE - 1; i++) {
            z_cross_ab[i][1] = z_cross_ab[i][0] * (1 + r) + z_cross_ab[i + 1][0] * (r) * (-1);
        }

        //Остальные слои
        for(int i = 2; i < SIZEY; i++) {
            for (int j = 1; j < SIZE - 1; j++) {
                z_cross_ab[j][i] = z_cross_ab[j][i - 2] - r * (z_cross_ab[j + 1][i - 1] - z_cross_ab[j - 1][i - 1]);
            }
        }
    }

    private void realSolutionAb() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZEY; j++) {
                z_real_ab[i][j] = func_ab((x_start + i * h) - a * (j * tau));
//                System.out.println("Real solution = " + z[i][j]);
            }
//            System.out.println("\n");
        }
    }

    private void writeAb() {
        PrintWriter writer = null;
        try {
            File fout = new File("output_ab.csv");
            writer = new PrintWriter(fout);

            writer.println("Point"+ ";" + "Real Func" + ";" + "Me Func" + ";" + "Me Func x2" + ";" + ";Point;Difference1" + ";" + "Difference2");

            for (int i = 0; i < SIZE; i++) {
                String result = String.format("%.3f;%.3f;%.3f;%.3f;;%.3f;%.3f;%.3f;\n", (i * (x_end - x_start) / SIZE + 0.001), z_real_ab[i][SIZE / 2], z_cross_ab[i][SIZE / 2],
                        z_cross_ab_2[i * 2][SIZE], (i * (x_end - x_start) / SIZE + 0.001), abs(z_real_ab[i][SIZE / 2] - z_cross_ab[i][SIZE / 2]),
                        abs(z_real_ab[i][SIZE / 2] - z_cross_ab_2[i*2][SIZE])).replace('.', ',');
                writer.printf(result);
            }
            writer.close();
        } catch (IOException e){
            System.err.println("Error while writing file:" + e.getLocalizedMessage());
        }
        finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
}
