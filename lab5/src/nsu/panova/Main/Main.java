package nsu.panova.Main;

import java.util.Scanner;

//  x = l * sin(x)

public class Main {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Введите коэффициент уравнения");
        double l = in.nextDouble();

        Executor exec = new Executor(l);
        exec.findAnswer();

        in.close();

    }
}
