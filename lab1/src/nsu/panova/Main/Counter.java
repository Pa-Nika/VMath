package nsu.panova.Main;

import static java.lang.Math.*;

public class Counter {

    private double a;
    private double b;
    private double c;
    private double d;
    private double discr;
    private final double epsilon = pow(10, -10);

    Counter(double a, double b, double c, double d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        countDiscriminant();
    }

    private void countDiscriminant() {
        discr = (4 * b * b - 12 * a * c);
    }

    public void searchAnswer() {
        if (discr <= 0) {
            monotonousFunc();
        }
        else {
            normalizeOdds();
            notMonotonous();
        }
    }

    private void normalizeOdds() {
        if (a != 0) {
            this.b = b / a;
            this.c = c / a;
            this.d = d / a;
            this.a = 1;
            countDiscriminant();
        }
        if (a == 0) {
            discr = c * c - 4 * b * d;
            if (discr < 0) {
                System.out.println("Нет решений");
            }
            if (discr == 0) {
                System.out.println("Корень: x = " + ((-1) * c / 2 * b));
            }
            if (discr > 0) {
                System.out.println("Два корня\n Корень: x = " + ((-1) * c + sqrt(discr)) / (2 * b) +
                        "\n Корень: x = "+ ((-1) * c - sqrt(discr)) / (2 * b));
            }
            System.exit(0);
        }
    }

    private double countFunc(double x) {
        return (a * pow(x, 3) + b * pow(x, 2) + c * x + d);
    }

    private void findAnswer(double start, double end) {
        double alpha, beta;
        double newBorder;
        alpha = countFunc(start);
        beta = countFunc(end);
        newBorder = (start + end) / 2;

        double answer = countFunc(newBorder);

        if (alpha < 0 && beta > 0) {
            while (abs(answer) >= epsilon) {
                if (answer > 0) {
                    end = newBorder;
                }
                if (answer < 0) {
                    start = newBorder;
                }

                newBorder = (start + end) / 2;
                answer = countFunc(newBorder);
            }

            System.out.println("Корень: x = " + newBorder);
        }
        else if (alpha > 0 && beta < 0) {
            while (abs(answer) >= epsilon) {
                if (answer < 0) {
                    end = newBorder;
                }
                if (answer > 0) {
                    start = newBorder;
                }

                newBorder = (start + end) / 2;
                answer = countFunc(newBorder);
            }
            System.out.println("Корень: x = " + newBorder);
        }
    }

    private void countWithDelta(double a, int signDelta, int signRoot) {
        double delta = 0.1;
        double root = signRoot * countFunc(a + delta * (signDelta));
        while(root <= 0) {
            a = a + delta * (signDelta);
            root = signRoot * countFunc(a + delta * (signDelta));
        }
        if (a < a + delta * (signDelta)) {
            System.out.println("Корень находится на интервале [" + a + " ; " + (a + delta * (signDelta)) + "]");
            findAnswer(a, a + delta * (signDelta));
        }
        else {
            System.out.println("Корень находится на интервале [" + (a + delta * (signDelta))+ " ; " + a + "]");
            findAnswer(a + delta * (signDelta), a);
        }
    }

    private void monotonousFunc() {
        if (a < 0) {
            System.out.println("Функция монотонно убывает. Будет только один корень");
            double root = countFunc(0);

            if (abs(root) < epsilon) {
                System.out.println("Корень: x = 0");
            }
            else if (root > 0) {
                System.out.println("Корень находится на интервале [0; +inf)");
                countWithDelta(0,1, -1);
            }
            else if (root < 0) {
                System.out.println("Корень находится на интервале (-inf; 0]");
                countWithDelta(0,-1, 1);
            }
        }

        if (a > 0) {
            System.out.println("Функция монотонно возрастает. Будет только один корень");
            double root = countFunc(0);

            if (abs(root) < epsilon) {
                System.out.println("Корень: x = 0");
            }
            else if (root < 0) {
                System.out.println("Корень находится на интервале [0; +inf)");
                countWithDelta(0,1, 1);
            }
            else if (root > 0) {
                System.out.println("Корень находится на интервале (-inf; 0]");
                countWithDelta(0,-1 ,-1);
            }
        }
    }

    private void notMonotonous() {
        double zeroOfDerivative1 = (-2 * b - sqrt(discr)) / 6 * a ;
        double zeroOfDerivative2 = (-2 * b + sqrt(discr)) / 6 * a ;

        if (zeroOfDerivative2 < zeroOfDerivative1) {
            double tmp = zeroOfDerivative1;
            zeroOfDerivative1 = zeroOfDerivative2;
            zeroOfDerivative2 = tmp;
        }

        double alpha = countFunc(zeroOfDerivative1);
        double beta = countFunc(zeroOfDerivative2);

        if (abs(alpha) < epsilon) {
            if (abs(beta) < epsilon) {
                System.out.println("Функция имеет один корень: x = " + (zeroOfDerivative1 + zeroOfDerivative2) / 2);
            }
            if (beta < 0) {
                System.out.println("Функция имеет 2 корня. Первый - " + zeroOfDerivative1 + ". " +
                        "Второй - на интервале [" + zeroOfDerivative2 + "; +inf)");
                countWithDelta(zeroOfDerivative2, 1, 1);
            }
        }
        if (alpha > 0) {
            if (abs(beta) < epsilon) {
                System.out.println("Функция имеет 2 корня. Первый - " + zeroOfDerivative2 +
                        ". Второй - на интервале (-inf; " + zeroOfDerivative1 + "]");
                countWithDelta(zeroOfDerivative1, -1, -1);
            }

            if (beta > 0) {
                System.out.println("Функция имеет корень на интервале (-inf; " + zeroOfDerivative1 + "]");
                countWithDelta(zeroOfDerivative1, -1, -1);
            }

            if (beta < 0) {
                System.out.println("Функция имеет 3 корня.\n Первый - на интервале (-inf; " + zeroOfDerivative1 + "]\n" +
                        " Второй - на интервале [" + zeroOfDerivative1 + "; " + zeroOfDerivative2 + "]\n" +
                        " Третий - на интервале [" + zeroOfDerivative2 + "; +inf)");
                countWithDelta(zeroOfDerivative1, -1, -1);
                countWithDelta(zeroOfDerivative1, 1, -1);
                countWithDelta(zeroOfDerivative2, 1, 1);
            }
        }
        if (alpha < 0) {
            if (beta < 0) {
                System.out.println("Функция имеет корень на интервале [" + zeroOfDerivative2 + "; +inf)");
                countWithDelta(zeroOfDerivative2, 1, 1);
            }
        }
    }
}
