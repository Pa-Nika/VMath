package nsu.panova.Main;
import java.util.Scanner;

//3x − cos(x) − 1
public class Main {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Введите исследуемый интервал");
        double lenStart = in.nextDouble();
        double lenEnd = in.nextDouble();
        if (lenStart > lenEnd) {
            System.out.println("Введите в порядке возрастания значений");
            return;
        }

        System.out.println("Введите количество узлов");
        int count = in.nextInt();
        if (count <= 0) {
            System.out.println("Неправильное количество узлов");
            return;
        }

        System.out.println("Введите точку, где будем определять погрешность");
        double point = in.nextDouble();
        if (point < lenStart || point > lenEnd) {
            System.out.println("Неправильно введена точка относительно интервала");
            return;
        }

        Executor exec = new Executor(lenStart, lenEnd, count, point);
        exec.findAnswer();
        in.close();
    }
}
