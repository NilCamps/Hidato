package domini.controladors;

import domini.model.Dificultat;
import domini.model.Partida;
import domini.model.Supervivencia;
import domini.model.Taulell;
import domini.algorismes.SolucionarHidato;

public class CtrlSupervivencia extends CtrlPartida {

    @Override
    public Partida jugar(Taulell t, Dificultat d) {
        SolucionarHidato sh = new SolucionarHidato();
        Taulell tResolt = sh.SolucionarHidatoTaulell(t);

        Supervivencia partida = new Supervivencia(t, tResolt);
        partida.setDificultat(d);
        return partida;
    }
}
