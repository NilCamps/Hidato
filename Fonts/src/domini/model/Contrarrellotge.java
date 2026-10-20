package domini.model;

/**
 * @class Contrarrellotge
 * @brief Representa una partida en mode Contrarrellotge de Hidato.
 * @author Arnau Rodriguez Trocho
 *
 * Mode de joc amb temps límit configurable. Si el jugador no completa
 * el tablero abans que s'exhaureixi el temps, la partida acaba amb
 * una puntuació penalitzada proporcional al progrés assolit.
 *
 * Formula completat: (casellesBuides*10 - errors*5 + bonus_temps) * multiplicador
 *   on bonus_temps = (tempsRestant / tempsLimit) * casellesBuides * 5
 * Formula temps exhaurit: (casellesCompletades / casellesBuides) * casellesBuides * 5 * multiplicador
 */
public class Contrarrellotge extends Partida {

    /** @brief Temps límit de la partida en segons. */
    private long tempsLimitSegons;

    /** @brief Nombre d'errors comesos durant la partida. */
    private int errors;

    /** @brief Indica si la partida ha acabat per temps exhaurit. */
    private boolean tempsEsgotat;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    /**
     * @brief Crea una partida en mode Contrarrellotge.
     *
     * @param tbuit            Taulell inicial que veu el jugador.
     * @param tresolt          Taulell complet amb la solucio correcta.
     * @param tempsLimitSegons Temps límit en segons configurable per l'usuari.
     */
    public Contrarrellotge(Taulell tbuit, Taulell tresolt, long tempsLimitSegons) {
        super(tbuit, tresolt);
        this.tempsLimitSegons = tempsLimitSegons;
        this.errors = 0;
        this.tempsEsgotat = false;
    }

    // -------------------------------------------------------------------------
    // Implementació de metodes abstractes
    // -------------------------------------------------------------------------

    /**
     * @brief Aplica la penalitzacio per error en mode Contrarrellotge.
     *
     * Incrementa el comptador d'errors. La puntuacio es recalcula
     * al final via calcularPuntuacio().
     */
    @Override
    public void penalitzaError() {
        errors++;
    }

    /**
     * @brief Calcula la puntuacio final en mode Contrarrellotge.
     *
     * Si el temps s'ha esgotat, la puntuació es proporcional al progrés.
     * Si s'ha completat a temps, inclou bonus per temps restant.
     */
    @Override
    public void calcularPuntuacio() {
        if (tempsEsgotat) {
            int casellesCompletades = casellesBuidesInicials - NumerosDisponibles.size();
            float proporcio = (float) casellesCompletades / casellesBuidesInicials;
            puntuacio = proporcio * casellesBuidesInicials * 5 * d.getMultiplicador();
        } else {
            long tempsRestant = tempsLimitSegons - getTempsPartida().toSeconds();
            if (tempsRestant < 0) tempsRestant = 0;
            float bonusTemps = ((float) tempsRestant / tempsLimitSegons) * casellesBuidesInicials * 5;
            float base = casellesBuidesInicials * 10 - errors * 5 + bonusTemps;
            if (base < 0) base = 0;
            puntuacio = base * d.getMultiplicador();
        }
    }

    /**
     * @brief Mostra els resultats finals de la partida Contrarrellotge.
     */
    @Override
    public void treuResultats() {
        calcularPuntuacio();
        System.out.println("=== RESULTATS PARTIDA CONTRARRELLOTGE ===");
        if (tempsEsgotat) {
            System.out.println("Temps exhaurit!");
        } else {
            long tempsRestant = tempsLimitSegons - getTpartida().toSeconds();
            System.out.println("Temps restant: " + tempsRestant + "s");
        }
        System.out.println("Errors comesos: " + errors);
        System.out.println("Puntuacio final: " + puntuacio);
    }

    // -------------------------------------------------------------------------
    // Metodes específics de Contrarrellotge
    // -------------------------------------------------------------------------

    /**
     * @brief Comprova si el temps límit s'ha exhaurit.
     *
     * S'ha de cridar des del controlador després de cada moviment.
     *
     * @return Cert si el temps transcorregut supera el límit configurat.
     */
    public boolean comprovarTemps() {
        if (getTempsPartida().toSeconds() >= tempsLimitSegons) {
            tempsEsgotat = true;
        }
        return tempsEsgotat;
    }

    /**
     * @brief Marca explícitament la partida com acabada per temps esgotat.
     */
    public void marcarTempsEsgotat() {
        this.tempsEsgotat = true;
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    /**
     * @brief Retorna el temps límit configurat en segons.
     * @return Temps límit en segons.
     */
    public long getTempsLimitSegons() {
        return tempsLimitSegons;
    }

    /**
     * @brief Retorna el temps restant en segons.
     * @return Segons restants. Retorna 0 si el temps ja s'ha esgotat.
     */
    public long getTempsRestantSegons() {
        long restant = tempsLimitSegons - getTempsPartida().toSeconds();
        return Math.max(0, restant);
    }

    /**
     * @brief Indica si la partida ha acabat per temps esgotat.
     * @return Cert si el temps s'ha exhaurit.
     */
    public boolean isTempsEsgotat() {
        return tempsEsgotat;
    }

    /**
     * @brief Retorna el nombre d'errors comesos.
     * @return Nombre d'errors.
     */
    public int getErrors() {
        return errors;
    }
}