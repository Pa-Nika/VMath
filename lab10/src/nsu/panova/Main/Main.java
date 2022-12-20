package nsu.panova.Main;

import nsu.panova.Main.NonLineral.CrossAB;
import nsu.panova.Main.NonLineral.CrossSin;
import nsu.panova.Main.NonLineral.ImplicitSin;

public class Main {

    public static void main(String[] args) {
	    CrossAll crossAll = new CrossAll();
        crossAll.work();

        Implicit implicit = new Implicit();
        implicit.work();

        CrossSin cross = new CrossSin();
        cross.work();

        ImplicitSin implicit1 = new ImplicitSin();
        implicit1.work();

        CrossAB crossAB = new CrossAB();
        crossAB.work();
    }
}
