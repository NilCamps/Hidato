package domini.model;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import domini.algorismes.*;

/**
 * @class DirectoriHidatos
 * @brief Emmagatzema i gestiona el catàleg de taulells Hidato i les seves respectives solucions.
 * 
 * @details Aquesta classe actua com a memòria del sistema de taulells.
 * Implementa dos mapes estructurals vinculats lògicament a través del codi únic de cada Hidato.
 * Per al directori hem utilitzat un HashMap per emmagatzemar els taulells, ja que ens interessa tenir-los associats a un ID 
 * i fer cerques ràpides quan l'usuari en triï un.
 * 
 * @author Carlos
 */
public class DirectoriHidatos {
    private HashMap<Integer, Taulell> DtH;
    private HashMap<Integer, Taulell> DtHs;
    private int maxCodi;


    /**
     * @brief Constructor per defecte.
     * @details Inicialitza un directori completament buit.
     */
    public DirectoriHidatos() {
        DtH = new HashMap<Integer, Taulell>();
        DtHs = new HashMap<Integer, Taulell>();
        maxCodi = 0;
    }

    /**
     * @brief Registra un nou Hidato al directori.
     * @details Verifica l'existència del codi. Si no existeix, resol el taulell 
     * mitjançant l'algorisme de solució i desa ambdues versions. Això fa que quan es torni accedir a un mateix hidato
     * no s'ha de resoldre cada cop, simplement imprimir la solucio.
     */
    public void afegirHidato(Taulell t) {
        int codi = t.getCodi();
        if (DtH.containsKey(codi)) {
            System.out.println("Ja existeix un Hidato amb aquest Codi.");
            return;
        }
        SolucionarHidato sh = new SolucionarHidato();
        Taulell tsol = sh.SolucionarHidatoTaulell(t);
        DtH.put(codi, t);
        DtHs.put(codi, tsol);
        if (codi >= maxCodi) maxCodi = codi;
    }

    /**
     * @brief Obté la quantitat de taulells gestionats actualment.
     * @return El nombre enter de Hidatos registrats en el mapa principal.
     */
    public int getNumHidatosTotals() {
        return DtH.size();
    }

    /**
     * @brief Recupera un taulell original específic mitjançant el seu identificador.
     * @return L'objecte Taulell corresponent, o null si la clau no existeix.
     */
    public Taulell getHidato(int codi) {
        return DtH.get(codi);
    }

    /**
     * @brief Verifica l'existència d'un taulell al sistema.
     * @return true si el directori conté un Hidato amb aquest codi, false si no.
     */
    public boolean existeixHidato(int codi) {
        return DtH.get(codi) != null;
    }

    /**
     * @brief Recupera un taulell solucionat específic mitjançant el seu identificador.
     * @return L'objecte Taulell corresponent, o null si la clau no existeix.
     */
    public Taulell getHidatoSolucio(int codi) {
        return DtHs.get(codi);
    }

    /**
     * @brief Bolca per sortida estàndard la informació de tots els taulells 
     * per a que l'usuari pugui saber quins hidatos hi ha al directori.
     * @details Itera el directori principal i invoca la impressió de cada taulell.
     */
    public HashMap<Integer, Taulell> getTaulells() {
        return DtH;
    }

    public void getTotsHidatosE() {
        Iterator<Map.Entry<Integer, Taulell>> it = DtH.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<Integer, Taulell> entry = it.next();
            
            Integer clave = entry.getKey();
            Taulell valor = entry.getValue();
            
            System.out.println("Codi: " + clave);
            valor.imprimirTaulell();
        }
    }
}