package nsu.panova.Main;

import java.util.Scanner;

public class Lab4 {
    private double start;
    private double end;
    private int countOfPoint;
    private double h;
    private double[] y;

    public void work() {
        System.out.println("y(x) = - y'(x)");
        System.out.println("Введите отрезок для задачи");
        Scanner in = new Scanner(System.in);
        start = in.nextDouble();
        end = in.nextDouble();

        System.out.println("Введите количество точек");
        countOfPoint = in.nextInt();
        y = new double[countOfPoint];
        iteration();
    }

    private void iteration() {
        h = (end - start) / countOfPoint;
        System.out.println("h = " + h);

        y[0] = 1;
        for (int i = 1; i < countOfPoint; i++) {
            y[i] = (1 - h) * y[i - 1];
        }

        for (int i = 0; i < countOfPoint; i++) {
            System.out.println(/*"y_" + i + " = " + */ + y[i]);
        }
    }

}
