/**
 * @file Estadistiques.java
 * @brief Classe que gestiona les mètriques i estadístiques de joc d'un usuari.
 * @author Imanol
 */

package domini.model;



/**
 * @class Estadistiques
 * @brief Aquesta classe emmagatzema i gestiona el progrés acumulat d'un usuari en el sistema.
 * * Conté dades sobre partides jugades, completades i les puntuacions assolides,
 * servint com a base per a la generació de rànquings i el seguiment personal de l'usuari.
 */
public class Estadistiques {
    /** @brief Nombre total de partides d'Hidato que l'usuari ha començat */
    private int hidatosJugats;
    /** @brief Nombre total de partides que l'usuari ha finalitzat amb èxit */
    private int hidatosCompletats;
    /** @brief Puntuació mitjana obtinguda per l'usuari en totes les seves partides */
    private float puntuacioMitjana;
    /** @brief La puntuació més alta que l'usuari ha obtingut al finalitzar una partida */
    private float millorPuntuacio;

    /** @brief Referència al perfil d'usuari propietari d'aquestes estadístiques.
     * Es marca com a transient per evitar un bucle infinit en la serialització a JSON */
    private transient Perfil perfil;

    /**
     * @brief Constructor de la classe Estadistiques.
     * * Inicialitza tots els comptadors i puntuacions a zero.
     * * Estableix la relació bidireccional amb el perfil de l'usuari.
     * @param perfil Objecte Perfil al qual pertanyen aquestes estadístiques.
     */
    public Estadistiques(Perfil perfil){
        this.hidatosJugats = 0;
        this.hidatosCompletats = 0;
        this.puntuacioMitjana = 0.0f;
        this.millorPuntuacio = 0.0f;

        this.perfil = perfil;
    }
    /** @return Retorna el nombre de partides jugades. */
    public int getHidatosJugats() { return hidatosJugats; }
    /** @return Retorna el nombre de partides completades. */
    public int getHidatosCompletats() { return hidatosCompletats; }
    /** @return Retorna la puntuació mitjana acumulada. */
    public float getPuntuacioMitjana() { return puntuacioMitjana; }
    /** @return Retorna la millor puntuació registrada. */
    public float getMillorPuntuacio() { return millorPuntuacio; }
    /** @return Retorna l'objecte Perfil associat a aquestes estadístiques. */
    public Perfil getPerfil() { return perfil; }


    /** @param puntuacioMitjana Valor float amb la nova mitjana a establir. */
    public void setPuntuacioMitjana(float puntuacioMitjana) { this.puntuacioMitjana = puntuacioMitjana; };
    /** @param hidatosCompletats Valor enter amb el nou total de partides completades. */
    public void setHidatosCompletats(int hidatosCompletats) {this.hidatosCompletats = hidatosCompletats; };
    /** @param hidatosJugats Valor enter amb el nou total de partides jugades. */
    public void setHidatosJugats(int hidatosJugats) {this.hidatosJugats = hidatosJugats; };
    /** @param millorPuntuacio Valor float amb la nova millor puntuació assolida. */
    public void setMillorPuntuacio(float millorPuntuacio) {this.millorPuntuacio = millorPuntuacio; };
    /** @param perfil Objecte Perfil propietari d'aquestes estadístiques. */
    public void setPerfil(Perfil perfil) { this.perfil = perfil; }
}