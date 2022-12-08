package nsu.panova.Main;

import java.util.Scanner;

//  x − a = ln(1 + x)
public class Main {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Введите коэффициент уравнения");
        double a = in.nextDouble();

        Executor exec = new Executor(a);
        exec.findAnswer();

        in.close();
    }
}
