package nsu.panova.Main;

import static java.lang.Math.*;

public class Executor {
    double a;
    private static final double eps = pow(10, -9);

    Executor(double a) {
        this.a = a;
    }

    public void findAnswer() {
        if (a < 0) {
            System.out.println("Нет решений");
            return;
        }
        if (a == 0) {
            System.out.println("Один корень: x = 0");
            return;
        }
        System.out.println("Будет два корня\n");
        findLeftRoot(0);
        findRightRoot(0);
    }

    private void findLeftRoot(double x) {
        //  x = exp(x - a) - 1
        double value = exp(x - a) - 1;
        int i = 1;
        while (abs(x - value) >= eps) {
            x = value;
            value = exp(x - a) - 1;
            i++;
        }
        System.out.println("Корень: x = " + x);
        System.out.println("Количество итераций: " + i);
    }

    private void findRightRoot(double x) {
        //  x = ln(x + 1) + a
        double value = log(x + 1) + a;
        int i = 1;
        while (abs(x - value) >= eps) {
            x = value;
            value = log(x + 1) + a;
            i++;
        }

        System.out.println("Корень: x = " + x);
        System.out.println("Количество итераций: " + i);
    }

}


