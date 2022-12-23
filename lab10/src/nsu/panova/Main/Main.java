package nsu.panova.Main;

import nsu.panova.Main.NonLineral.CrossAB;
import nsu.panova.Main.NonLineral.CrossSin;
import nsu.panova.Main.NonLineral.ImplicitAB;
import nsu.panova.Main.NonLineral.ImplicitSin;

public class Main {

    public static void main(String[] args) {
	    CrossAll crossAll = new CrossAll();
        crossAll.work();

        Implicit implicit = new Implicit();
        implicit.work();

        CrossSin cross = new CrossSin();
        cross.work();

        CrossAB crossAB = new CrossAB();
        crossAB.work();

        ImplicitSin implicitSin = new ImplicitSin();
        implicitSin.work();

        ImplicitAB implicitAB = new ImplicitAB();
        implicitAB.work();
    }
}
