/**
 * @file CtrlCrearPerfil.java
 * @brief Controlador especialista encarregat de la creació i registre de nous perfils d'usuari.
 * @author Imanol
 */
package domini.controladors;
import domini.model.*;

import java.util.HashMap;


/**
 * @class CtrlCrearPerfil
 * @brief Aquesta classe implementa la lògica de validació necessària per registrar un nou usuari al sistema.
 * * Verifica que les contrasenyes coincideixin i que l'identificador d'usuari no estigui duplicat
 * comprovant-ho directament mitjançant el sistema de persistència en disc.
 * Se segueix validació per gestionar els casos d'error abans de crear la instanciació i persistir-la.
 */
public class CtrlCrearPerfil {


    /**
     * @brief Crea un nou perfil d'usuari i el guarda a disc si les validacions són correctes.
     * * El mètode realitza dues validacions importants:
     * 1. Comparació de contrasenya i la seva confirmació, han de ser iguals.
     * 2. Comprovació de la disponibilitat del nom d'usuari mitjançant el sistema de persistència. No ha d'existir cap usuari amb aquest username.
     * * @param persistencia El controlador de persistència utilitzat per verificar el perfil i desar les dades.
     * @param name Nom real de l'usuari.
     * @param surname Cognom real de l'usuari.
     * @param username Nom d'usuari únic que servirà d'identificador.
     * @param password Contrasenya escollida per l'usuari.
     * @param confirmPassword Repetició de la contrasenya per a la seva verificació.
     * @return Retorna true si el perfil s'ha creat i guardat correctament a disc; false si alguna validació ha fallat.
     */
    public boolean crearPerfil(CtrlPersistencia persistencia, String name, String surname, String username, String password, String confirmPassword) {
        // Validar que el password y la confimació son iguals
        if (!password.equals(confirmPassword)) {
            System.out.println("Error: Les contrasenyes no coincideixen.");
            return false;
        }

        // Validar que el nom de perfil no está ja usat
        if (persistencia.existeixPerfil(username)) {
            System.out.println("Error: El nom d'usuari '" + username + "' ya existeix.");
            return false;
        }

        Perfil nouPerfil = new Perfil(name, surname, username, password);
        persistencia.storePerfil(nouPerfil);

        System.out.println("Perfil '" + username + "' creat amb èxit!.");
        return true;
    }


}
