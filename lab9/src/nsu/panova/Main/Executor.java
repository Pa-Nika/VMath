package nsu.panova.Main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import static java.lang.Math.*;

// g(x) = e^x ∗ cos(x); g(0) = 1; y(0) = 0
//y'=g(x);
//y = (sin(x) + cos(x) ) * exp(x) - 1) / 2

public class Executor {
    private double start, end, h;
    private int n;
    private double[] x, y, yReal, ySecond, y_h, ySecond_h, x_h;
    PrintWriter writer = null;

    public void work() throws FileNotFoundException {
        fillData();
        memData();
        startInitial();
        count();
        writeAnswer();
        write2();
        writer.close();
    }

    private void memData() throws FileNotFoundException {
        x = new double[n];
        x_h = new double[2 * n];
        y = new double[n];
        y_h = new double[2 * n];
        yReal = new double[n];
        ySecond = new double[n];
        ySecond_h = new double[2 * n];
        File fout = new File("output.csv");
        writer = new PrintWriter(fout);
    }

    private void fillData() {
        System.out.println("Введите начало и конец отрезка");
        Scanner scanner = new Scanner(System.in);
        start = scanner.nextDouble();
        end = scanner.nextDouble();

        System.out.println("Введите количество точек (N)");
        n = scanner.nextInt();
        h = (end - start) / n;
    }

    private void startInitial() {
        x[0] = x_h[0] = start;
        y[0] = yReal[0] = ySecond[0] = 0;
        y_h[0] = ySecond_h[0] = 0;
    }

    private void count() {
        for(int i = 1; i < n; i++) {
            x[i] = x[i - 1] + h;
            yReal[i] = ((sin(x[i]) + cos(x[i])) * exp(x[i]) - 1) / 2;
            if (i < 2) {
                //y[i] = h * g(x[i]);
                y[i] = 2 * h * g(x[i - 1]);
                ySecond[i] = h * (g(x[i]) + 4 * g(x[i - 1])) / 3;
            } else {
                y[i] = h * (g(x[i - 2]) + g(x[i])) + y[i - 2];
                ySecond[i] = h * (g(x[i]) + 4 * g(x[i - 1]) + g(x[i - 2])) / 3 + ySecond[i - 2];
                //y[i] = 2 * h * g(x[i - 1]) + y[i - 2];
            }

            //System.out.println(x[i] + " " + yReal[i] + " " + y[i] + " " + ySecond[i]);
        }

        h = h / 2;
        for(int i = 1; i < 2*n - 1; i++) {
            x_h[i] = x_h[i - 1] + h;
            if (i < 2) {
                //y_h[i] = h * g(x_h[i]);
                //y[i] = 2 * h * g(x[i - 1]);
                ySecond_h[i] = h * (g(x_h[i]) + 4 * g(x_h[i - 1])) / 3;
            } else {
                y_h[i] = h * (g(x_h[i - 2]) + g(x_h[i])) + y_h[i - 2];
                ySecond_h[i] = h * (g(x_h[i]) + 4 * g(x_h[i - 1]) + g(x_h[i - 2])) / 3 + ySecond_h[i - 2];
                //y[i] = 2 * h * g(x[i - 1]) + y[i - 2];
            }
            System.out.println(y_h[i] + "\t" + ySecond_h[i]);
        }

        System.out.println(" \n" + 2 * h * g(x[0]));
        //double r = abs(y[399] - y_h[798]) / (pow(2, 2) - 1);
        //System.out.println("Примерный порядок сходимости = " + r + " " + y[399] + " " + y_h[798]);

    }


    private void write2() {
        writer.println("Point_2"+ ";" + "Real Func" + ";" + "Me Func (1)" + ";" + "My Func (2)");
        int k = 1;
        y[k]  = h * g(x[k]); //1
        ySecond[k] = ySecond[k + 1] - (8 * g(x[k + 1]) + 5 * g(x[k]) - g(x[k + 2])) * h / 12; //3
        for(int i = 0; i < n; i++) {
            String result = String.format("%.3f;%.3f;%.3f;%.3f\n", x[i], yReal[i], y[i], ySecond[i]);
            writer.printf(result);
        }
    }

    private double g(double x) {
        return exp(x) * cos(x);
    }

    private void writeAnswer() {
        writer.println("Point"+ ";" + "Real Func" + ";" + "Me Func (1)" + ";" + "My Func (2)");
        for(int i = 0; i < n; i++) {
            String result = String.format("%.3f;%.3f;%.3f;%.3f\n", x[i], yReal[i], y[i], ySecond[i]);
            writer.printf(result);
        }
    }


}
