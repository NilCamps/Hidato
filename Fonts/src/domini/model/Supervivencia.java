package domini.model;

/**
 * @class Supervivencia
 * @brief Representa una partida en mode Supervivència de Hidato.
 * @author Arnau Rodriguez Trocho
 *
 * Mode de joc infinit on el jugador va completant tableros consecutius.
 * La partida acaba quan el jugador perd totes les vides (una per error).
 * La puntuació s'acumula al llarg de tots els tableros completats.
 *
 * Formula: sum(casellesBuides_tablero_i * 10) * multiplicadorDificultat
 */
public class Supervivencia extends Partida {

    /** @brief Nombre de vides restants. */
    private int nvides;

    /** @brief Nombre de tableros completats durant la partida. */
    private int taulerllsCompletats;

    /** @brief Puntuació acumulada dels tableros completats fins ara. */
    private float puntuacioAcumulada;

    /** @brief Nombre maxim de vides. */
    private static final int VIDES_INICIALS = 3;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    /**
     * @brief Crea una partida en mode Supervivència.
     *
     * @param tbuit   Taulell inicial que veu el jugador.
     * @param tresolt Taulell complet amb la solucio correcta.
     */
    public Supervivencia(Taulell tbuit, Taulell tresolt) {
        super(tbuit, tresolt);
        this.nvides = VIDES_INICIALS;
        this.taulerllsCompletats = 0;
        this.puntuacioAcumulada = 0.0f;
    }

    // -------------------------------------------------------------------------
    // Implementació de metodes abstractes
    // -------------------------------------------------------------------------

    /**
     * @brief Aplica la penalitzacio per error en mode Supervivència.
     *
     * Resta una vida al jugador. No modifica directament la puntuació.
     */
    @Override
    public void penalitzaError() {
        if (nvides > 0) nvides--;
    }

    /**
     * @brief Calcula i acumula la puntuació del tablero acabat de completar.
     *
     * S'ha de cridar cada cop que el jugador completa un tablero.
     * Formula per tablero: casellesBuidesInicials * 10 * multiplicador
     */
    @Override
    public void calcularPuntuacio() {
        puntuacioAcumulada += casellesBuidesInicials * 10 * d.getMultiplicador();
        puntuacio = puntuacioAcumulada;
    }

    /**
     * @brief Mostra els resultats finals de la partida Supervivència.
     */
    @Override
    public void treuResultats() {
        System.out.println("=== RESULTATS PARTIDA SUPERVIVENCIA ===");
        System.out.println("Tableros completats: " + taulerllsCompletats);
        System.out.println("Puntuacio final: " + puntuacio);
    }

    // -------------------------------------------------------------------------
    // Metodes específics de Supervivència
    // -------------------------------------------------------------------------

    /**
     * @brief Comprova si el jugador encara té vides.
     *
     * @return Cert si nvides > 0.
     */
    public boolean teVides() {
        return nvides > 0;
    }

    /**
     * @brief Registra la compleció d'un tablero i acumula la seva puntuació.
     *
     * S'ha de cridar des del controlador quan el jugador completa un tablero.
     */
    public void completarTaulell() {
        calcularPuntuacio();
        taulerllsCompletats++;
    }

    /**
     * @brief Carrega el seguent tablero per continuar la partida.
     *
     * Actualitza els tableros i reinicia els nombres disponibles per al nou tablero.
     * TODO: substituir per crida real a CrearHidato quan estigui implementat.
     *
     * @param nouTaulell   Nou taulell buit per jugar.
     * @param nouTResolt   Solucio del nou taulell.
     */
    public void carregarSeguenTaulell(Taulell nouTaulell, Taulell nouTResolt) {
        // TODO: aquest metode rebrà el taulell generat per CrearHidato
        this.t = new Taulell(nouTaulell);
        this.t_resolt = new Taulell(nouTResolt);
        this.casellesBuidesInicials = nouTaulell.NumerosDisponibles().size();
        this.NumerosDisponibles = nouTaulell.NumerosDisponibles();
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    /**
     * @brief Retorna el nombre de vides restants.
     * @return Nombre de vides.
     */
    public int getNvides() {
        return nvides;
    }

    /**
     * @brief Retorna el nombre de tableros completats.
     * @return Nombre de tableros completats.
     */
    public int getTaulerllsCompletats() {
        return taulerllsCompletats;
    }
}