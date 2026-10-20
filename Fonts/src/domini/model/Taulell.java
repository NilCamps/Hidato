package domini.model;

import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.ArrayDeque;

/**
 * @file Taulell.java
 * @brief Representa el taulell de joc d'un puzzle Hidato.
 *
 *        El taulell és una graella rectangular de {@link Cella}·les que pot
 *        tenir
 *        tres formes de cel·la ({@link TForma}) i dos modes d'adjacència
 *        ({@link TAdjacencia}).
 *
 *        Les posicions del taulell poden ser:
 *        - Fora d'àrea ({@code null}): posicions excloses de la graella activa,
 *        marcades amb {@code "#"} en el format de fitxer.
 *        - Forats (valors = {@link Cella#FORAT}): caselles negres no jugables,
 *        marcades amb {@code "*"} en el format de fitxer.
 *        - Buides (valors = {@link Cella#BUIDA}): caselles jugables sense
 *        número,
 *        marcades amb {@code "?"} en el format de fitxer.
 *        - Amb número (valor > 0): caselles amb número assignat o pista
 *        inicial.
 *
 * @author Nil Camps    
 * @version 1.0
 */
public class Taulell {

    /** @brief Identificador únic del taulell dins el sistema. */
    private int codi;

    /** @brief Nombre de files de la graella. */
    private int files;

    /** @brief Nombre de columnes de la graella. */
    private int columnes;

    /** @brief Matriu de cel·les del taulell ({@code null} indica fora d'àrea). */
    private Cella[][] celles;

    /**
     * @brief Nombre de caselles jugables.
     *
     *        Compta totes les caselles que no són {@code null} ni forats.
     *        S'actualitza automàticament en qualsevol operació que modifica
     *        l'estructura.
     */
    private int n_caselles;

    /** @brief Mode d'adjacència del taulell. */
    private TAdjacencia adjacencia;

    /** @brief Forma de les cel·les del taulell. */
    private TForma forma;

    // -------------------------------------------------------------------------
    // Enumeracions
    // -------------------------------------------------------------------------

    /**
     * @brief Tipus d'adjacència entre cel·les.
     *
     *        - {@code C} (Cares): adjacència per cares compartides (4 veïns en
     *        quadrat).
     *        - {@code CA} (Cares i Angles): adjacència per cares i cantons (8 veïns
     *        en quadrat).
     */
    public enum TAdjacencia {
        C, CA
    }

    /**
     * @brief Forma geomètrica de les cel·les del taulell.
     *
     *        - {@code TRIANGLE}: cel·les triangulars.
     *        - {@code QUADRAT}: cel·les quadrades.
     *        - {@code HEXAGON}: cel·les hexagonals.
     */
    public enum TForma {
        TRIANGLE, QUADRAT, HEXAGON
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * @brief Constructor principal. Crea un taulell buit amb els paràmetres
     *        indicats.
     *
     *        Totes les cel·les s'inicialitzen com a buides ({@link Cella#BUIDA}).
     *
     * @param codi       Identificador del taulell.
     * @param adjacencia Mode d'adjacència (no pot ser {@code null}).
     * @param forma      Forma de les cel·les (no pot ser {@code null}).
     * @param files      Nombre de files (ha de ser > 0).
     * @param columnes   Nombre de columnes (ha de ser > 0).
     * @throws IllegalArgumentException si {@code adjacencia} o {@code forma} són
     *                                  {@code null}, o si
     *                                  {@code files}/{@code columnes}
     *                                  no són positives.
     */
    public Taulell(int codi, TAdjacencia adjacencia, TForma forma, int files, int columnes) {
        if (adjacencia == null) {
            throw new IllegalArgumentException("L'adjacència no pot ser null");
        }
        if (forma == null) {
            throw new IllegalArgumentException("La forma no pot ser null");
        }
        if (files <= 0 || columnes <= 0) {
            throw new IllegalArgumentException("Files i columnes han de ser positives");
        }

        this.codi = codi;
        this.adjacencia = adjacencia;
        this.forma = forma;
        this.files = files;
        this.columnes = columnes;
        this.celles = new Cella[files][columnes];

        inicialitzarCellesBuides();
        recalcularNCaselles();
    }

    /**
     * @brief Constructor per defecte.
     *
     *        Crea un taulell 1×1 de forma quadrada i adjacència cardinal (cares).
     *        Útil per a instanciació ràpida en tests o inicialitzacions genèriques.
     */
    public Taulell() {
        this(0, TAdjacencia.C, TForma.QUADRAT, 1, 1);
    }

    /**
     * @brief Constructor còpia. Crea una rèplica independent del taulell donat.
     *
     *        Totes les {@link Cella}·les es copien de manera profunda
     *        ({@link Cella#copia()}).
     *
     * @param altre El taulell a copiar (no pot ser {@code null}).
     * @throws IllegalArgumentException si {@code altre} és {@code null}.
     */
    public Taulell(Taulell altre) {
        if (altre == null) {
            throw new IllegalArgumentException("No es pot copiar un taulell null");
        }

        this.codi = altre.codi;
        this.files = altre.files;
        this.columnes = altre.columnes;
        this.adjacencia = altre.adjacencia;
        this.forma = altre.forma;
        this.celles = new Cella[files][columnes];

        for (int i = 0; i < files; i++) {
            for (int j = 0; j < columnes; j++) {
                this.celles[i][j] = (altre.celles[i][j] == null) ? null : altre.celles[i][j].copia();
            }
        }

        this.n_caselles = altre.n_caselles;
    }

    // -------------------------------------------------------------------------
    // Mètodes privats auxiliars
    // -------------------------------------------------------------------------

    /**
     * @brief Inicialitza totes les cel·les com a buides.
     *
     *        S'invoca durant la construcció del taulell.
     */
    private void inicialitzarCellesBuides() {
        for (int i = 0; i < files; i++) {
            for (int j = 0; j < columnes; j++) {
                celles[i][j] = new Cella(i, j, Cella.BUIDA);
            }
        }
    }

    // -------------------------------------------------------------------------
    // Consultors bàsics
    // -------------------------------------------------------------------------

    /**
     * @brief Retorna l'identificador del taulell.
     * @return Codi del taulell.
     */
    public int getCodi() {
        return codi;
    }

    /**
     * @brief Retorna el nombre de files del taulell.
     * @return Nombre de files.
     */
    public int getFiles() {
        return files;
    }

    /**
     * @brief Retorna el nombre de columnes del taulell.
     * @return Nombre de columnes.
     */
    public int getColumnes() {
        return columnes;
    }

    /**
     * @brief Retorna el nombre de caselles jugables (no nul·les i no forats).
     * @return Nombre de caselles jugables.
     */
    public int getNCaselles() {
        return n_caselles;
    }

    /**
     * @brief Retorna el mode d'adjacència del taulell.
     * @return {@link TAdjacencia} del taulell.
     */
    public TAdjacencia getAdjacencia() {
        return adjacencia;
    }

    /**
     * @brief Retorna la forma geomètrica de les cel·les del taulell.
     * @return {@link TForma} del taulell.
     */
    public TForma getForma() {
        return forma;
    }

    // -------------------------------------------------------------------------
    // Validació de posicions
    // -------------------------------------------------------------------------

    /**
     * @brief Comprova si una posició es troba dins dels límits de la graella.
     *
     * @param posX Fila a comprovar.
     * @param posY Columna a comprovar.
     * @return {@code true} si 0 ≤ posX < files i 0 ≤ posY < columnes.
     */
    public boolean posicioValida(int posX, int posY) {
        return posX >= 0 && posX < files && posY >= 0 && posY < columnes;
    }

    /**
     * @brief Retorna la {@link Cella} situada a la posició donada.
     *
     * @param posX Fila de la cel·la.
     * @param posY Columna de la cel·la.
     * @return La {@link Cella} en aquella posició (pot ser {@code null} si és fora
     *         d'àrea).
     * @throws IndexOutOfBoundsException si la posició és fora dels límits del
     *                                   taulell.
     */
    public Cella getCella(int posX, int posY) {
        if (!posicioValida(posX, posY)) {
            throw new IndexOutOfBoundsException("Posició fora del taulell");
        }
        return celles[posX][posY];
    }

    /**
     * @brief Comprova si una posició és fora de l'àrea activa del taulell.
     *
     *        Una posició és fora d'àrea si la cel·la corresponent és {@code null}.
     *
     * @param posX Fila de la posició.
     * @param posY Columna de la posició.
     * @return {@code true} si la cel·la és {@code null}.
     * @throws IndexOutOfBoundsException si la posició és fora dels límits del
     *                                   taulell.
     */
    public boolean esForaArea(int posX, int posY) {
        if (!posicioValida(posX, posY)) {
            throw new IndexOutOfBoundsException("Posició fora del taulell");
        }
        return celles[posX][posY] == null;
    }

    /**
     * @brief Comprova si una posició conté una casella jugable (no nul·la i no
     *        forat).
     *
     * @param posX Fila de la posició.
     * @param posY Columna de la posició.
     * @return {@code true} si la posició és vàlida, la cel·la no és {@code null}
     *         i no és un forat.
     */
    public boolean esCasellaValida(int posX, int posY) {
        return posicioValida(posX, posY)
                && celles[posX][posY] != null
                && !celles[posX][posY].esForat();
    }

    /**
     * @brief Comprova si una posició conté una casella editable pel jugador.
     *
     *        Una casella és usable si és jugable (vegeu {@link #esCasellaValida}) i
     *        a més la {@link Cella} és editable (no és inicial ni té número).
     *
     * @param posX Fila de la posició.
     * @param posY Columna de la posició.
     * @return {@code true} si la casella es pot editar.
     */
    public boolean esCasellaUsable(int posX, int posY) {
        return posicioValida(posX, posY)
                && celles[posX][posY] != null
                && celles[posX][posY].esEditable();
    }

    // -------------------------------------------------------------------------
    // Modificadors d'estructura
    // -------------------------------------------------------------------------

    /**
     * @brief Marca una posició com a fora de l'àrea activa del taulell.
     *
     *        La cel·la es posa a {@code null}. Aquesta operació és irreversible
     *        (no es pot desfer sense recarregar el taulell).
     *        Actualitza {@link #n_caselles}.
     *
     * @param posX Fila de la posició.
     * @param posY Columna de la posició.
     * @throws IndexOutOfBoundsException si la posició és fora dels límits.
     */
    public void marcarForaArea(int posX, int posY) {
        if (!posicioValida(posX, posY)) {
            throw new IndexOutOfBoundsException("Posició fora del taulell");
        }
        celles[posX][posY] = null;
        recalcularNCaselles();
    }

    /**
     * @brief Converteix una posició en un forat (casella negra no jugable).
     *
     *        Substitueix la {@link Cella} actual per una nova amb valor
     *        {@link Cella#FORAT}.
     *        Actualitza {@link #n_caselles}.
     *
     * @param posX Fila de la posició.
     * @param posY Columna de la posició.
     * @throws IndexOutOfBoundsException si la posició és fora dels límits.
     */
    public void convertirEnForat(int posX, int posY) {
        if (!posicioValida(posX, posY)) {
            throw new IndexOutOfBoundsException("Posició fora del taulell");
        }
        celles[posX][posY] = new Cella(posX, posY, Cella.FORAT);
        recalcularNCaselles();
    }

    /**
     * @brief Assigna una pista inicial (número fix) a una posició del taulell.
     *
     *        Crea una nova {@link Cella} marcada com a inicial. La casella no podrà
     *        ser editada ni buidade pel jugador.
     *        Actualitza {@link #n_caselles}.
     *
     * @param posX   Fila de la posició.
     * @param posY   Columna de la posició.
     * @param numero Número de la pista (ha de ser > 0).
     * @throws IndexOutOfBoundsException si la posició és fora dels límits.
     * @throws IllegalArgumentException  si {@code numero} no és positiu.
     * @throws IllegalStateException     si la posició és fora d'àrea o és un forat.
     */
    public void posarPistaInicial(int posX, int posY, int numero) {
        if (!posicioValida(posX, posY)) {
            throw new IndexOutOfBoundsException("Posició fora del taulell");
        }
        if (numero <= 0) {
            throw new IllegalArgumentException("La pista inicial ha de ser positiva");
        }
        if (celles[posX][posY] == null || celles[posX][posY].esForat()) {
            throw new IllegalStateException("No es pot posar una pista inicial fora de l'hidato o en un forat");
        }

        celles[posX][posY] = new Cella(posX, posY, numero, true);
        recalcularNCaselles();
    }

    // -------------------------------------------------------------------------
    // Càrrega del taulell
    // -------------------------------------------------------------------------

    /**
     * @brief Crea un taulell a partir d'una llista de línies en format text del
     *        puzzle.
     *
     *        Format esperat:
     *        - Primera línia: {@code "Forma,Adjacència,Files,Columnes"}
     *        (p. ex. {@code "Q,C,3,4"})
     *        - Línies 2..Files+1: valors separats per comes
     *        ({@code "#"} = fora d'àrea, {@code "*"} = forat, {@code "?"} = buida,
     *        número enter positiu = valor).
     *
     * @param linies             Llista de línies del fitxer (no pot ser buida).
     * @param codi               Identificador que s'assignarà al taulell.
     * @param numerosSonInicials Si {@code true}, tots els números carregats es
     *                           marquen com a pistes inicials.
     * @return Un nou {@link Taulell} inicialitzat amb les dades del fitxer.
     * @throws IllegalArgumentException si el format és incorrecte o les dades no
     *                                  corresponen als paràmetres declarats.
     */
    public static Taulell carregarDesDeLinies(List<String> linies, int codi, boolean numerosSonInicials) {
        if (linies == null || linies.isEmpty()) {
            throw new IllegalArgumentException("El fitxer està buit");
        }

        String[] capcalera = linies.get(0).split(",");
        if (capcalera.length != 4) {
            throw new IllegalArgumentException("La capçalera ha de tenir 4 camps");
        }

        String formaStr = capcalera[0].trim();
        String adjStr = capcalera[1].trim();

        int files;
        int columnes;
        try {
            files = Integer.parseInt(capcalera[2].trim());
            columnes = Integer.parseInt(capcalera[3].trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Files o columnes no són enters vàlids");
        }

        TForma forma;
        switch (formaStr) {
            case "Q":
                forma = TForma.QUADRAT;
                break;
            case "H":
                forma = TForma.HEXAGON;
                break;
            case "T":
                forma = TForma.TRIANGLE;
                break;
            default:
                throw new IllegalArgumentException("Tipus de cel·la desconegut: " + formaStr);
        }

        TAdjacencia adjacencia;
        switch (adjStr) {
            case "C":
                adjacencia = TAdjacencia.C;
                break;
            case "CA":
                adjacencia = TAdjacencia.CA;
                break;
            default:
                throw new IllegalArgumentException("Tipus d'adjacència desconegut: " + adjStr);
        }

        if (linies.size() != files + 1) {
            throw new IllegalArgumentException("El nombre de línies no coincideix amb les files indicades");
        }

        String[][] tokens = new String[files][columnes];
        for (int i = 0; i < files; i++) {
            String[] parts = linies.get(i + 1).split(",");
            if (parts.length != columnes) {
                throw new IllegalArgumentException("La fila " + i + " no té " + columnes + " columnes");
            }

            for (int j = 0; j < columnes; j++) {
                tokens[i][j] = parts[j].trim();
            }
        }

        Taulell taulell = new Taulell(codi, adjacencia, forma, files, columnes);
        taulell.carregarDesDeFormatExtern(tokens, numerosSonInicials);
        return taulell;
    }

    /**
     * @brief Carrega l'estat del taulell a partir d'una matriu de tokens.
     *
     *        Reinicialitza totes les cel·les com a buides i les sobreescriu amb els
     *        valors de la matriu. Vegeu {@link #carregarDesDeLinies} per al format
     *        del token.
     *
     * @param tokens             Matriu de strings (files × columnes).
     * @param numerosSonInicials Si {@code true}, els números es marquen com a
     *                           inicials.
     * @throws IllegalArgumentException si la matriu és {@code null} o les
     *                                  dimensions
     *                                  no coincideixen amb el taulell.
     */
    public void carregarDesDeFormatExtern(String[][] tokens, boolean numerosSonInicials) {
        if (tokens == null) {
            throw new IllegalArgumentException("La matriu de tokens no pot ser null");
        }
        if (tokens.length != files) {
            throw new IllegalArgumentException("El nombre de files no coincideix amb el taulell");
        }

        for (int i = 0; i < files; i++) {
            if (tokens[i] == null || tokens[i].length != columnes) {
                throw new IllegalArgumentException("La fila " + i + " no té el nombre correcte de columnes");
            }
        }

        inicialitzarCellesBuides();

        for (int i = 0; i < files; i++) {
            for (int j = 0; j < columnes; j++) {
                carregarToken(tokens[i][j], i, j, numerosSonInicials);
            }
        }

        recalcularNCaselles();
    }

    /**
     * @brief Interpreta un token de text i aplica l'operació corresponent a la
     *        cel·la (i, j).
     *
     *        Tokens reconeguts:
     *        - {@code "#"}: marca la posició com a fora d'àrea.
     *        - {@code "*"}: converteix la posició en forat.
     *        - {@code "?"}: deixa la cel·la buida.
     *        - Enter positiu: assigna el número (inicial si
     *        {@code numerosSonInicials}).
     *
     * @param token              Token de text a interpretar.
     * @param i                  Fila de destí.
     * @param j                  Columna de destí.
     * @param numerosSonInicials Si {@code true}, els números es marquen com a
     *                           inicials.
     * @throws IllegalArgumentException si el token és invàlid.
     */
    private void carregarToken(String token, int i, int j, boolean numerosSonInicials) {
        if (token == null) {
            throw new IllegalArgumentException("Token null a la posició (" + i + ", " + j + ")");
        }

        token = token.trim();

        if (token.equals("#")) {
            marcarForaArea(i, j);
        } else if (token.equals("*")) {
            convertirEnForat(i, j);
        } else if (token.equals("?")) {
            celles[i][j] = new Cella(i, j, Cella.BUIDA);
        } else {
            int valor;
            try {
                valor = Integer.parseInt(token);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Token invàlid a la posició (" + i + ", " + j + "): " + token);
            }

            if (valor <= 0) {
                throw new IllegalArgumentException("Valor invàlid a la posició (" + i + ", " + j + "): " + token);
            }

            if (numerosSonInicials) {
                posarPistaInicial(i, j, valor);
            } else {
                celles[i][j] = new Cella(i, j, valor);
            }
        }
    }

    // -------------------------------------------------------------------------
    // Operacions de joc
    // -------------------------------------------------------------------------

    /**
     * @brief Comprova si es pot col·locar un número en una posició concreta.
     *
     *        La col·locació és possible si:
     *        - La casella és usable (editable).
     *        - El número és entre 1 i {@link #n_caselles}.
     *        - El número no existeix ja en cap altra casella del taulell.
     *
     * @param posX   Fila de la casella.
     * @param posY   Columna de la casella.
     * @param numero Número a col·locar.
     * @return {@code true} si la col·locació és permesa.
     */
    public boolean potPosarNumero(int posX, int posY, int numero) {
        if (!esCasellaUsable(posX, posY))
            return false;
        if (numero <= 0 || numero > n_caselles)
            return false;

        Cella trobada = buscarPerValor(numero);
        return trobada == null || (trobada.getPosX() == posX && trobada.getPosY() == posY);
    }

    /**
     * @brief Col·loca un número en una posició concreta del taulell.
     *
     * @param posX   Fila de la casella.
     * @param posY   Columna de la casella.
     * @param numero Número a col·locar (ha de complir {@link #potPosarNumero}).
     * @throws IllegalStateException si la col·locació no és permesa.
     */
    public void posarNumero(int posX, int posY, int numero) {
        if (!potPosarNumero(posX, posY, numero)) {
            throw new IllegalStateException("No es pot posar aquest número en aquesta casella");
        }
        celles[posX][posY].posarNumero(numero);
    }

    /**
     * @brief Buida una casella editable del taulell.
     *
     * @param posX Fila de la casella.
     * @param posY Columna de la casella.
     * @throws IllegalStateException si la casella no és usable.
     */
    public void buidarCasella(int posX, int posY) {
        if (!esCasellaUsable(posX, posY)) {
            throw new IllegalStateException("No es pot buidar aquesta casella");
        }
        celles[posX][posY].buidar();
    }

    /**
     * @brief Crea i retorna una còpia independent del taulell.
     * @return Nou {@link Taulell} amb el mateix estat.
     */
    public Taulell copia() {
        return new Taulell(this);
    }

    /**
     * @brief Busca la primera cel·la que conté el valor indicat.
     *
     * @param valor Valor a cercar (si ≤ 0 retorna {@code null} directament).
     * @return La {@link Cella} amb aquell valor, o {@code null} si no es troba.
     */
    public Cella buscarPerValor(int valor) {
        if (valor <= 0)
            return null;

        for (int i = 0; i < files; i++) {
            for (int j = 0; j < columnes; j++) {
                if (celles[i][j] != null && celles[i][j].getValor() == valor) {
                    return celles[i][j];
                }
            }
        }
        return null;
    }

    /**
     * @brief Recalcula i actualitza el comptador de caselles jugables.
     *
     *        Compta totes les posicions no {@code null} de la graella que no siguin
     *        forats.
     *        S'ha d'invocar cada cop que l'estructura del taulell canvia.
     */
    public void recalcularNCaselles() {
        int comptador = 0;
        for (int i = 0; i < files; i++) {
            for (int j = 0; j < columnes; j++) {
                if (celles[i][j] != null && !celles[i][j].esForat()) {
                    comptador++;
                }
            }
        }
        this.n_caselles = comptador;
    }

    // -------------------------------------------------------------------------
    // Consultes d'estat
    // -------------------------------------------------------------------------

    /**
     * @brief Comprova si totes les caselles jugables estan buides (sense número).
     * @return {@code true} si cap casella vàlida té un valor assignat.
     */
    public boolean estaBuit() {
        for (int i = 0; i < files; i++) {
            for (int j = 0; j < columnes; j++) {
                if (esCasellaValida(i, j) && celles[i][j].teValor()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * @brief Comprova si el taulell té almenys una casella amb pista inicial.
     * @return {@code true} si hi ha almenys una casella inicial.
     */
    public boolean tePistesInicials() {
        for (int i = 0; i < files; i++) {
            for (int j = 0; j < columnes; j++) {
                if (esCasellaValida(i, j) && celles[i][j].esInicial()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @brief Comprova si hi ha valors numèrics repetits entre les caselles
     *        jugables.
     * @return {@code true} si algun valor positiu apareix més d'un cop.
     */
    public boolean teValorsDuplicats() {
        Set<Integer> vistos = new HashSet<>();

        for (int i = 0; i < files; i++) {
            for (int j = 0; j < columnes; j++) {
                if (!esCasellaValida(i, j))
                    continue;

                int valor = celles[i][j].getValor();
                if (valor > 0) {
                    if (vistos.contains(valor))
                        return true;
                    vistos.add(valor);
                }
            }
        }
        return false;
    }

    /**
     * @brief Comprova si algun valor de les caselles jugables supera
     *        {@link #n_caselles}.
     * @return {@code true} si hi ha algun valor fora del rang [1, n_caselles].
     */
    public boolean teValorsForaDeRang() {
        for (int i = 0; i < files; i++) {
            for (int j = 0; j < columnes; j++) {
                if (!esCasellaValida(i, j))
                    continue;

                int valor = celles[i][j].getValor();
                if (valor > n_caselles)
                    return true;
            }
        }
        return false;
    }

    /**
     * @brief Comprova si l'estructura del taulell és vàlida com a puzzle Hidato.
     *
     *        Es consideren les condicions:
     *        1. Hi ha almenys una casella jugable.
     *        2. El taulell no és completament buit.
     *        3. No hi ha valors duplicats.
     *        4. No hi ha valors fora del rang [1, n].
     *        5. El taulell és connex (totes les caselles jugables formen un graf
     *        connex).
     *
     * @return {@code true} si totes les condicions es compleixen.
     */
    public boolean esEstructuraValida() {
        if (n_caselles <= 0)
            return false;
        if (estaBuit())
            return false;
        if (teValorsDuplicats())
            return false;
        if (teValorsForaDeRang())
            return false;
        if (!esTaulellConnectat())
            return false;
        return true;
    }

    /**
     * @brief Retorna el conjunt de números que encara no han estat col·locats al
     *        taulell.
     *
     *        El rang esperat és [1, {@link #n_caselles}].
     *
     * @return Conjunt d'enters disponibles (pot ser buit si el taulell és complet).
     */
    public Set<Integer> NumerosDisponibles() {
        Set<Integer> disponibles = new HashSet<>();

        for (int i = 1; i <= n_caselles; i++) {
            disponibles.add(i);
        }

        for (int i = 0; i < files; i++) {
            for (int j = 0; j < columnes; j++) {
                if (celles[i][j] != null) {
                    int v = celles[i][j].getValor();
                    if (v > 0) {
                        disponibles.remove(v);
                    }
                }
            }
        }

        return disponibles;
    }

    /**
     * @brief Comprova si el taulell està completat (totes les caselles jugables
     *        plenes).
     * @return {@code true} si no hi ha cap casella vàlida buida.
     */
    public boolean Completat() {
        for (int i = 0; i < files; i++) {
            for (int j = 0; j < columnes; j++) {
                Cella c = celles[i][j];
                if (c != null && !c.esForat() && c.esBuida()) {
                    return false;
                }
            }
        }
        return true;
    }

    // -------------------------------------------------------------------------
    // Veïnatge
    // -------------------------------------------------------------------------

    /**
     * @brief Mètode auxiliar que afegeix la cel·la (x, y) a la llista si és vàlida.
     *
     * @param veines Llista on s'afegirà la cel·la.
     * @param x      Fila de la posició candidata.
     * @param y      Columna de la posició candidata.
     */
    private void afegirSiValida(List<Cella> veines, int x, int y) {
        if (esCasellaValida(x, y)) {
            veines.add(celles[x][y]);
        }
    }

    /**
     * @brief Retorna la llista de cel·les veïnes de la posició (x, y).
     *
     *        El conjunt de veïns depèn de la {@link TForma} i la
     *        {@link TAdjacencia}
     *        del taulell. Si la posició no és una casella vàlida, retorna una
     *        llista buida.
     *
     * @param x Fila de la posició.
     * @param y Columna de la posició.
     * @return Llista de {@link Cella}·les veïnes (mai {@code null}).
     */
    public List<Cella> obtenirVeines(int x, int y) {
        if (!esCasellaValida(x, y)) {
            return new ArrayList<>();
        }

        switch (forma) {
            case QUADRAT:
                return obtenirVeinesQuadrat(x, y);
            case TRIANGLE:
                return obtenirVeinesTriangle(x, y);
            case HEXAGON:
                return obtenirVeinesHexagon(x, y);
            default:
                throw new IllegalStateException("Forma de cel·la no suportada");
        }
    }

    /**
     * @brief Calcula els veïns d'una cel·la quadrada.
     *
     *        En mode {@link TAdjacencia#C} afegeix els 4 veïns ortogonals.
     *        En mode {@link TAdjacencia#CA} afegeix a més els 4 veïns diagonals.
     *
     * @param x Fila de la cel·la.
     * @param y Columna de la cel·la.
     * @return Llista de cel·les veïnes.
     */
    private List<Cella> obtenirVeinesQuadrat(int x, int y) {
        List<Cella> veines = new ArrayList<>();

        afegirSiValida(veines, x - 1, y);
        afegirSiValida(veines, x + 1, y);
        afegirSiValida(veines, x, y - 1);
        afegirSiValida(veines, x, y + 1);

        if (adjacencia == TAdjacencia.CA) {
            afegirSiValida(veines, x - 1, y - 1);
            afegirSiValida(veines, x - 1, y + 1);
            afegirSiValida(veines, x + 1, y - 1);
            afegirSiValida(veines, x + 1, y + 1);
        }

        return veines;
    }

    /**
     * @brief Calcula els veïns d'una cel·la triangular.
     *
     *        La connectivitat depèn de si el triangle té la base a baix
     *        ({@code (x+y) % 2 == 0}) o a dalt.
     *        En mode {@link TAdjacencia#CA} s'afegeixen els veïns extesos.
     *
     * @param x Fila de la cel·la.
     * @param y Columna de la cel·la.
     * @return Llista de cel·les veïnes.
     */
    private List<Cella> obtenirVeinesTriangle(int x, int y) {
        List<Cella> veines = new ArrayList<>();
        boolean baseABaix = ((x + y) % 2 == 0);

        afegirSiValida(veines, x, y - 1);
        afegirSiValida(veines, x, y + 1);

        if (baseABaix)
            afegirSiValida(veines, x + 1, y);
        else
            afegirSiValida(veines, x - 1, y);

        if (adjacencia == TAdjacencia.CA) {
            afegirSiValida(veines, x - 1, y - 1);
            afegirSiValida(veines, x - 1, y + 1);
            afegirSiValida(veines, x + 1, y - 1);
            afegirSiValida(veines, x + 1, y + 1);
        }

        return veines;
    }

    /**
     * @brief Calcula els veïns d'una cel·la hexagonal.
     *
     *        Utilitza el model de graella offset on les files impars estan
     *        desplaçades.
     *        L'adjacència hexagonal és sempre de 6 veïns (no s'afegeix mode CA
     *        diferent).
     *
     * @param x Fila de la cel·la.
     * @param y Columna de la cel·la.
     * @return Llista de cel·les veïnes (fins a 6).
     */
    private List<Cella> obtenirVeinesHexagon(int x, int y) {
        List<Cella> veines = new ArrayList<>();
        boolean filaDesplacada = (x % 2 == 1);

        afegirSiValida(veines, x, y - 1);
        afegirSiValida(veines, x, y + 1);

        if (filaDesplacada) {
            afegirSiValida(veines, x - 1, y);
            afegirSiValida(veines, x - 1, y + 1);
            afegirSiValida(veines, x + 1, y);
            afegirSiValida(veines, x + 1, y + 1);
        } else {
            afegirSiValida(veines, x - 1, y - 1);
            afegirSiValida(veines, x - 1, y);
            afegirSiValida(veines, x + 1, y - 1);
            afegirSiValida(veines, x + 1, y);
        }

        return veines;
    }

    // -------------------------------------------------------------------------
    // Connectivitat
    // -------------------------------------------------------------------------

    /**
     * @brief Comprova si totes les caselles jugables del taulell formen un graf
     *        connex.
     *
     *        Utilitza una cerca en amplada (BFS) partint d'una casella jugable
     *        arbitrària.
     *        Un taulell sense caselles jugables es considera no connex.
     *
     * @return {@code true} si totes les caselles jugables són accessibles entre
     *         elles.
     */
    public boolean esTaulellConnectat() {
        int totalValides = 0;
        int iniciX = -1;
        int iniciY = -1;

        for (int i = 0; i < files; i++) {
            for (int j = 0; j < columnes; j++) {
                if (esCasellaValida(i, j)) {
                    totalValides++;
                    if (iniciX == -1) {
                        iniciX = i;
                        iniciY = j;
                    }
                }
            }
        }

        if (totalValides == 0)
            return false;

        boolean[][] visitat = new boolean[files][columnes];
        Queue<int[]> cua = new ArrayDeque<>();
        cua.add(new int[] { iniciX, iniciY });
        visitat[iniciX][iniciY] = true;

        int visitades = 0;

        while (!cua.isEmpty()) {
            int[] pos = cua.poll();
            int x = pos[0];
            int y = pos[1];
            visitades++;

            for (Cella vei : obtenirVeines(x, y)) {
                int vx = vei.getPosX();
                int vy = vei.getPosY();

                if (!visitat[vx][vy]) {
                    visitat[vx][vy] = true;
                    cua.add(new int[] { vx, vy });
                }
            }
        }

        return visitades == totalValides;
    }

    // -------------------------------------------------------------------------
    // Serialització
    // -------------------------------------------------------------------------

    /**
     * @brief Retorna la cadena amb el codi de forma per al format de fitxer.
     * @return {@code "Q"}, {@code "H"} o {@code "T"}.
     */
    private String getFormaFormatExtern() {
        switch (forma) {
            case QUADRAT:
                return "Q";
            case HEXAGON:
                return "H";
            case TRIANGLE:
                return "T";
            default:
                throw new IllegalStateException("Forma no suportada");
        }
    }

    /**
     * @brief Retorna la cadena amb el codi d'adjacència per al format de fitxer.
     * @return {@code "C"} o {@code "CA"}.
     */
    private String getAdjacenciaFormatExtern() {
        switch (adjacencia) {
            case C:
                return "C";
            case CA:
                return "CA";
            default:
                throw new IllegalStateException("Adjacència no suportada");
        }
    }

    /**
     * @brief Retorna la representació completa del taulell en format de fitxer.
     *
     *        La primera línia és la capçalera ({@code "Forma,Adj,Files,Columnes"})
     *        seguida del contingut de la graella (vegeu {@link #toString()}).
     *
     * @return String amb el contingut complet llest per ser escrit a fitxer.
     */
    public String toFormatExternComplet() {
        StringBuilder sb = new StringBuilder();
        sb.append(getFormaFormatExtern())
                .append(",")
                .append(getAdjacenciaFormatExtern())
                .append(",")
                .append(files)
                .append(",")
                .append(columnes)
                .append(System.lineSeparator())
                .append(toString());
        return sb.toString();
    }

    /**
     * @brief Retorna la representació en format token de la graella del taulell.
     *
     *        Cada fila és una línia separada. Els valors de cada fila estan
     *        separats per comes.
     *        Cada cel·la s'imprimeix amb {@link Cella#toTokenFormat()}, o
     *        {@code "#"} si
     *        la posició és fora d'àrea ({@code null}).
     *
     * @return String amb la graella en format text.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < files; i++) {
            if (i > 0)
                sb.append(System.lineSeparator());

            for (int j = 0; j < columnes; j++) {
                if (j > 0)
                    sb.append(",");

                Cella c = celles[i][j];
                if (c == null)
                    sb.append("#");
                else
                    sb.append(c.toTokenFormat());
            }
        }

        return sb.toString();
    }

    /**
     * @brief Imprimeix la graella del taulell per la sortida estàndard.
     *
     *        Utilitza el format de {@link #toString()}.
     */
    public void imprimirTaulell() {
        System.out.println(toString());
    }

    /**
     * @brief Imprimeix el taulell complet (capçalera + graella) per la sortida
     *        estàndard.
     *
     *        Utilitza el format de {@link #toFormatExternComplet()}.
     */
    public void imprimirFormatExternComplet() {
        System.out.println(toFormatExternComplet());
    }
}