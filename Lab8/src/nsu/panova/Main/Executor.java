package nsu.panova.Main;

import static java.lang.Math.*;

public class Executor {
    private final double delta;
    private static final int A = 0;
    private static final int B = 2;

    public Executor(int count) {
        delta = (double) 2 / count;
    }

    public void findAnswer() {
        System.out.println("Формула трапеции");
        trapCalc();

        System.out.println("Формула парабол");
        parabCalc();
    }

    private double calcFunc(double point) {
        return log(1 + point);
    }

    //  max(-1 / (1 + x)^2)  при x == 2
    private double calcDerivative2() {
        return abs((double) -1 / 9);
    }

    //  max(-6 / (1 + x)^4) при x == 2
    private double calcDerivative4() {
        return abs((-6) / (pow(1 + B, 4)));
    }

    private void trapCalc() {
        double a = A;
        double answer = 0;
        for (double b = delta; b <= B; b += delta) {
            answer += (b - a) * ((calcFunc(a) + calcFunc(b)) / 2);
            a = b;
        }
        System.out.println(answer);

        double error = calcDerivative2() * pow((B - A), 3) / 12;
        System.out.println("\t Пошрешность " + error);
    }

    private void parabCalc() {
        double answer = A;
        double a = 0;
        for (double b = delta; b <= B; b+= delta) {
            double tmp = calcFunc(a) + calcFunc(b) + 4 * calcFunc((a + b) / 2);
            answer += tmp * ((b - a) / 6);
            a = b;
        }
        System.out.println(answer);

        double error = calcDerivative4() * pow((B - A), 5) / 2880;
        System.out.println("\t Пошрешность " + error);
    }
}
