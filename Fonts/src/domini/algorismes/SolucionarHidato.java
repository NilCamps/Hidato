package domini.algorismes;

import domini.model.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;

public class SolucionarHidato {
    public SolucionarHidato(){

    }


    public Taulell SolucionarHidatoTaulell(Taulell t) {
        Taulell copia = new Taulell(t);

        int min = Integer.MAX_VALUE;
        int max = copia.getNCaselles();

        for (int i = 0; i < copia.getFiles(); i++) {
            for (int j = 0; j < copia.getColumnes(); j++) {
                Cella c = copia.getCella(i, j);
                if (c != null && !c.esForat() && c.getValor() > 0) {
                    min = Math.min(min, c.getValor());
                }
            }
        }

        if (min == Integer.MAX_VALUE) return null;

        Cella start = copia.buscarPerValor(min);
        if (start == null) return null;

        Taulell[] solucio = { null };

        dfsResoldre(copia, start.getPosX(), start.getPosY(), min, max, solucio);

        return solucio[0];
    }


  
    public boolean solucionarHidatoBoolean(Taulell t) {
        Taulell copia = new Taulell(t);

        // Buscar mínim
        int min = Integer.MAX_VALUE;
        int max = copia.getNCaselles();

        for (int i = 0; i < copia.getFiles(); i++) {
            for (int j = 0; j < copia.getColumnes(); j++) {
                Cella c = copia.getCella(i,j);
                if (c != null && !c.esForat() && c.getValor() > 0) {
                    min = Math.min(min, c.getValor());
                }
            }
        }

        if (min == Integer.MAX_VALUE) return false;

        Cella start = copia.buscarPerValor(min);
        if (start == null) return false;

        int[] solucions = {0};

        dfsValidar(copia, start.getPosX(), start.getPosY(), min, max, solucions);

        return solucions[0] == 1;
    }

    private void dfsResoldre(Taulell t, int x, int y, int num, int max, Taulell[] solucio) {
        if (solucio[0] != null) return;

        if (num == max) {
            solucio[0] = t.copia();
            return;
        }

        int seg = num + 1;

        //  PRUNING DE DISTÀNCIA
        Cella fixa = t.buscarPerValor(seg);
        if (fixa != null) {

            boolean veina = false;
            for (Cella v : t.obtenirVeines(x, y)) {
                if (v.getPosX() == fixa.getPosX() && v.getPosY() == fixa.getPosY()) {
                    veina = true;
                    break;
                }
            }
            if (!veina) return;

            //  PRUNING D'ESPAI DISPONIBLE
            int restantsFix = max - seg;
            int espaiFix = comptarEspaiDisponible(t, fixa.getPosX(), fixa.getPosY());
            if (espaiFix < restantsFix) return;

            // PRUNING DE DISTÀNCIA
            for (int k = seg + 1; k <= max; k++) {
                Cella dest = t.buscarPerValor(k);
                if (dest != null) {
                    int dist = Math.abs(dest.getPosX() - fixa.getPosX()) +
                               Math.abs(dest.getPosY() - fixa.getPosY());
                    int steps = k - seg;
                    if (dist > steps) return;
                    break;
                }
            }

            dfsResoldre(t, fixa.getPosX(), fixa.getPosY(), seg, max, solucio);
            return;
        }

        // PRUNING D'ESPAI DISPONIBLE
        int restants = max - num;
        int espai = comptarEspaiDisponible(t, x, y);
        if (espai < restants) return;

        // PRUNING DE DISTÀNCIA
        for (int k = seg; k <= max; k++) {
            Cella dest = t.buscarPerValor(k);
            if (dest != null) {
                int dist = Math.abs(dest.getPosX() - x) +
                           Math.abs(dest.getPosY() - y);
                int steps = k - num;
                if (dist > steps) return;
                break;
            }
        }

        // HEURÍSTICA: ordenar veïns
        List<Cella> veins = new ArrayList<>(t.obtenirVeines(x, y));
        veins.sort((a, b) -> {
            int da = distanciaHeuristica(a, t, seg, max);
            int db = distanciaHeuristica(b, t, seg, max);
            return Integer.compare(da, db);
        });

        // Provar veïns buits
        for (Cella v : veins) {
            if (v.esBuida()) {
                v.posarNumero(seg);

                dfsResoldre(t, v.getPosX(), v.getPosY(), seg, max, solucio);

                if (solucio[0] != null) return;

                v.buidar();
            }
        }
    }


    private void dfsValidar(Taulell t, int x, int y, int num, int max, int[] solucions) {
        if (solucions[0] > 1) return;

        if (num == max) {
            solucions[0]++;
            return;
        }

        int seg = num + 1;

        // PRUNING DE DISTÀNCIA
        Cella fixa = t.buscarPerValor(seg);
        if (fixa != null) {

            // Ha de ser veïna
            boolean veina = false;
            for (Cella v : t.obtenirVeines(x, y)) {
                if (v.getPosX() == fixa.getPosX() && v.getPosY() == fixa.getPosY()) {
                    veina = true;
                    break;
                }
            }
            if (!veina) return;

            // PRUNING D'ESPAI DISPONIBLE
            int restantsFix = max - seg;
            int espaiFix = comptarEspaiDisponible(t, fixa.getPosX(), fixa.getPosY());
            if (espaiFix < restantsFix) return;

            // PRUNING DE DISTÀNCIA
            for (int k = seg + 1; k <= max; k++) {
                Cella dest = t.buscarPerValor(k);
                if (dest != null) {
                    int dist = Math.abs(dest.getPosX() - fixa.getPosX()) +
                            Math.abs(dest.getPosY() - fixa.getPosY());
                    int steps = k - seg;
                    if (dist > steps) return;
                    break;
                }
            }

            dfsValidar(t, fixa.getPosX(), fixa.getPosY(), seg, max, solucions);
            return;
        }

        // PRUNING D'ESPAI DISPONIBLE
        int restants = max - num;
        int espai = comptarEspaiDisponible(t, x, y);
        if (espai < restants) return;

        // PRUNING DE DISTÀNCIA
        for (int k = seg; k <= max; k++) {
            Cella dest = t.buscarPerValor(k);
            if (dest != null) {
                int dist = Math.abs(dest.getPosX() - x) +
                        Math.abs(dest.getPosY() - y);
                int steps = k - num;
                if (dist > steps) return;
                break;
            }
        }

        // HEURÍSTICA: ordenar veïns
        List<Cella> veins = new ArrayList<>(t.obtenirVeines(x, y));
        veins.sort((a, b) -> {
            int da = distanciaHeuristica(a, t, seg, max);
            int db = distanciaHeuristica(b, t, seg, max);
            return Integer.compare(da, db);
        });

        // Provar veïns buits
        for (Cella v : veins) {
            if (v.esBuida()) {
                v.posarNumero(seg);

                dfsValidar(t, v.getPosX(), v.getPosY(), seg, max, solucions);

                v.buidar();

                if (solucions[0] > 1) return;
            }
        }
    }


    private int distanciaHeuristica(Cella c, Taulell t, int seg, int max) {
        for (int k = seg + 1; k <= max; k++) {
            Cella dest = t.buscarPerValor(k);
            if (dest != null) {
                return Math.abs(dest.getPosX() - c.getPosX()) +
                    Math.abs(dest.getPosY() - c.getPosY());
            }
        }
        return 0;
    }

    private int comptarEspaiDisponible(Taulell t, int x, int y) {
        boolean[][] vist = new boolean[t.getFiles()][t.getColumnes()];
        Queue<Cella> q = new LinkedList<>();

        Cella start = t.getCella(x, y);
        q.add(start);
        vist[x][y] = true;

        int count = 1;

        while (!q.isEmpty()) {
            Cella c = q.poll();
            for (Cella v : t.obtenirVeines(c.getPosX(), c.getPosY())) {
                int vx = v.getPosX();
                int vy = v.getPosY();
                if (!vist[vx][vy] && !v.esForat()) {
                    vist[vx][vy] = true;
                    q.add(v);
                    count++;
                }
            }
        }

        return count;
    }
}