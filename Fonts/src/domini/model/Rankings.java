/**
 * @file Rankings.java
 * @brief Classe que gestiona els rànquings globals del joc Hidato.
 * @author Imanol
 */
package domini.model;

import java.util.ArrayList;

/**
 * @class Rankings
 * @brief Singleton que gestiona les llistes de rankings de puntuació mitjana, partides completades i millor puntuació.
 * * Aquesta classe s'encarrega de mantenir les dades del Top 10 de cada categoria,
 */
public class Rankings{
    /** @brief Instància única del Singleton Rankings */
    private static Rankings unicRankings;

    /** @brief Llista d'entrades per al rànquing de puntuació mitjana */
    private ArrayList<EntradaRanking> rPuntuacioMitjana;
    /** @brief Llista d'entrades per al rànquing de partides completades */
    private ArrayList<EntradaRanking> rPartidesCompletades;
    /** @brief Llista d'entrades per al rànquing de millor puntuació */
    private ArrayList<EntradaRanking> rMillorPuntuacio;

    /**
     * @brief Constructor privat de la classe Rankings.
     * * Inicialitza les llistes de rànquing buides. Es privat per garantir el patró Singleton.
     */
    private Rankings() {
        this.rPuntuacioMitjana = new ArrayList<>();
        this.rPartidesCompletades = new ArrayList<>();
        this.rMillorPuntuacio = new ArrayList<>();
    }


    /**
     * @brief Actualitza el rànquing de partides completades per a un usuari.
     * * Si l'usuari ja existeix, actualitza el seu valor i el reordena cap amunt.
     * Si no existeix, l'afegeix i el col·loca a la posició corresponent.
     * @param pCompletades Nombre de partides completades.
     * @param id Identificador de l'usuari.
     */
    private void actualitzarPartidesCompletades(int pCompletades, String id) {
        // Mirem si l'usuari existeix
        int pos = -1;
        for (int i = 0; i < rPartidesCompletades.size(); i++) {
            if (rPartidesCompletades.get(i).getUsername().equals(id)) {
                pos = i;
                break;
            }
        }

        // si l'usuari ja estabaa
        if (pos != -1) {
            rPartidesCompletades.get(pos).setPuntuacio(pCompletades);
        }
        else {
        // vamos a añadir el elemento, ponemos pos al size del array y lo enviamos al bucle :3

            rPartidesCompletades.add(new EntradaRanking(id, pCompletades));
            pos = rPartidesCompletades.size() - 1;
        }

        for (int i = pos; i > 0; i--) {
            // iterem desde pos cap al principi de l'array fins que pCompletades sigui més petit que a la posició anterior
            EntradaRanking aux = rPartidesCompletades.get(i - 1); // agafem l'anterior
            if (aux.getValor() < pCompletades) {
                rPartidesCompletades.set(i - 1, rPartidesCompletades.get(i));
                rPartidesCompletades.set(i, aux);
            }
            else {
                break;
            }
        }
        while (rPartidesCompletades.size() > 10) {
            rPartidesCompletades.remove(10);
        }

    }

    /**
     * @brief Actualitza el rànquing de millor puntuació.
     * * Afegeix una nova entrada i la reordena. A diferència d'altres rànquings,
     * permet múltiples entrades per al mateix usuari.
     * @param mPuntuacio Puntuació assolida.
     * @param id Identificador de l'usuari.
     */
    private void actualitzarMillorPuntuacio(float mPuntuacio, String id) {
        rMillorPuntuacio.add(new EntradaRanking(id, mPuntuacio));

        for (int i = rMillorPuntuacio.size() - 1; i > 0; i--) {
            // iterem desde pos cap al principi de l'array fins que pCompletades sigui més petit que a la posició anterior
            EntradaRanking aux = rMillorPuntuacio.get(i - 1); // agafem l'anterior
            if (aux.getValor() < mPuntuacio) {
                rMillorPuntuacio.set(i - 1, rMillorPuntuacio.get(i));
                rMillorPuntuacio.set(i, aux);
            }
            else {
                break;
            }
        }
        while (rMillorPuntuacio.size() > 10) {
        rMillorPuntuacio.remove(10);
        }

    }

    /**
     * @brief Actualitza el rànquing de puntuació mitjana amb lògica bidireccional.
     * * Si l'usuari esta al ranquing i la mitjana millora, l'usuari puja. Si empitjora,
     * l'usuari baixa posicions.
     * Si l'usuari no hi era, es comprova si hauria d'estar i el posiciona si ho mereix
     * @param pMitjana Nova puntuació mitjana.
     * @param id Identificador de l'usuari.
     */
    private void actualitzarPuntuacioMitjana(float pMitjana, String id) {
        // Mirem si l'usuari existeix
        int pos = -1;
        for (int i = 0; i < rPuntuacioMitjana.size(); i++) {
            if (rPuntuacioMitjana.get(i).getUsername().equals(id)) {
                pos = i;
                break;
            }
        }

        // si l'usuari ja estaba
        if (pos != -1) {
            boolean improved = (rPuntuacioMitjana.get(pos).getValor() < pMitjana);
            rPuntuacioMitjana.get(pos).setPuntuacio(pMitjana);

            if (improved) {
                for (int i = pos; i > 0; i--) {
                    EntradaRanking aux = rPuntuacioMitjana.get(i - 1);
                    if (aux.getValor() < pMitjana) {
                        rPuntuacioMitjana.set(i - 1, rPuntuacioMitjana.get(i));
                        rPuntuacioMitjana.set(i, aux);
                    }
                    else {
                        break;
                    }
                }
            }
            else {
                for (int i = pos; i < rPuntuacioMitjana.size() - 1; i++) {
                    EntradaRanking aux = rPuntuacioMitjana.get(i + 1);
                    if (aux.getValor() > pMitjana) {
                        rPuntuacioMitjana.set(i + 1, rPuntuacioMitjana.get(i));
                        rPuntuacioMitjana.set(i, aux);
                    }
                    else {
                        break;
                    }
                }
            }
        }
        else {
            rPuntuacioMitjana.add(new EntradaRanking(id, pMitjana));
            pos = rPuntuacioMitjana.size() - 1;

            for (int i = pos; i > 0; i--) {
                EntradaRanking aux = rPuntuacioMitjana.get(i - 1); // agafem l'anterior
                if (aux.getValor() < pMitjana) {
                    rPuntuacioMitjana.set(i - 1, rPuntuacioMitjana.get(i));
                    rPuntuacioMitjana.set(i, aux);
                }
                else {
                    break;
                }
            }
        }

        while (rPuntuacioMitjana.size() > 10) {
            rPuntuacioMitjana.remove(10);
        }

    }

    /**
     * @brief Punt d'entrada públic per actualitzar tots els rànquings d'un usuari.
     * @param username Identificador de l'usuari.
     * @param estadistiques Objecte que conté les mètriques actualitzades.
     */
    public void actualitzarRankings(String username, Estadistiques estadistiques) {
        actualitzarPartidesCompletades(estadistiques.getHidatosCompletats(), username);
        actualitzarMillorPuntuacio(estadistiques.getMillorPuntuacio(),  username);
        actualitzarPuntuacioMitjana(estadistiques.getPuntuacioMitjana(), username);
    }

    /**
     * @brief Obté la instància única de Rankings.
     * @return Retorna la instància unicRankings.
     */
    public static Rankings getInstanciaRankings() {
        if (unicRankings == null) {
            unicRankings = new Rankings();
        }
        return unicRankings;
    }



    // Mètode per poder fer els tests
    /**
     * @brief Reinicia tots els rànquings.
     * * Útil exclusivament per a la realització de proves unitàries
     */
    public void reset() {
        rPuntuacioMitjana.clear();
        rPartidesCompletades.clear();
        rMillorPuntuacio.clear();
    }

    /**
     * @brief Obté la llista d'entrades del rànquing de puntuació mitjana.
     * @return ArrayList amb les entrades del Top 10 de puntuació mitjana.
     */
    public ArrayList<EntradaRanking> getRPuntuacioMitjana() { return rPuntuacioMitjana; }
    /**
     * @brief Obté la llista d'entrades del rànquing de partides completades.
     * @return ArrayList amb les entrades del Top 10 de partides completades.
     */
    public ArrayList<EntradaRanking> getRPartidesCompletades() { return rPartidesCompletades; }
    /**
     * @brief Obté la llista d'entrades del rànquing de millor puntuació.
     * @return ArrayList amb les entrades del Top 10 de millor puntuació.
     */
    public ArrayList<EntradaRanking> getRMillorPuntuacio() { return rMillorPuntuacio; }

    /**
     * @brief Estableix la llista d'entrades del rànquing de puntuació mitjana.
     * * Usat pel controlador de persistència per restaurar l'estat des del fitxer JSON.
     * @param list Llista d'entrades recuperades de disc.
     */
    public void setRPuntuacioMitjana(ArrayList<EntradaRanking> list) { this.rPuntuacioMitjana = list; }
    /**
     * @brief Estableix la llista d'entrades del rànquing de partides completades.
     * * Usat pel controlador de persistència per restaurar l'estat des del fitxer JSON.
     * @param list Llista d'entrades recuperades de disc.
     */
    public void setRPartidesCompletades(ArrayList<EntradaRanking> list) { this.rPartidesCompletades = list; }
    /**
     * @brief Estableix la llista d'entrades del rànquing de millor puntuació.
     * * Usat pel controlador de persistència per restaurar l'estat des del fitxer JSON.
     * @param list Llista d'entrades recuperades de disc.
     */
    public void setRMillorPuntuacio(ArrayList<EntradaRanking> list) { this.rMillorPuntuacio = list; }

}
