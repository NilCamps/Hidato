/**
 * @file CtrlEditarPerfil.java
 * @brief Controlador especialista encarregat de la modificació de dades de perfils existents.
 * @author Imanol
 */

package domini.controladors;
import domini.model.*;

/**
 * @class CtrlEditarPerfil
 * @brief Aquesta classe conté la lògica per modificar la informació d'un perfil d'usuari, assegurant la validació de les dades abans de la seva actualització.
 * * S'encarrega de gestionar els canvis de contrasenya i de dades personals (nom i cognoms), actuant sobre el model @ref Perfil.
 */
public class CtrlEditarPerfil {

    /**
     * @brief Modifica la contrasenya d'un perfil d'usuari amb validació de seguretat.
     * * Realitza 2 comprovacions abans d'actualitzar:
     * 1. Verifica que la contrasenya actual sigui correcta.
     * 2. Verifica que la nova contrasenya i la seva confirmació siguin idèntiques.
     * * @param p El perfil que s'està editant.
     * @param oldPassword La contrasenya actual per validar la identitat.
     * @param newPassword La nova contrasenya desitjada.
     * @param confirm La confirmació de la nova contrasenya.
     * @return Retorna true si s'ha canviat la contrasenya amb èxit; false si alguna validació ha fallat.
     */
    public boolean modificarPassword(Perfil p, String oldPassword, String newPassword, String confirm) {

        // Confirmem que la password és correcte
        if (!p.getPassword().equals(oldPassword)) {
            System.out.println("Error: La password actual no és correcte.");
            return false;
        }

        // Confirmem que la nova password i la de confirmació son iguals
        if (!newPassword.equals(confirm)) {
            System.out.println("Error: Les noves passwords no coincideixen");
            return false;
        }

        p.setPassword(newPassword);
        return true;
    }


    /**
     * @brief Modifica el nom i el cognom d'un perfil d'usuari.
     * * Inclou validacions per evitar l'ús de cadenes buides, assegurant la integritat de les dades del perfil.
     * * @param p El perfil que s'està editant.
     * @param name El nou nom de l'usuari.
     * @param surname El nou cognom de l'usuari.
     * @return Retorna true si s'ha actualitzat el nom amb èxit; false si s'han detectat camps buits.
     */
    public boolean modificarNom(Perfil p, String name, String surname) {
        if (name.isEmpty()) {
            System.out.println("Error: El nom no pot ser buit.");
            return false;
        }
        if (surname.isEmpty()) {
            System.out.println("Error: El cognom no pot ser buit.");
            return false;
        }

        p.setName(name);
        p.setSurname(surname);
        return true;
    }

}
