/**
 * @file EntradaRanking.java
 * @brief Classe que representa una entrada individual en un rànquing d'Hidato.
 * @author Imanol
 */

package domini.model;

/**
 * @class EntradaRanking
 * @brief Classe per encapsular un parell format per l'identificador d'usuari i un valor de puntuació.
 * * S'utilitza com a unitat bàsica per a les llistes dels rankings
 */
public class EntradaRanking {
    /** @brief Identificador únic de l'usuari (username) */
    private String id;
    /** @brief Valor numèric de la puntuació o estadística assolida */
    private float puntuacio;


    /**
     * @brief Constructor de la classe EntradaRanking.
     * @param id Identificador de l'usuari.
     * @param puntuacio Valor de la puntuació o estadística.
     */
    public EntradaRanking(String id, float puntuacio) {
        this.id = id;
        this.puntuacio = puntuacio;
    }

    /** @return Retorna l'identificador de l'usuari associat a l'entrada. */
    public String getUsername() { return id; }
    /** @return Retorna el valor de la puntuació associada a l'entrada. */
    public float getValor() { return puntuacio; }

    /** @param id Nou identificador d'usuari per a l'entrada. */
    public void setUsername(String id) { this.id = id; }
    /** @param puntuacio Nou valor de puntuació per a l'entrada. */
    public void setPuntuacio(float puntuacio) { this.puntuacio = puntuacio; }

}
