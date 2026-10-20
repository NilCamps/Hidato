package domini.model;

/**
 * @class Normal
 * @brief Representa una partida en mode normal de Hidato.
 * @author Arnau Rodriguez Trocho
 *
 * Mode de joc estàndard sense límit de temps ni vides.
 * La puntuació es calcula a partir de les caselles buides inicials i els errors comesos.
 *
 * Formula: (casellesBuides * 10 - errors * 5) * multiplicadorDificultat
 */
public class Normal extends Partida {

    /** @brief Nombre d'errors comesos durant la partida. */
    private int errors;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    /**
     * @brief Crea una partida en mode Normal.
     *
     * @param tbuit   Taulell inicial que veu el jugador.
     * @param tresolt Taulell complet amb la solucio correcta.
     */
    public Normal(Taulell tbuit, Taulell tresolt) {
        super(tbuit, tresolt);
        this.errors = 0;
    }

    // -------------------------------------------------------------------------
    // Implementació de metodes abstractes
    // -------------------------------------------------------------------------

    /**
     * @brief Aplica la penalitzacio per error en mode Normal.
     *
     * Incrementa el comptador d'errors. La puntuacio es recalcula
     * al final via calcularPuntuacio().
     */
    @Override
    public void penalitzaError() {
        errors++;
    }

    /**
     * @brief Calcula la puntuacio final en mode Normal.
     *
     * Formula: (casellesBuidesInicials * 10 - errors * 5) * multiplicador
     * El resultat minim es 0.
     */
    @Override
    public void calcularPuntuacio() {
        float base = casellesBuidesInicials * 10 - errors * 5;
        if (base < 0) base = 0;
        puntuacio = base * d.getMultiplicador();
    }

    /**
     * @brief Mostra els resultats finals de la partida Normal.
     *
     * Calcula la puntuacio i imprimeix el temps i la puntuacio obtinguda.
     */
    @Override
    public void treuResultats() {
        calcularPuntuacio();
        System.out.println("=== RESULTATS PARTIDA NORMAL ===");
        System.out.println("Temps de partida: " + getTpartida().toMinutes() + "m " + (getTpartida().toSeconds() % 60) + "s");
        System.out.println("Errors comesos: " + errors);
        System.out.println("Puntuacio final: " + puntuacio);
    }

    // -------------------------------------------------------------------------
    // Getter
    // -------------------------------------------------------------------------

    /**
     * @brief Retorna el nombre d'errors comesos durant la partida.
     * @return Nombre d'errors.
     */
    public int getErrors() {
        return errors;
    }
}