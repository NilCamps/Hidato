package domini.algorismes;

import java.util.HashMap;
import java.util.Map;

import domini.model.Cella;
import domini.model.Taulell;

//ple

/**
 * @file ComprovarHidato.java
 * @brief Algorisme per verificar si un taulell Hidato és una solució vàlida.
 *
 *        Proporciona dos mètodes estàtics principals:
 *        - {@link #comprovar(Taulell)}: retorna un {@link Resultat} detallat
 *        amb la raó
 *        per la qual el taulell no és vàlid, o {@link Resultat#CORRECTE} si ho
 *        és.
 *        - {@link #esSolucioValida(Taulell)}: comprova la validesa de la
 *        seqüència numèrica
 *        del puzzle assumint que el taulell ja és complet.
 *
 *        Un taulell Hidato és correcte si i només si:
 *        1. No és {@code null} i té una estructura vàlida
 *        ({@link Taulell#esEstructuraValida()}).
 *        2. Totes les caselles jugables estan plenes
 *        ({@link Taulell#Completat()}).
 *        3. Els números 1..n formen un camí connex on cada número consecutiu és
 *        veí de l'anterior.
 *
 * @author Nil Camps
 * @version 1.0
 */
public class ComprovarHidato {

    /**
     * @brief Resultat de la verificació del taulell.
     *
     *        Possibles valors:
     *        - {@code CORRECTE}: El taulell és una solució vàlida del puzzle.
     *        - {@code TAULELL_NULL}: El taulell passat és {@code null}.
     *        - {@code ESTRUCTURA_INVALIDA}: L'estructura del taulell no és vàlida
     *        ({@link Taulell#esEstructuraValida()}).
     *        - {@code NO_COMPLET}: El taulell no està completament omplert.
     *        - {@code SOLUCIO_INCORRECTA}: El taulell està omplert però la
     *        seqüència
     *        numèrica no forma un camí connex vàlid.
     */
    public enum Resultat {
        CORRECTE,
        TAULELL_NULL,
        ESTRUCTURA_INVALIDA,
        NO_COMPLET,
        SOLUCIO_INCORRECTA
    }

    /**
     * @brief Comprova si un taulell Hidato representa una solució correcta.
     *
     *        Executa les validacions en ordre:
     *        1. El taulell no és {@code null}.
     *        2. L'estructura és vàlida ({@link Taulell#esEstructuraValida()}).
     *        3. El taulell és complet ({@link Taulell#Completat()}).
     *        4. La solució és numèricament correcta
     *        ({@link #esSolucioValida(Taulell)}).
     *
     * @param taulell El taulell a verificar.
     * @return Un valor de {@link Resultat} que indica el primer error trobat,
     *         o {@link Resultat#CORRECTE} si totes les comprovacions passen.
     */
    public static Resultat comprovar(Taulell taulell) {
        if (taulell == null) {
            return Resultat.TAULELL_NULL;
        }

        if (!taulell.esEstructuraValida()) {
            return Resultat.ESTRUCTURA_INVALIDA;
        }

        if (!taulell.Completat()) {
            return Resultat.NO_COMPLET;
        }

        if (!esSolucioValida(taulell)) {
            return Resultat.SOLUCIO_INCORRECTA;
        }

        return Resultat.CORRECTE;
    }

    /**
     * @brief Verifica si la distribució numèrica del taulell forma una solució
     *        Hidato vàlida.
     *
     *        Una solució és vàlida si:
     *        - El nombre de caselles jugables coincideix amb
     *        {@link Taulell#getNCaselles()}.
     *        - Cada enter del rang [1, n] apareix exactament una vegada.
     *        - Per a cada k en [1, n-1], la casella amb valor k és veïna adjacent
     *        de la
     *        casella amb valor k+1 (selon la {@link Taulell.Taulell.TForma} i
     *        {@link Taulell.Taulell.TAdjacencia} del taulell).
     *
     * @param taulell El taulell a verificar (si és {@code null} retorna
     *                {@code false}).
     * @return {@code true} si la distribució numèrica és una solució Hidato
     *         correcta.
     */
    public static boolean esSolucioValida(Taulell taulell) {
        if (taulell == null)
            return false;

        Map<Integer, Cella> posicions = new HashMap<>();
        int totalCaselles = 0;
        int nCaselles = taulell.getNCaselles();

        for (int i = 0; i < taulell.getFiles(); i++) {
            for (int j = 0; j < taulell.getColumnes(); j++) {
                if (!taulell.esCasellaValida(i, j))
                    continue;

                totalCaselles++;
                Cella cella = taulell.getCella(i, j);
                int valor = cella.getValor();

                if (valor <= 0 || valor > nCaselles)
                    return false;
                if (posicions.containsKey(valor))
                    return false;

                posicions.put(valor, cella);
            }
        }

        if (totalCaselles != nCaselles)
            return false;
        if (posicions.size() != nCaselles)
            return false;

        for (int k = 1; k < nCaselles; k++) {
            Cella actual = posicions.get(k);
            Cella seguent = posicions.get(k + 1);

            if (actual == null || seguent == null)
                return false;

            boolean sonVeines = false;
            for (Cella vei : taulell.obtenirVeines(actual.getPosX(), actual.getPosY())) {
                if (vei.getPosX() == seguent.getPosX() &&
                        vei.getPosY() == seguent.getPosY()) {
                    sonVeines = true;
                    break;
                }
            }

            if (!sonVeines)
                return false;
        }

        return true;
    }
}