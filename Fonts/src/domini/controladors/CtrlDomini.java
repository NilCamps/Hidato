package domini.controladors;
import java.util.ArrayList;
import java.util.HashMap;
import domini.model.Perfil;
import domini.model.Supervivencia;
import domini.model.Contrarrellotge;
import domini.model.Dificultat;
import domini.model.DirectoriHidatos;
import domini.model.Taulell;
import domini.model.Taulell.TAdjacencia;
import domini.model.Taulell.TForma;
import domini.model.Partida;
import domini.algorismes.ValidarHidato;
import domini.controladors.CtrlConfiguracio.TPartida;
import domini.algorismes.ComprovarHidato;
import domini.algorismes.SolucionarHidato;
import domini.algorismes.CrearHidato;

/**
 * @class CtrlDomini
 * @brief Controlador principal del sistema
 * @details Gestiona la sessió de l'usuari actiu i crida les operacions
 * específiques als controladors corresponents.
 */
public class CtrlDomini {

    /** @brief Directori que conté tots els taulells Hidato disponibles al sistema. */
    private DirectoriHidatos DtH;

    /** @brief Perfil de l'usuari amb sessió iniciada. Null si no hi ha cap sessió activa. */
    private Perfil usuariActual;

    /** @brief Controlador encarregat de la gestió de perfils d'usuari. */
    private CtrlPerfil ctrlPerfil;

    /** @brief Controlador encarregat de la gestió de la persistència. */
    private CtrlPersistencia ctrlPersistencia;

    private CtrlConfiguracio ctrlConfiguracio;
    
    private Partida partidaActiva;
    
    private CtrlPartida ctrlPartidaActiu;

    /**
     * @brief Constructor per defecte de CtrlDomini.
     * @details Inicialitza el directori de taulells, estableix l'usuari actiu
     * a null i instancia el controlador de perfils.
     */
    public CtrlDomini() {
        DtH = new DirectoriHidatos();
        usuariActual = null;
        ctrlPersistencia = new CtrlPersistencia();
        ctrlPersistencia.cargarRankings();
        ctrlPerfil = new CtrlPerfil(ctrlPersistencia);
        ctrlConfiguracio = new CtrlConfiguracio();
        carregarHidatosInicials();
    }

    private void carregarHidatosInicials() {
        // Hidato 1 — QUADRAT 3×3, cares (4 caselles buides)
        afegirHidatoDeText(1, java.util.Arrays.asList(
            "Q,C,3,3",
            "1,?,3",
            "?,5,?",
            "7,?,9"
        ));
        // Hidato 2 — QUADRAT 4×4, cares (8 caselles buides)
        afegirHidatoDeText(2, java.util.Arrays.asList(
            "Q,C,4,4",
            "1,?,?,4",
            "?,?,?,5",
            "9,?,?,12",
            "16,?,?,13"
        ));
        // Hidato 3 — TRIANGLE 2×6, CA costats+diagonals directes (6 caselles buides)
        // Camí: (0,5)1→(0,4)2→(1,5)3→(1,4)4→(0,3)5→(0,2)6→(1,3)7→(1,2)8→(0,1)9→(0,0)10→(1,0)11→(1,1)12
        afegirHidatoDeText(3, java.util.Arrays.asList(
            "T,CA,2,6",
            "?,9,?,5,?,1",
            "11,?,8,?,4,?"
        ));
        // Hidato 4 — HEXÀGON 3×4, cares (6 caselles buides)
        afegirHidatoDeText(4, java.util.Arrays.asList(
            "H,C,3,4",
            "1,?,?,4",
            "?,?,?,?",
            "9,?,?,12"
        ));
    }

    private void afegirHidatoDeText(int codi, java.util.List<String> linies) {
        try {
            Taulell t = Taulell.carregarDesDeLinies(linies, codi, true);
            DtH.afegirHidato(t);
        } catch (Exception e) {
            System.out.println("Error carregant hidato " + codi + ": " + e.getMessage());
        }
    }

    /**
     * @brief Creació d'un nou perfil d'usuari.
     * @details Crida a crearPerfil del controlador de perfils.
     * @param name Nom de l'usuari.
     * @param surname Cognom de l'usuari.
     * @param username Identificador únic.
     * @param password Contrasenya del nou compte.
     * @param password2 Confirmació de la contrasenya.
     */
    public boolean crearPerfil(String name, String surname, String username, String password1, String password2) {
        return ctrlPerfil.crearPerfil(name, surname, username, password1, password2);
    }

    /**
     * @brief Inici de sessió d'un usuari existent.
     * @details Si les credencials són vàlides, estableix l'usuari com a sessió activa.
     * @param username Nom d'usuari per accedir.
     * @param password Contrasenya del compte.
     * @return true si les credencials són correctes i la sessió s'ha iniciat, false en cas contrari.
     */
    public boolean cargarPerfil(String username, String password) {
        Perfil p = ctrlPerfil.cargarPerfil(username, password);
        if (p != null) {
            usuariActual = p;
            return true;
        }
        return false;
    }

    /**
     * @brief Tanca la sessió de l'usuari actiu.
     * @details Estableix l'usuari actual a null, deixant el sistema sense sessió activa.
     */
    public void tancarSessio() {
        usuariActual = null;
    }

    public Perfil getUsuariActual() {
        return usuariActual;
    }

    public HashMap<Integer, Taulell> getHidatos() {
        return DtH.getTaulells();
    }

    public String validarHidato(int codi) {
        Taulell t = DtH.getHidato(codi);
        if (t == null) return "No existeix cap hidato amb el codi " + codi + ".";
        ValidarHidato vh = new ValidarHidato();
        if (!vh.validarHidato(t))
            return "L'hidato #" + codi + " no té una estructura vàlida.";
        SolucionarHidato sh = new SolucionarHidato();
        if (sh.solucionarHidatoBoolean(t))
            return "L'hidato #" + codi + " és vàlid i té solució única.";
        return "L'hidato #" + codi + " és estructuralment vàlid però no té solució única.";
    }

    public String comprovarHidato(int codi) {
        Taulell t = DtH.getHidato(codi);
        if (t == null) return "No existeix cap hidato amb el codi " + codi + ".";
        switch (ComprovarHidato.comprovar(t)) {
            case CORRECTE:           return "L'hidato #" + codi + " és una solució correcta!";
            case NO_COMPLET:         return "L'hidato #" + codi + " no està completament omplert.";
            case ESTRUCTURA_INVALIDA: return "L'hidato #" + codi + " té una estructura invàlida.";
            case SOLUCIO_INCORRECTA: return "L'hidato #" + codi + " té la seqüència numèrica incorrecta.";
            default:                 return "Error desconegut.";
        }
    }

    public boolean editarDadesPersonals(String name, String surname) {
        if (usuariActual == null) return false;
        return ctrlPerfil.editarDadesPersonals(usuariActual, name, surname);
    }

    public boolean editarPassword(String oldPassword, String newPassword, String confirmPassword) {
        if (usuariActual == null) return false;
        return ctrlPerfil.editarPassword(usuariActual, oldPassword, newPassword, confirmPassword);
    }

    /**
     * @brief Notifica al sistema que una partida ha finalitzat.
     * @details Si hi ha un usuari amb sessió activa, actualitza les seves estadístiques
     * amb la puntuació obtinguda.
     * @param puntuacio Puntuació obtinguda.
     */
    public void partidaFinalitzada(float puntuacio) {
        if (usuariActual != null) {
            ctrlPerfil.finalizarPartida(usuariActual, puntuacio);
        }
    }

    public ArrayList<String> getPartidesGuardades() {
        return ctrlPersistencia.getPartidesUsuari(usuariActual.getUsername());
    }

    public domini.model.Taulell getHidatoSolucio(int codi) {
        return DtH.getHidatoSolucio(codi);
    }

    public Taulell[] crearHidatoNou(Taulell.TForma forma, Taulell.TAdjacencia adj, int nCaselles) {
        CrearHidato creator = new CrearHidato();
        Taulell puzzle = creator.crearHidato(forma, adj, nCaselles);
        if (puzzle == null) return null;
        Taulell solucio = creator.getSolucio();
        if (solucio == null) return null;
        return new Taulell[]{puzzle, solucio};
    }

    public void configurarPartida(TPartida mode, int difIdx, TForma forma, TAdjacencia adj, int nCaselles, int codiHidato, Integer tempsMinuts) {
        ctrlConfiguracio.configurar(mode, difIdx, forma, adj, nCaselles, tempsMinuts, codiHidato);
    }

    public void guardarPartida(Partida p){
        ctrlPersistencia.storePartida(usuariActual.getUsername(), null, p);
    }

    public Partida carregarPartida(String usuari,String nompartida){
        if (usuariActual == null || !usuari.equals(usuariActual.getUsername())) {
            return null;
        }
        return ctrlPersistencia.loadPartida(usuari,nompartida);
    }

    public Partida iniciarPartida() {
        TPartida mode    = ctrlConfiguracio.getTipusPartida();
        Dificultat d     = ctrlConfiguracio.getD();
        TForma forma     = ctrlConfiguracio.getForma();
        TAdjacencia adj  = ctrlConfiguracio.getAdjacencia();
        int nCaselles    = ctrlConfiguracio.getNCaselles();
        int codiHidato   = ctrlConfiguracio.getCodiHidato();
        
        Taulell tBuit, tResolt;
        if (codiHidato > 0) {
            tBuit  = DtH.getHidato(codiHidato);
            tResolt = DtH.getHidatoSolucio(codiHidato);
            if (tBuit == null || tResolt == null) return null;
        } else {
            Taulell[] ts = crearHidatoNou(forma, adj, nCaselles);
            if (ts == null) return null;
            tBuit   = ts[0];
            tResolt = ts[1];
        }
        
        if (mode == TPartida.CONTRARRELLOTGE) {
            CtrlContrarrellotge ctrl = new CtrlContrarrellotge();
            partidaActiva = ctrl.jugar(tBuit, d, ctrlConfiguracio.getTimecontra());
            ctrlPartidaActiu = ctrl;
        } else if (mode == TPartida.SUPERVIVENCIA) {
            ctrlPartidaActiu = new CtrlSupervivencia();
            partidaActiva = ctrlPartidaActiu.jugar(tBuit, d);
        } else {
            ctrlPartidaActiu = new CtrlNormal();
            partidaActiva = ctrlPartidaActiu.jugar(tBuit, d);
        }
        
        return partidaActiva;
    }

    public boolean processarMoviment(int n, int x, int y) {
        if (ctrlPartidaActiu == null || partidaActiva == null) return false;
        return ctrlPartidaActiu.processarMoviment(partidaActiva, n, x, y);
    }

    public void setPartidaActiva(Partida p) {
        this.partidaActiva = p;
        if (p instanceof Contrarrellotge)    ctrlPartidaActiu = new CtrlContrarrellotge();
        else if (p instanceof Supervivencia) ctrlPartidaActiu = new CtrlSupervivencia();
        else                                 ctrlPartidaActiu = new CtrlNormal();
    }
    
    public Partida getPartidaActiva() { return partidaActiva; }

    public TPartida getModePartida(){
        return ctrlConfiguracio.getTipusPartida();
    }
}