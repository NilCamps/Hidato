package domini.controladors;

import domini.model.Contrarrellotge;
import domini.model.Dificultat;
import domini.model.Partida;
import domini.model.Taulell;
import domini.algorismes.SolucionarHidato;
import java.time.Duration;

public class CtrlContrarrellotge extends CtrlPartida {

    public Partida jugar(Taulell t, Dificultat d, Duration tempsLimit) {
        SolucionarHidato sh = new SolucionarHidato();
        Taulell tResolt = sh.SolucionarHidatoTaulell(t);

        Contrarrellotge partida = new Contrarrellotge(t, tResolt, tempsLimit.toSeconds());
        partida.setDificultat(d);
        return partida;
    }

    @Override
    public Partida jugar(Taulell t, Dificultat d) {
        return jugar(t, d, Duration.ofMinutes(10));
    }
}
