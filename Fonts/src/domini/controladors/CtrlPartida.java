package domini.controladors;

import domini.model.Dificultat;
import domini.model.Partida;
import domini.model.Taulell;

public abstract class CtrlPartida {

    /**
     * Crea i retorna una partida del tipus concret.
     */
    public abstract Partida jugar(Taulell t, Dificultat d);

    /**
     * Processa un moviment: valida número i posició, i aplica o penalitza.
     * (Aquest mètode sí que és útil per VistaJoc si el vols reutilitzar.)
     */
    public boolean processarMoviment(Partida p, int n, int x, int y) {
        if (!p.isNumberAvailable(n)) return false;
        if (!p.isPositionOk(x, y)) return false;

        if (p.comprovaSolucio(n, x, y)) {
            p.modificaHidato(n, x, y);
            return true;
        } else {
            p.penalitzaError();
            return false;
        }
    }
}
