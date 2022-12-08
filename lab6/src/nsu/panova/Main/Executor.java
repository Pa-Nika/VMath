package nsu.panova.Main;

import static java.lang.Math.abs;
import static java.lang.Math.cos;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class Executor {
    private final double lenStart;
    private final double lenEnd;
    private double point;
    private int countOfPoints;
    private double delta;

    public Executor(double lenStart, double lenEnd, int count, double point) {
        this.lenStart = lenStart;
        this.lenEnd = lenEnd;
        this.countOfPoints = count;
        this.point = point;
        delta = (lenEnd - lenStart) / (countOfPoints - 1);
    }

    //3x − cos(x) − 1
    private double calcFunc(double x) {
        return 3 * x - cos(x) - 1;
    }

    private double calcXI(int i) {
        double x = lenStart;
        for (int j = 0; j < i; j++) {
            x += delta;
        }
        return x;
    }

    public void findAnswer() {
        double xFunc = calcFunc(point);
        double xInter = counter(point);
        System.out.println("\nАбсолютная погрешность при данном количестве узлов = " + (xFunc - xInter));
        System.out.println("\tЗначение через полином = " + xInter + "\n\tЗначение через функцию = " + xFunc);


        PrintWriter writer = null;
        try {
            File fout = new File("output.csv");
            writer = new PrintWriter(fout);

            writer.println("Point"+ ";" + "Text Func" + ";" + "Me Func" + ";" + "Difference");

            for (double i = lenStart; i < countOfPoints - 1; i += delta) {
                xInter = counter(i);
                xFunc = calcFunc(i);
                String result = String.format("%.3f;%.3f;%.3f;%.3f\n", (i + 0.001), xInter, xFunc, abs(xInter - xFunc)).replace('.', ',');
                writer.printf(result);
                //System.out.println("AAAAAAA" + i);
            }

            writer.close();
        } catch (IOException e){
            System.err.println("Error while writing file:" + e.getLocalizedMessage());
        }
        finally {
            if (writer != null) {
                writer.close();
            }
        }



        countOfPoints *= 2;
        delta = (lenEnd - lenStart) / countOfPoints;
        xInter = counter(point);
        xFunc = calcFunc(point);

        System.out.println("\nАбсолютная погрешность при удвоенном количестве узлов = " + (xFunc - xInter));
        System.out.println("\tЗначение через полином = " + xInter + "\n\tЗначение через функцию = " + xFunc);

    }

    private double counter(double x) {
        double Ln = 0.0;
        double tmp = 1;
        for (int i = 0; i < countOfPoints; i++) {
            for (int j = 0; j < countOfPoints; j++) {
                if (j != i) {
                    tmp *= (x - calcXI(j)) / (calcXI(i) - calcXI(j));
                }
            }

            tmp *= calcFunc(calcXI(i));
            Ln += tmp;
            tmp = 1;
        }

        return Ln;
    }
}



