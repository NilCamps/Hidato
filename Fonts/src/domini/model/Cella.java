package domini.model;

/**
 * @file Cella.java
 * @brief Representa una cel·la individual del taulell Hidato.
 *
 *        Una cel·la pot ser:
 *        - Un forat (valor = {@link #FORAT}): casella negra no jugable.
 *        - Buida (valor = {@link #BUIDA}): casella jugable sense número
 *        assignat.
 *        - Amb número (valor > 0): casella jugable amb un número assignat.
 *
 *        A més, una cel·la pot ser inicial si el seu valor forma part de les
 *        pistes
 *        precarregades del puzzle, en aquest cas el valor no es pot modificar
 *        ni esborrar.
 *
 * @author Nil Camps
 * @version 1.0
 */
public class Cella {

    /** @brief Valor que representa un forat (casella no jugable). */
    public static final int FORAT = -1;

    /** @brief Valor que representa una casella buida (sense número assignat). */
    public static final int BUIDA = 0;

    /** @brief Fila de la cel·la dins el taulell. */
    private final int posX;

    /** @brief Columna de la cel·la dins el taulell. */
    private final int posY;

    /**
     * @brief Valor de la cel·la.
     *
     *        -1 = forat, 0 = buida, 1..n = número jugat o pista.
     */
    private int valor;

    /**
     * @brief Indica si la cel·la és una casella inicial (pista precarregada).
     *
     *        Les caselles inicials no es poden editar ni esborrar.
     */
    private final boolean casellaInicial;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * @brief Constructor principal sense indicació d'inicialitat.
     *
     *        Crea una cel·la a la posició (posX, posY) amb el valor indicat.
     *        La cel·la NO serà marcada com a inicial.
     *
     * @param posX  Fila de la cel·la (ha de ser >= 0).
     * @param posY  Columna de la cel·la (ha de ser >= 0).
     * @param valor Valor de la cel·la (ha de ser >= {@link #FORAT}).
     * @throws IllegalArgumentException si la posició és negativa o el valor
     *                                  és menor que {@link #FORAT}.
     */
    public Cella(int posX, int posY, int valor) {
        this(posX, posY, valor, false);
    }

    /**
     * @brief Constructor complet amb posició, valor i flag d'inicialitat.
     *
     * @param posX    Fila de la cel·la (ha de ser >= 0).
     * @param posY    Columna de la cel·la (ha de ser >= 0).
     * @param valor   Valor de la cel·la (ha de ser >= {@link #FORAT}).
     * @param inicial {@code true} si la cel·la és una pista inicial del puzzle.
     *                En aquest cas el valor ha de ser positiu (> 0).
     * @throws IllegalArgumentException si la posició és negativa, el valor és
     *                                  invàlid, o s'intenta crear una casella
     *                                  inicial amb valor no positiu.
     */
    public Cella(int posX, int posY, int valor, boolean inicial) {
        if (posX < 0 || posY < 0) {
            throw new IllegalArgumentException("La posició de la cel·la no és vàlida");
        }
        if (valor < FORAT) {
            throw new IllegalArgumentException("El valor de la cel·la no és vàlid");
        }
        if (inicial && valor <= 0) {
            throw new IllegalArgumentException("Una casella inicial ha de tenir un nombre positiu");
        }

        this.posX = posX;
        this.posY = posY;
        this.valor = valor;
        this.casellaInicial = inicial;
    }

    /**
     * @brief Constructor còpia.
     *
     *        Crea una còpia profunda d'una altra cel·la existent.
     *
     * @param altra La cel·la a copiar (no pot ser {@code null}).
     * @throws IllegalArgumentException si {@code altra} és {@code null}.
     */
    public Cella(Cella altra) {
        if (altra == null) {
            throw new IllegalArgumentException("No es pot copiar una cel·la null");
        }

        this.posX = altra.posX;
        this.posY = altra.posY;
        this.valor = altra.valor;
        this.casellaInicial = altra.casellaInicial;
    }

    // -------------------------------------------------------------------------
    // Consultors
    // -------------------------------------------------------------------------

    /**
     * @brief Retorna la fila de la cel·la.
     * @return Index de fila.
     */
    public int getPosX() {
        return posX;
    }

    /**
     * @brief Retorna la columna de la cel·la.
     * @return Índex de columna.
     */
    public int getPosY() {
        return posY;
    }

    /**
     * @brief Retorna el valor numèric de la cel·la.
     *
     *        Pot ser {@link #FORAT} (-1), {@link #BUIDA} (0), o un enter positiu.
     *
     * @return Valor de la cel·la.
     */
    public int getValor() {
        return valor;
    }

    /**
     * @brief Comprova si la cel·la és un forat.
     * @return {@code true} si el valor és {@link #FORAT}.
     */
    public boolean esForat() {
        return valor == FORAT;
    }

    /**
     * @brief Comprova si la cel·la és buida (sense número assignat).
     * @return {@code true} si el valor és {@link #BUIDA}.
     */
    public boolean esBuida() {
        return valor == BUIDA;
    }

    /**
     * @brief Comprova si la cel·la té un número assignat (valor > 0).
     * @return {@code true} si el valor és estrictament positiu.
     */
    public boolean teValor() {
        return valor > 0;
    }

    /**
     * @brief Comprova si la cel·la és una casella inicial (pista precarregada).
     * @return {@code true} si és casella inicial.
     */
    public boolean esInicial() {
        return casellaInicial;
    }

    /**
     * @brief Comprova si la cel·la es pot editar pel jugador.
     *
     *        Una cel·la és editable si i només si:
     *        - No és casella inicial.
     *        - No és un forat.
     *
     *        Una cel·la editable pot estar buida o contenir un valor temporal.
     *        Això permet tant assignar-hi un número com tornar-la a buidar
     *        durant el joc o en algoritmes de backtracking.
     *
     * @return {@code true} si la cel·la és editable.
     */
    public boolean esEditable() {
        return !casellaInicial && !esForat();
    }

    // -------------------------------------------------------------------------
    // Modificadors
    // -------------------------------------------------------------------------

    /**
     * @brief Assigna un número a la cel·la.
     *
     *        Només es pot cridar sobre cel·les editables ({@link #esEditable()}).
     *
     * @param numero El número a assignar (ha de ser > 0).
     * @throws IllegalArgumentException si {@code numero} no és positiu.
     * @throws IllegalStateException    si la cel·la no és editable.
     */
    public void posarNumero(int numero) {
        if (numero <= 0) {
            throw new IllegalArgumentException("El número ha de ser positiu");
        }
        if (!esEditable()) {
            throw new IllegalStateException("Aquesta cel·la no es pot editar");
        }

        this.valor = numero;
    }

    /**
     * @brief Buida la cel·la, eliminant el número assignat.
     *
     *        Només es pot cridar sobre cel·les editables ({@link #esEditable()}).
     *        Després de la crida, la cel·la tornarà a l'estat {@link #BUIDA}.
     *
     * @throws IllegalStateException si la cel·la no és editable.
     */
    public void buidar() {
        if (!esEditable()) {
            throw new IllegalStateException("Aquesta cel·la no es pot buidar");
        }

        this.valor = BUIDA;
    }

    // -------------------------------------------------------------------------
    // Serialització / còpia
    // -------------------------------------------------------------------------

    /**
     * @brief Retorna la representació en format token del fitxer de puzzle.
     *
     *        - {@code "*"} si és un forat.
     *        - {@code "?"} si és buida.
     *        - La representació del número si té valor.
     *
     * @return String amb el token corresponent.
     */
    public String toTokenFormat() {
        if (esForat())
            return "*";
        if (esBuida())
            return "?";
        return String.valueOf(valor);
    }

    /**
     * @brief Crea i retorna una còpia independent d'aquesta cel·la.
     * @return Nova instància de {@link Cella} amb els mateixos atributs.
     */
    public Cella copia() {
        return new Cella(this);
    }

    /**
     * @brief Retorna una representació textual de la cel·la per a debugging.
     *
     *        Format: {@code Cella [posX=x, posY=y, valor=v, casellaInicial=b]}
     *
     * @return String descriptiu.
     */
    @Override
    public String toString() {
        return "Cella [posX=" + posX +
                ", posY=" + posY +
                ", valor=" + valor +
                ", casellaInicial=" + casellaInicial + "]";
    }
}