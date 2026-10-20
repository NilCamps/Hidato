/**
 * @file CtrlPerfil.java
 * @brief Controlador encarregat de coordinar la gestió de perfils d'usuari del sistema i les estadistiques
 * @author Imanol
 */

package domini.controladors;
import domini.model.*;




/**
 * @class CtrlPerfil
 * @brief Aquesta classe actua com a controlador de domini per a totes les operacions relacionades amb els perfils.
 * * Utilitza el controlador de persistència per a carregar usuaris sota demanda.
 * * Delega les operacions específiques a controladors especialistes (creació, càrrega, edició i estadístiques)
 * seguint el principi de responsabilitat única.
 */
public class CtrlPerfil {

    /** @brief Instància del controlador encarregat de llegir i escriure al disc (permanencia). */
    private CtrlPersistencia persistencia;
    /** @brief Especialista en la creació de nous perfils. */
    private CtrlCrearPerfil creador;
    /** @brief Especialista en la verificació i càrrega de perfils existents (Login). */
    private CtrlCargarPerfil carregador;
    /** @brief Especialista en la modificació de dades de perfils existents. */
    private CtrlEditarPerfil editor;
    /** @brief Especialista en la gestió i actualització de les mètriques de l'usuari. */
    private CtrlEstadistiques gestorStats;


    /**
     * @brief Constructor del controlador de perfils.
     * * Guarda la referència al controlador de persistència i instancia els controladors especialistes necessaris
     * per gestionar els casos d'ús.
     * @param persistencia Instància única del controlador de persistència del sistema.
     */
    public CtrlPerfil(CtrlPersistencia persistencia) {
        this.persistencia = persistencia;
        this.creador = new CtrlCrearPerfil();
        this.carregador = new CtrlCargarPerfil();
        this.editor = new CtrlEditarPerfil();
        this.gestorStats = new CtrlEstadistiques();
    }


    // Cas d'ús crearPerfil
    /**
     * @brief Gestiona el cas d'ús de creació d'un nou perfil.
     * * @param name Nom real de l'usuari.
     * @param surname Cognom real de l'usuari.
     * @param username Identificador únic desitjat.
     * @param password Contrasenya del nou compte.
     * @param confirmPassword Confirmació de la contrasenya per seguretat.
     */
    public boolean crearPerfil(String name, String surname, String username, String password, String confirmPassword) {
        boolean st = creador.crearPerfil(persistencia, name, surname, username, password, confirmPassword);
        if (!st) {
            System.out.println("Problema al crear perfil");
        }
        return st;
    }

    // Cas d'ús cargarPerfil
    /**
     * @brief Gestiona el cas d'ús de càrrega d'un perfil.
     * * @param username Nom d'usuari per accedir.
     * @param password Contrasenya associada al compte.
     * @return Retorna l'objecte Perfil si les credencials són vàlides, o null en cas contrari.
     */
    public Perfil cargarPerfil(String username, String password) {
        Perfil p = carregador.carregarPerfil(persistencia, username, password);

        // si p és null no s'ha trobat l'usuari
        if (p == null) {
            System.out.println("Problema al cargar perfil");
        }
        return p;
    }


    // Cas d'ús editarDadesNom
    /**
     * @brief Gestiona el cas d'ús de modificació de dades personals.
     * * @param p El perfil que s'està editant.
     * @param name El nou nom a establir.
     * @param surname El nou cognom a establir.
     */
    public boolean editarDadesPersonals(Perfil p, String name, String surname) {
        boolean st = editor.modificarNom(p, name, surname);
        persistencia.storePerfil(p);
        return st;
    }

    // Cas d'ús editarPassword
    /**
     * @brief Gestiona el cas d'ús de canvi de contrasenya.
     * * @param p El perfil que s'està editant.
     * @param oldPassword Contrasenya actual per a validació.
     * @param newPassword Nova contrasenya desitjada.
     * @param confirmPassword Confirmació de la nova contrasenya.
     */
    public boolean editarPassword(Perfil p, String oldPassword, String newPassword, String confirmPassword) {
        boolean st = editor.modificarPassword(p, oldPassword, newPassword, confirmPassword);
        persistencia.storePerfil(p);
        return st;
    }


    // Funció per quan l'usuari acaba una partida i hem d'actualitzar tots els resultat.
    /**
     * @brief Actualitza el progrés de l'usuari en finalitzar una partida.
     * * Aquesta funció s'encarrega d'invocar al gestor d'estadístiques per registrar
     * els nous resultats i actualitzar els rànquings globals.
     * * @param p El perfil de l'usuari que ha jugat.
     * @param puntuacio Puntuació obtinguda en la partida finalitzada.
     */
    public void finalizarPartida(Perfil p, float puntuacio) {
        gestorStats.actualitzarProgres(p, puntuacio);
        persistencia.storePerfil(p);
        persistencia.guardarRankings();
    }
}
