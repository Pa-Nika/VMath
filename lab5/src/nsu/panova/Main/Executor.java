package nsu.panova.Main;

import java.util.ArrayList;

import static java.lang.Math.*;

public class Executor {
    private final double l;
    private static final double eps = pow(10, -5);

    Executor(double l) {
        this.l = l;
    }

    public void findAnswer() {
        if (l == 1) {
            System.out.println("Уравнение имеет один корень\nКорень: x = 0");
            return;
        }
        System.out.println("Корень: x = 0");
        counter();
    }

    private double func(double x) {
        return (x - l * sin(x));
    }

    private void counter() {
        int k = 0;
        double delta = 1;
        boolean flag = true;
        double x = pow(10, -5);
        double step = delta;
        double secondX = delta + x;
        double answer = Double.MAX_VALUE;
        ArrayList<Double> lastAnswer = new ArrayList<>();

        lastAnswer.add(0.0);

        while (step <= l - 1) {
            while (flag) {
                answer = secondX - func(secondX) * ((secondX - x) / (func(secondX) - func(x)));
                if (abs(answer - secondX) >= eps) {
                    double tmp = secondX;
                    secondX = answer;
                    x = tmp;
                }
                else {
                    flag = false;
                }
            }


            boolean isAnswer = true;

            for (Double aDouble : lastAnswer) {
                if (abs(aDouble - answer) <= eps || abs(aDouble + answer) <= eps) {
                    k++;
                    isAnswer = false;
                    break;
                }
            }
            if (isAnswer) {
                System.out.println("Корень: x = " + answer);
                System.out.println("Корень: x = " + (-1) * answer + "\n");
                lastAnswer.add(answer);
            }

            x = step + pow(10, -5);
            step += delta;
            answer = Double.MAX_VALUE;
            secondX = step + pow(10, -5);
            flag = true;
        }

        delta = 0.01;
        while (step <= l) {
            while (flag) {
                answer = secondX - func(secondX) * ((secondX - x) / (func(secondX) - func(x)));
                if (abs(answer - secondX) >= eps) {
                    double tmp = secondX;
                    secondX = answer;
                    x = tmp;
                }
                else {
                    flag = false;
                }
            }

            boolean isAnswer = true;

            for (Double aDouble : lastAnswer) {
                if (abs(aDouble - answer) <= eps || abs(aDouble + answer) <= eps) {
                    k++;
                    isAnswer = false;
                    break;
                }
            }
            if (isAnswer) {
                System.out.println("Корень: x = " + answer);
                System.out.println("Корень: x = " + (-1) * answer + "\n");
                lastAnswer.add(answer);
            }

            x = step + pow(10, -5);
            step += delta;
            answer = Double.MAX_VALUE;
            secondX = step + pow(10, -5);
            flag = true;
        }
    }
}
