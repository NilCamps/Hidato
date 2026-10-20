/**
 * @file Perfil.java
 * @brief Classe que representa un perfil d'usuari en el sistema
 * @author Imanol
 */

package domini.model;

import java.util.Scanner;


/**
 * @class Perfil
 * @brief Aquesta classe emmagatzema la informació personal d'un usuari i la seva vinculació amb les estadístiques.
 * * Conté les dades d'identificació com el nom, cognoms, nom d'usuari i contrasenya,
 * a més de gestionar la creació de les seves pròpies estadístiques.
 */
public class Perfil {
    /** @brief Nom real de l'usuari */
    private String name;
    /** @brief Cognom real de l'usuari */
    private String surname;
    /** @brief Nom d'usuari (identificador únic) */
    private String username;
    /** @brief Contrasenya d'accés */
    private String password;

    /** @brief Objecte que conté les estadístiques personals de l'usuari */
    private Estadistiques estadistiques;


    public Perfil(){
        this.name = null;
        this.surname = null;
        this.username = null;
        this.password = null;
        this.estadistiques = null;
    }

    /**
     * @brief Constructor de la classe Perfil.
     * * Crea un nou perfil amb les dades proporcionades i inicialitza automàticament
     * un objecte Estadistiques vinculat a aquest perfil.
     *  * @param Iname Nom de l'usuari.
     * @param Isurname Cognoms de l'usuari.
     * @param Iusername Identificador únic d'usuari.
     * @param Ipassword Contrasenya per al compte.
     */
    public Perfil(String Iname, String Isurname, String Iusername, String Ipassword){
        this.name = Iname;
        this.surname = Isurname;
        this.username = Iusername;
        this.password = Ipassword;
        this.estadistiques = new Estadistiques(this);
    }
    /** @return Retorna el nom del perfil. */
    public String getName() {return name;}
    /** @return Retorna el cognom del perfil. */
    public String getSurname() {return surname;}
    /** @return Retorna el nom d'usuari del perfil. */
    public String getUsername() {return username;}
    /** @return Retorna la contrasenya del perfil. */
    public String getPassword() {return password;}

    /** * @brief Obté l'objecte d'estadístiques de l'usuari.
     * @return Retorna la instància d'Estadistiques associada.
     */
    public Estadistiques getEstadistiques() {return estadistiques;}
    /** @param password Nova contrasenya a establir. */
    public void setPassword(String password) {this.password = password;}
    /** @param name Nou nom a establir. */
    public void setName(String name) {this.name = name;}
    /** @param surname Nou cognom a establir. */
    public void setSurname(String surname) {this.surname = surname;}
    /** @param username Nou nom d'usuari a establir. */
    public void setUsername(String username) {this.username = username;}
}
