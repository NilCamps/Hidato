package domini.controladors;

import domini.model.Dificultat;
import domini.model.Taulell;
import domini.model.Taulell.TAdjacencia;
import domini.model.Taulell.TForma;
import java.time.Duration;

public class CtrlConfiguracio {

    public enum TPartida {
        NORMAL,
        SUPERVIVENCIA,
        CONTRARRELLOTGE
    }

    private static final int MIN_CASELLES_FACIL   = 9;
    private static final int MIN_CASELLES_MITJA   = 16;
    private static final int MIN_CASELLES_DIFICIL = 25;

    private Dificultat d;
    private TForma forma;
    private TAdjacencia adjacencia;
    private TPartida tipusPartida;
    private Duration timecontra;
    private int codiHidato;
    private int nCaselles;

    public CtrlConfiguracio() {}

    public void configurar(TPartida mode, int dificultatIdx, TForma forma, TAdjacencia adj, int nCaselles, Integer tempsMinuts, int codiHidato) {
        this.tipusPartida = mode;
        Dificultat dif = new Dificultat();
        switch (dificultatIdx) {
            case 0: dif.modificaDificultat(5, true, 1.0f);  break;
            case 1: dif.modificaDificultat(3, true, 1.5f);  break;
            case 2: dif.modificaDificultat(0, false, 2.0f); break;
        }
        this.d = dif;
        this.forma = forma;
        this.adjacencia = adj;
        this.nCaselles = nCaselles;
        if (mode.equals(TPartida.CONTRARRELLOTGE) && tempsMinuts != null) {
            this.timecontra = Duration.ofMinutes(tempsMinuts);
        } else {
            this.timecontra = null;
        }
        this.codiHidato = codiHidato;
    }

    public Dificultat getD() { return d; }
    public TForma getForma() { return forma; }
    public TAdjacencia getAdjacencia() { return adjacencia; }
    public TPartida getTipusPartida() { return tipusPartida; }
    public Duration getTimecontra() { return timecontra; }
    public int getCodiHidato() { return codiHidato; }
    public int getNCaselles() { return nCaselles; }

    public int getMinCasellesPerDificultat() {
        float m = d.getMultiplicador();
        if (m == 1.0f) return MIN_CASELLES_FACIL;
        if (m == 1.5f) return MIN_CASELLES_MITJA;
        return MIN_CASELLES_DIFICIL;
    }
}
