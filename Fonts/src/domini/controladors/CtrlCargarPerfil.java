/**
 * @file CtrlCargarPerfil.java
 * @brief Controlador especialista encarregat de la càrrega i validació de perfils existents.
 * @author Imanol
 */

package domini.controladors;
import domini.model.*;


/**
 * @class CtrlCargarPerfil
 * @brief Aquesta classe conté la lògica per validar les credencials d'un usuari i permetre l'accés al seu perfil.
 * * Realitza les comprovacions de seguretat bàsiques: existència de l'identificador i validesa de la contrasenya
 * de l'usuari demanat mitjançant el sistema de persistència.
 */
public class CtrlCargarPerfil {

    /**
     * @brief Realitza la càrrega d'un perfil des de disc verificant el nom d'usuari i la contrasenya.
     * * Aquest mètode cerca el fitxer corresponent a l'identificador de l'usuari a través del controlador de
     * persistència i valida si la contrasenya coincideix amb la registrada. És la base del cas d'ús de càrrega de perfil.
     * * @param persistencia El controlador de persistència utilitzat per carregar les dades des de disc.
     * @param username El nom d'usuari que intenta accedir.
     * @param password La contrasenya proporcionada per a la validació.
     * @return Retorna l'objecte Perfil si la validació és satisfactòria; en cas contrari, retorna null.
     */
    public Perfil carregarPerfil(CtrlPersistencia persistencia, String username, String password) {

        // Mirem que existeixi l'usuari
        Perfil p = persistencia.loadPerfil(username);

        if (p == null) {
            System.out.println("Error: El user '" + username + "' no existeix.");
            return null;
        }


        // Confirmem la password es correcta
        if (!p.getPassword().equals(password)) {
            System.out.println("Error: Password incorrecte per '" + username + "'.");
            return null;
        }

        // tot bé, retornem el perfil
        return p;
    }

}
