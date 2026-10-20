package domini.model;

import java.time.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @class Partida
 * @brief Classe abstracta que representa l'estat comú de qualsevol tipus de partida de Hidato.
 * @author Arnau Rodriguez Trocho
 *
 * Conté tots els atributs i operacions compartides pels tres modes de joc:
 * Normal, Contrarrellotge i Supervivència.
 * Cada subclasse implementa la lògica específica de penalització, càlcul de
 * puntuació i presentació de resultats.
 */
public abstract class Partida {

    public enum TPartida {
        NORMAL,
        SUPERVIVENCIA,
        CONTRARRELLOTGE
    }

    /** @brief Taulell actual de la partida (estat visible pel jugador). */
    protected Taulell t;

    /** @brief Taulell resolt utilitzat per validar la solucio. */
    protected Taulell t_resolt;

    /** @brief Puntuacio acumulada durant la partida. */
    protected float puntuacio;

    /** @brief Instant en que s'inicia la partida. */
    protected Instant inicio;

    /** @brief Temps final registrat quan la partida acaba. */
    protected Duration tpartida;

    /** @brief Conjunt de nombres que encara no s'han col.locat al taulell. */
    protected Set<Integer> NumerosDisponibles;

    /** @brief Dificultat associada a la partida actual. */
    protected Dificultat d;

    /** @brief Nombre de caselles buides al inici de la partida (base per al calcul de puntuacio). */
    protected int casellesBuidesInicials;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    /**
     * @brief Constructor protegit. Inicialitza els atributs comuns de la partida.
     *
     * S'invoca des dels constructors de les subclasses. Configura la dificultat,
     * els nombres disponibles i registra l'instant d'inici.
     *
     * @param tbuit   Taulell inicial que veu el jugador.
     * @param tresolt Taulell complet amb la solucio correcta.
     */
    protected Partida(Taulell tbuit, Taulell tresolt) {
        this.t = new Taulell(tbuit);
        this.t_resolt = new Taulell(tresolt);
        this.puntuacio = 0.0f;
        this.tpartida = Duration.ZERO;
        this.d = new Dificultat();
        this.NumerosDisponibles = new HashSet<>();
        this.casellesBuidesInicials = tbuit.NumerosDisponibles().size();

        setNumerosDisponibles();
        this.inicio = Instant.now();
    }

    // -------------------------------------------------------------------------
    // Metodes privats
    // -------------------------------------------------------------------------

    /**
     * @brief Actualitza el conjunt de nombres disponibles a partir del taulell actual.
     */
    private void setNumerosDisponibles() {
        this.NumerosDisponibles = t.NumerosDisponibles();
    }

    // -------------------------------------------------------------------------
    // Metodes abstractes (cada subclasse els implementa)
    // -------------------------------------------------------------------------

    /**
     * @brief Aplica la penalitzacio corresponent al mode de joc quan l'usuari comet un error.
     */
    public abstract void penalitzaError();

    /**
     * @brief Calcula la puntuacio final segons les regles del mode de joc.
     */
    public abstract void calcularPuntuacio();

    /**
     * @brief Mostra per pantalla els resultats finals de la partida.
     */
    public abstract void treuResultats();

    // -------------------------------------------------------------------------
    // Metodes concrets comuns
    // -------------------------------------------------------------------------

    /**
     * @brief Col.loca un nombre al taulell i l'elimina dels disponibles.
     *
     * @param n Nombre que es vol posar.
     * @param x Coordenada de fila on es col.loca.
     * @param y Coordenada de columna on es col.loca.
     */
    public void modificaHidato(int n, int x, int y) {
        t.posarNumero(x, y, n);
        NumerosDisponibles.remove(n);
    }

    /**
     * @brief Comprova si el numero posat en la posicio (posx, posy) es el mateix que el de la solucio.
     *
     * @param n    Nombre a validar.
     * @param posx Coordenada de fila a comprovar.
     * @param posy Coordenada de columna a comprovar.
     * @return Cert si el valor coincideix amb la solucio, fals altrament.
     */
    public boolean comprovaSolucio(int n, int posx, int posy) {
        return t_resolt.getCella(posx, posy).getValor() == n;
    }

    /**
     * @brief Indica si un nombre continua disponible per jugar-se.
     *
     * @param number Nombre que es vol consultar.
     * @return Cert si el nombre pot utilitzar-se per jugar.
     */
    public boolean isNumberAvailable(int number) {
        return NumerosDisponibles.contains(number);
    }

    /**
     * @brief Comprova si una posicio del taulell es valida per col.locar un nombre.
     *
     * @param x Coordenada de fila.
     * @param y Coordenada de columna.
     * @return Cert si la casella es usable.
     */
    public boolean isPositionOk(int x, int y) {
        return t.esCasellaUsable(x, y);
    }

    /**
     * @brief Indica si el taulell actual esta completat.
     *
     * @return Cert si totes les caselles jugables estan plenes.
     */
    public boolean checkCompletat() {
        return t.Completat();
    }

    /**
     * @brief Mostra per pantalla el taulell actual.
     */
    public void outputTaulell() {
        t.imprimirTaulell();
    }

    /**
     * @brief Registra el temps final de la partida.
     */
    public void finalizarTiempo() {
        this.tpartida = Duration.between(this.inicio, Instant.now());
    }

    /**
     * @brief Retorna el temps transcorregut des de l'inici de la partida.
     *
     * @return Temps actual de joc com a Duration.
     */
    public Duration getTempsPartida() {
        return Duration.between(this.inicio, Instant.now());
    }

    /**
     * @brief Proporciona una pista a l'usuari si en queden disponibles.
     * TODO: implementar logica de pistes
     */
    public void donarPistes() {
        if (d.getNpistes() > 0) {
            for (int i = 0; i < t.getFiles(); i++) {
                for (int j = 0; j < t.getColumnes(); j++) {
                    Cella c = t.getCella(i, j);
                    if (c != null && !c.esForat() && c.esBuida()) {
                        int valorCorrecte = t_resolt.getCella(i, j).getValor();
                        modificaHidato(valorCorrecte, i, j);
                        d.consumirPista();
                        return;
                    }
                }
            }
            System.out.println("No hi ha caselles buides per donar pistes.");
        } else {
            System.out.println("No et queden pistes disponibles.");
        }
    }

    /**
     * @brief Retorna la partida actual (utilitzat per guardar l'estat).
     *
     * @return La propia instancia de Partida.
     */
    public Partida guardaPartida() {
        return this;
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    /**
     * @brief Retorna la puntuacio actual de la partida.
     * @return Puntuacio acumulada.
     */
    public float getPuntuacio() {
        return puntuacio;
    }

    /**
     * @brief Retorna la dificultat associada a la partida.
     * @return Objecte Dificultat.
     */
    public Dificultat getDificultat() {
        return d;
    }

    /**
     * @brief Estableix la dificultat de la partida.
     * S'ha de cridar des del controlador abans d'iniciar la partida.
     * @param d Objecte Dificultat configurat pel controlador.
     */
    public void setDificultat(Dificultat d) {
        this.d = d;
    }

    /**
     * @brief Retorna el nombre de caselles buides al inici de la partida.
     * @return Nombre de caselles buides inicials.
     */
    public int getCasellesBuidesInicials() {
        return casellesBuidesInicials;
    }

    /**
     * @brief Retorna el temps final de la partida.
     * @return Durada total de la partida.
     */
    public Duration getTpartida() {
        return tpartida;
    }

    /**
     * @brief Retorna el taulell actual de la partida.
     * @return Taulell amb l'estat visible pel jugador.
     */
    public Taulell getTaulell() {
        return t;
    }

    public long getTempsLimitSegons(){
        long n = 0;
        return n;
    }

    /**
     * @brief Buida una casella editable i torna el número als disponibles.
     * @param x Fila de la casella.
     * @param y Columna de la casella.
     * @return true si s'ha buidat correctament.
     */
    public boolean buidarCasella(int x, int y) {
        Cella c = t.getCella(x, y);
        if (c == null || !c.esEditable() || c.esBuida()) return false;
        int val = c.getValor();
        t.buidarCasella(x, y);
        NumerosDisponibles.add(val);
        return true;
    }

    public void resetInicio(Duration tempsJaTranscorregut){
        this.inicio = Instant.now().minus(tempsJaTranscorregut);
    }
}