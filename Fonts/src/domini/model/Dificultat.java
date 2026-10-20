package domini.model;
/**
 * @class Dificultat
 * @brief Representa la configuracio de dificultat d'una partida.
 * @author Arnau Rodriguez Trocho
 * 
 * Classe que defineix els paràmetres que configuraran la dificultat d'un partida quan la juga un usuari.
 */
public class Dificultat {
    /** @brief Nombre de pistes disponibles per a la partida. */
    private int npistes;

    /** @brief Indica si es permet fer operacions de desfer. */
    private boolean undos;

    /** @brief Multiplicador aplicat segons la dificultat. */
    private float multiplicador;
    
    /**
     * @brief Crea una dificultat facil amb valors per defecte.
     *
     * Inicialitza la dificultat amb 5 pistes, opcio de desfer moviments activada
     * i multiplicador 1.0.
     */
    public Dificultat(){ //Per default construim dif facil.
        this.npistes = 5;
        this.undos = true;
        this.multiplicador = 1.0f;
    }

    /**
     * @brief Crea una dificultat amb els valors indicats.
     *
     * @param n Nombre de pistes disponibles.
     * @param b Cert si es permet desfer moviments.
     * @param f Multiplicador associat a la dificultat.
     */
    public Dificultat(int n, boolean b, float f){
        if ((n == 5 && b && f == 1.0) || (n == 3 && b && f == 1.5) || (n == 0 && !b && f == 2.0)){
            this.npistes = n;
            this.undos = b;
            this.multiplicador = f;
        }
        else {
            System.out.println("No existeix una dificultat amb aquests parametres");
            this.npistes = 5; this.undos = true; this.multiplicador = 1.0f;
        }

    }

    /**
     * @brief Modifica els parametres de la dificultat actual.
     *
     * @param n Nou nombre de pistes disponibles.
     * @param b Nou valor que indica si es permet desfer moviments.
     * @param f Nou multiplicador associat a la dificultat.
     */
    public void modificaDificultat(int n,boolean b, float f){
        if ((n == 5 && b && f == 1.0) || (n == 3 && b && f == 1.5) || (n == 0 && !b && f == 2.0)){
            this.npistes = n;
            this.undos = b;
            this.multiplicador = f;
        }
        else System.out.println("No existeix una dificultat amb aquests parametres, ");
    }

    /**
     * @brief Redueix en 1 el nombre de pistes disponibles si en queden.
     */
    public void consumirPista() {
        if (npistes > 0) {
            npistes--;
        }
    }
    
    /**
     * @brief Retorna el nombre de pistes disponibles.
     *
     * @return El nombre de pistes.
     */
    public int getNpistes() {return npistes; }

    /**
     * @brief Indica si es permet desfer moviments.
     *
     * @return Cert si es poden fer undos, fals altrament.
     */
    public boolean checkUndos() {return undos; }

    /**
     * @brief Retorna el multiplicador de la dificultat.
     *
     * @return El multiplicador associat.
     */
    public float getMultiplicador() {return multiplicador; }

}
