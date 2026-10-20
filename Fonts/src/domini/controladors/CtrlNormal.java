package domini.controladors;

import domini.model.Dificultat;
import domini.model.Normal;
import domini.model.Partida;
import domini.model.Taulell;
import domini.algorismes.SolucionarHidato;

public class CtrlNormal extends CtrlPartida {

    @Override
    public Partida jugar(Taulell t, Dificultat d) {
        SolucionarHidato sh = new SolucionarHidato();
        Taulell tResolt = sh.SolucionarHidatoTaulell(t);

        Normal partida = new Normal(t, tResolt);
        partida.setDificultat(d);
        return partida;
    }
}
