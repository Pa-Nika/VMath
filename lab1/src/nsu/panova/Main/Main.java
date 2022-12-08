package nsu.panova.Main;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Введите коэффициенты уравнения");
        double a = in.nextDouble();
        double b = in.nextDouble();
        double c = in.nextDouble();
        double d = in.nextDouble();

        Counter counter = new Counter(a, b, c, d);
        counter.searchAnswer();

        in.close();
    }


}
