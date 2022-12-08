package nsu.panova.Main;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        System.out.println("Heeeey! Let's do it!");
        Executor executor = new Executor();
        try {
            executor.work();
        } catch (IOException e) {
            System.out.println("Can't find file");
        }
    }
}
