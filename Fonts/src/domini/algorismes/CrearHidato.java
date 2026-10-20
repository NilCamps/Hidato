package domini.algorismes;

import domini.model.*;
import domini.model.Taulell.TForma;
import domini.model.Taulell.TAdjacencia;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CrearHidato {

    private final Random random;
    private Taulell solucio;
    private static final int MAX_INTENTS_CAMI = 200;
    private static final int MAX_INTENTS_ESTRUCTURA = 100;

    public CrearHidato() {
        this.random = new Random();
        this.solucio = null;
    }

    public CrearHidato(long seed) {
        this.random = new Random(seed);
        this.solucio = null;
    }

    public Taulell crearHidato(TForma forma, TAdjacencia adj, int nCaselles) {
        if (nCaselles <= 0) return null;

        Taulell taulell = generarEstructura(forma, adj, nCaselles);
        if (taulell == null) return null;

        Taulell taulellPle = generarSolucio(taulell);
        if (taulellPle == null) return null;

        this.solucio = new Taulell(taulellPle);

        Taulell puzzle = buidarCaselles(taulellPle, nCaselles);

        return puzzle;
    }

    public Taulell getSolucio() {
        return solucio != null ? new Taulell(solucio) : null;
    }


    // Generar estructura del taulell

    private Taulell generarEstructura(TForma forma, TAdjacencia adj, int nCaselles) {
        int[] dims = calcularDimensions(forma, nCaselles);
        int files = dims[0];
        int columnes = dims[1];
        int totalSlots = files * columnes;

        for (int intent = 0; intent < MAX_INTENTS_ESTRUCTURA; intent++) {
            Taulell t = new Taulell(0, adj, forma, files, columnes);

            int aEliminar = totalSlots - nCaselles;
            if (aEliminar > 0) {
                if (!eliminarCaselles(t, aEliminar, files, columnes)) {
                    continue;
                }
            }

            if (t.getNCaselles() == nCaselles && t.esTaulellConnectat()) {
                return t;
            }
        }

        return null;
    }

    private int[] calcularDimensions(TForma forma, int nCaselles) {
        int side = (int) Math.ceil(Math.sqrt(nCaselles));

        int files = side;
        int columnes = side;

        switch (forma) {
            case TRIANGLE:
                columnes = Math.max(columnes, side + 1);
                break;
            case HEXAGON:
                break;
            case QUADRAT:
            default:
                break;
        }

        while (files * columnes < nCaselles) {
            if (files <= columnes) files++;
            else columnes++;
        }

        return new int[]{files, columnes};
    }

    private boolean eliminarCaselles(Taulell t, int aEliminar, int files, int columnes) {
        List<int[]> candidats = new ArrayList<>();

        for (int i = 0; i < files; i++) {
            for (int j = 0; j < columnes; j++) {
                if (t.esCasellaValida(i, j)) {
                    boolean esVora = (i == 0 || i == files - 1 || j == 0 || j == columnes - 1);
                    boolean esCantona = esVora && (i == 0 || i == files - 1) && (j == 0 || j == columnes - 1);

                    if (esCantona) {
                        candidats.add(new int[]{i, j});
                        candidats.add(new int[]{i, j});
                        candidats.add(new int[]{i, j});
                    } else if (esVora) {
                        candidats.add(new int[]{i, j});
                        candidats.add(new int[]{i, j});
                    } else {
                        candidats.add(new int[]{i, j});
                    }
                }
            }
        }

        Collections.shuffle(candidats, random);

        int eliminades = 0;
        for (int[] pos : candidats) {
            if (eliminades >= aEliminar) break;

            int pi = pos[0];
            int pj = pos[1];
            if (!t.esCasellaValida(pi, pj)) continue;

            t.marcarForaArea(pi, pj);

            if (t.getNCaselles() > 0 && t.esTaulellConnectat()) {
                eliminades++;
            } else {
                return false;
            }
        }

        return eliminades >= aEliminar;
    }


    // Generar camí hamiltonià (solució)

    private Taulell generarSolucio(Taulell taulell) {
        int nCaselles = taulell.getNCaselles();

        for (int intent = 0; intent < MAX_INTENTS_CAMI; intent++) {
            Taulell copia = new Taulell(taulell);

            int[] inici = triarCasellaAleatoria(copia);
            if (inici == null) return null;

            int startX = inici[0];
            int startY = inici[1];

            copia.getCella(startX, startY).posarNumero(1);

            if (dfsCamiHamiltonia(copia, startX, startY, 1, nCaselles)) {
                return copia;
            }
        }

        return null;
    }

    private int[] triarCasellaAleatoria(Taulell t) {
        List<int[]> caselles = new ArrayList<>();

        for (int i = 0; i < t.getFiles(); i++) {
            for (int j = 0; j < t.getColumnes(); j++) {
                if (t.esCasellaValida(i, j)) {
                    caselles.add(new int[]{i, j});
                }
            }
        }

        if (caselles.isEmpty()) return null;

        return caselles.get(random.nextInt(caselles.size()));
    }

    private boolean dfsCamiHamiltonia(Taulell t, int x, int y, int num, int total) {
        if (num == total) {
            return true;
        }

        List<Cella> veins = new ArrayList<>(t.obtenirVeines(x, y));
        Collections.shuffle(veins, random);

        // Heurística de Warnsdorff
        veins.sort((a, b) -> {
            int degA = comptarVeinsBuits(t, a.getPosX(), a.getPosY());
            int degB = comptarVeinsBuits(t, b.getPosX(), b.getPosY());
            return Integer.compare(degA, degB);
        });

        int seg = num + 1;

        for (Cella v : veins) {
            if (v.esBuida()) {
                v.posarNumero(seg);

                if (seg == total || casellesBuidesConnexes(t, v.getPosX(), v.getPosY(), total - seg)) {
                    if (dfsCamiHamiltonia(t, v.getPosX(), v.getPosY(), seg, total)) {
                        return true;
                    }
                }

                v.buidar();
            }
        }

        return false;
    }

    private int comptarVeinsBuits(Taulell t, int x, int y) {
        int count = 0;
        for (Cella v : t.obtenirVeines(x, y)) {
            if (v.esBuida()) count++;
        }
        return count;
    }

    private boolean casellesBuidesConnexes(Taulell t, int x, int y, int esperats) {
        if (esperats == 0) return true;

        int startX = -1, startY = -1;
        outer:
        for (int i = 0; i < t.getFiles(); i++) {
            for (int j = 0; j < t.getColumnes(); j++) {
                if (t.esCasellaValida(i, j) && t.getCella(i, j).esBuida()) {
                    startX = i;
                    startY = j;
                    break outer;
                }
            }
        }

        if (startX == -1) return esperats == 0;

        boolean[][] vist = new boolean[t.getFiles()][t.getColumnes()];
        List<int[]> cua = new ArrayList<>();
        cua.add(new int[]{startX, startY});
        vist[startX][startY] = true;
        int count = 1;
        int idx = 0;

        while (idx < cua.size()) {
            int[] pos = cua.get(idx++);
            for (Cella v : t.obtenirVeines(pos[0], pos[1])) {
                int vx = v.getPosX();
                int vy = v.getPosY();
                if (!vist[vx][vy] && v.esBuida()) {
                    vist[vx][vy] = true;
                    cua.add(new int[]{vx, vy});
                    count++;
                }
            }
        }

        return count == esperats;
    }


    // Buidar caselles preservant solució única

    private Taulell buidarCaselles(Taulell taulellPle, int nCaselles) {
        Taulell puzzle = crearTaulellAmbPistesInicials(taulellPle);

        List<int[]> posicions = new ArrayList<>();
        for (int i = 0; i < puzzle.getFiles(); i++) {
            for (int j = 0; j < puzzle.getColumnes(); j++) {
                if (puzzle.esCasellaValida(i, j)) {
                    int val = puzzle.getCella(i, j).getValor();
                    if (val != 1 && val != nCaselles) {
                        posicions.add(new int[]{i, j});
                    }
                }
            }
        }

        Collections.shuffle(posicions, random);

        for (int[] pos : posicions) {
            int pi = pos[0];
            int pj = pos[1];


            Taulell prova = crearTaulellSensePista(puzzle, pi, pj);

            if (SolucioUnica.teSolucioUnica(prova)) {
                puzzle = prova;
            }
        }

        return puzzle;
    }

    private Taulell crearTaulellAmbPistesInicials(Taulell t) {
        Taulell resultat = new Taulell(0, t.getAdjacencia(), t.getForma(), t.getFiles(), t.getColumnes());

        for (int i = 0; i < t.getFiles(); i++) {
            for (int j = 0; j < t.getColumnes(); j++) {
                Cella c = t.getCella(i, j);
                if (c == null) {
                    resultat.marcarForaArea(i, j);
                } else if (c.esForat()) {
                    resultat.convertirEnForat(i, j);
                } else if (c.teValor()) {
                    resultat.posarPistaInicial(i, j, c.getValor());
                }
            }
        }

        return resultat;
    }

    private Taulell crearTaulellSensePista(Taulell t, int posX, int posY) {
        Taulell resultat = new Taulell(0, t.getAdjacencia(), t.getForma(), t.getFiles(), t.getColumnes());

        for (int i = 0; i < t.getFiles(); i++) {
            for (int j = 0; j < t.getColumnes(); j++) {
                Cella c = t.getCella(i, j);
                if (c == null) {
                    resultat.marcarForaArea(i, j);
                } else if (c.esForat()) {
                    resultat.convertirEnForat(i, j);
                } else if (c.teValor() && !(i == posX && j == posY)) {
                    resultat.posarPistaInicial(i, j, c.getValor());
                }
            }
        }

        return resultat;
    }
}