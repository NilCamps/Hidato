package domini.algorismes;

import domini.model.*;

public class SolucioUnica {
    public static boolean teSolucioUnica(Taulell t) {
        SolucionarHidato sh = new SolucionarHidato();
        return sh.solucionarHidatoBoolean(t);
    }
}