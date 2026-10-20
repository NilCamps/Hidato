/**
 * @file CtrlEstadistiques.java
 * @brief Controlador especialista encarregat de l'actualització de mètriques i la seva repercussió als rànquings.
 * @author Imanol
 */

package domini.controladors;
import domini.model.*;

/**
 * @class CtrlEstadistiques
 * @brief Classe que gestiona la lògica de càlcul de progrés i la comunicació amb el sistema de rànquings.
 * * Aquesta classe conté l'algorisme per recalcular la mitjana acumulada i determinar si una puntuació
 * és un nou rècord personal, assegurant que les dades estiguin preparades abans de notificar al Singleton de Rankings.
 */
public class CtrlEstadistiques {

    /**
     * @brief Actualitza el progrés d'un usuari després de finalitzar una partida.
     * * Aquest mètode realitza les següents operacions:
     * 1. Incrementa el comptador de partides jugades.
     * 2. Calcula la nova puntuació mitjana de forma ponderada.
     * 3. Comprova i actualitza la millor puntuació si la nova la supera.
     * 4. Incrementa el total de partides completades.
     * 5. Notifica al Singleton @ref Rankings per actualitzar les posicions globals.
     * * @param p El perfil de l'usuari que ha acabat la partida.
     * @param puntuacio Puntuació obtinguda en la partida actual.
     */
    public void actualitzarProgres(Perfil p, float puntuacio) {

        Estadistiques estadistiques = p.getEstadistiques();
        estadistiques.setHidatosJugats(estadistiques.getHidatosJugats() + 1);

        int completatAnteriors = estadistiques.getHidatosCompletats();
        float mitjanaActual = estadistiques.getPuntuacioMitjana();
        float maxpts = estadistiques.getMillorPuntuacio();


        // Mitja acumulada
        float mitjanaNova = ((mitjanaActual * completatAnteriors) + puntuacio) / (completatAnteriors + 1);


        if (maxpts < puntuacio) {
            estadistiques.setMillorPuntuacio(puntuacio);
        }

        estadistiques.setPuntuacioMitjana(mitjanaNova);
        estadistiques.setHidatosCompletats(completatAnteriors + 1);

        Rankings.getInstanciaRankings().actualitzarRankings(p.getUsername(), estadistiques);
    }
}
