//package nsu.panova.Main.NonLineral;
//
//import nsu.panova.Main.RunThrough;
//
//import java.io.File;
//import java.io.IOException;
//import java.io.PrintWriter;
//
//import static java.lang.Math.abs;
//import static java.lang.Math.sin;
//
//public class Implicit {
//    private double[][] z_impl, z_real, z_impl_2;
//    private double[][] z_impl_ab, z_real_ab, z_impl_ab_2;
//    private double x_start, x_end, y_end, h, tau, a, r;
//    private int SIZE = 50;
//    private int SIZEY;
//    private final double a_ab = 3.0;
//    private double b_ab = 1;
//    private double[] x;
//
//    public void work() {
//        fillData();
//
//        realSolution_sin();
//        impl_sin();
//        impl_sin_2();
//        writeFirst();
//    }
//
//    private void fillData() {
//        SIZEY = SIZE;
//        x_start = 0;
//        x_end = 10;
//        y_end = 2;
//        h = (x_end - x_start) / SIZE;
//        System.out.println("h = " + h);
//        tau = (y_end) / SIZEY;
//        System.out.println("tau = " + tau);
//        a = 2;
//
//        if (a * tau / h <= 1 && a * tau / h >= 0) {
//            r = a * tau / h;
//            System.out.println("r = " + r);
//        } else {
//            System.out.println("Поменяйте параметры, пожалуйста");
//            System.exit(0);
//        }
//
//        z_impl = new double[SIZE][SIZEY];
//        z_impl_2 = new double[SIZE * 2][SIZEY * 2];
//        z_real = new double[SIZE][SIZEY];
//
//        z_impl_ab = new double[SIZE][SIZEY];
//        z_impl_ab_2 = new double[SIZE * 2][SIZEY * 2];
//        z_real_ab = new double[SIZE][SIZEY];
//
//        x = new double[3];
//    }
//
//    private double func_sin(double x) {
//        if (x < 1)
//            return 0;
//        if (x < 4)
//            return sin(3.1415 * (x - 1) / 3);
//        return 0;
//    }
//
//    private double func_ab(double x) {
//        if (x < 0)
//            return a_ab;
//        return b_ab;
//    }
//
//    private void realSolution_sin() {
//        for (int i = 0; i < SIZE; i++) {
//            for (int j = 0; j < SIZEY; j++) {
//                z_real[i][j] = func_sin((x_start + i * h) - a * (j * tau));
//            }
//        }
//    }
//
//
//    private void impl_sin_2() {
//        double new_h = h / 2;
//        RunThrough run = new RunThrough();
//        double x_h = x_start;
//        for (int i = 0; i < SIZE * 2; i++) {
//            for (int j = 0; j < SIZEY * 2; j++) {
//                z_impl_2[i][j] = 0;
//            }
//        }
//
//        //нулевой слой. Значем краевые значения из задачи Коши
//        for (int i = 0; i < SIZE * 2; i++) {
//            z_impl_2[i][0] = func_sin(x_h);
//            x_h += new_h;
//        }
//
//        //Остальные слои
//        for (int j = 1; j < SIZEY * 2 - 1; j++) {
//            for (int i = 1; i < SIZE * 2 - 1; i++) {
//                z_impl_2[i][j] = run.work(r, z_impl_2[i - 1][j - 1], z_impl_2[i][j - 1], z_impl_2[i + 1][j - 1]);
//            }
//        }
//    }
//
//    private void impl_sin() {
//        RunThrough run = new RunThrough();
//        double x_h = x_start;
//        for (int i = 0; i < SIZE; i++) {
//            for (int j = 0; j < SIZEY; j++) {
//                z_impl[i][j] = 0;
//            }
//        }
//
//        //нулевой слой. Значем краевые значения из задачи Коши
//        for (int i = 0; i < SIZE; i++) {
//            z_impl[i][0] = func_sin(x_h);
//            System.out.println(z_impl[i][0]);
//            x_h += h;
//        }
//
//        //Остальные слои
//        for (int j = 1; j < SIZE - 1; j++) {
//            for (int i = 1; i < SIZEY - 1; i++) {
//                z_impl[i][j] = run.work(r, z_impl[i - 1][j - 1], z_impl[i][j - 1], z_impl[i + 1][j - 1]);
//            }
//        }
//    }
//
//    private void writeFirst() {
//        this.SIZE = 2;
//        PrintWriter writer = null;
//        try {
//            File fout = new File("output_sin_impl.csv");
//            writer = new PrintWriter(fout);
//
//            writer.println("Point" + ";" + "Real Func" + ";" + "Me Func" + ";" + "Me Func x2" + ";" + "Difference");
//
//            for (int i = 0; i < SIZEY; i++) {
//                String result = String.format("%.3f;%.3f;%.3f;%.3f;%.3f\n", (i * (x_end - x_start) / SIZEY + 0.001), z_real[i][SIZE / 2], z_impl_2[i * 2][SIZE],
//                        z_impl[i][SIZE / 2], abs(z_real[i][SIZE / 2] - z_impl_2[i * 2][SIZE])).replace('.', ',');
//                writer.printf(result);
//            }
//            writer.close();
//            SIZE = SIZEY;
//        } catch (IOException e) {
//            System.err.println("Error while writing file:" + e.getLocalizedMessage());
//        } finally {
//            if (writer != null) {
//                writer.close();
//            }
//        }
//    }
//
//}
