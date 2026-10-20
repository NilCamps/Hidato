import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class HidatoSolverTest {

    // --- CAS 1: Taulell trivial 1x1 ---
    @Test
    public void testTaulellTrivial() {
        Taulell t = new Taulell(1, 1);
        t.celles[0][0].posarNumero(1);

        Taulell sol = t.ResoldreHidat();

        assertNotNull(sol);
        assertEquals(1, sol.celles[0][0].getValor());
    }

    // --- CAS 2: Taulell ja resolt ---
    @Test
    public void testTaulellJaResolt() {
        Taulell t = new Taulell(2, 2);
        t.celles[0][0].posarNumero(1);
        t.celles[0][1].posarNumero(2);
        t.celles[1][1].posarNumero(3);
        t.celles[1][0].posarNumero(4);

        Taulell sol = t.ResoldreHidat();

        assertNotNull(sol);
        assertEquals(4, sol.buscarPerValor(4).getValor());
    }

    // --- CAS 3: Taulell impossible ---
    @Test
    public void testTaulellImpossible() {
        Taulell t = new Taulell(2, 2);
        t.celles[0][0].posarNumero(1);
        t.celles[1][1].posarNumero(4);

        // No hi ha manera de posar 2 i 3 consecutius
        Taulell sol = t.ResoldreHidat();

        assertNull(sol);
    }

    // --- CAS 4: Taulell amb forats ---
    @Test
    public void testTaulellAmbForats() {
        Taulell t = new Taulell(3, 3);

        t.celles[0][0].posarNumero(1);
        t.celles[1][1].posarForat();
        t.celles[2][2].posarNumero(9);

        Taulell sol = t.ResoldreHidat();

        assertNotNull(sol);
        assertEquals(9, sol.buscarPerValor(9).getValor());
    }

    // --- CAS 5: Taulell mitjà amb solució ---
    @Test
    public void testTaulellMig() {
        Taulell t = new Taulell(3, 3);

        t.celles[0][0].posarNumero(1);
        t.celles[2][2].posarNumero(9);

        Taulell sol = t.ResoldreHidat();

        assertNotNull(sol);
        assertEquals(9, sol.buscarPerValor(9).getValor());
    }

    // --- CAS 6: Taulell gran (stress test) ---
    @Test
    public void testTaulellGran() {
        Taulell t = new Taulell(5, 5);

        t.celles[0][0].posarNumero(1);
        t.celles[4][4].posarNumero(25);

        Taulell sol = t.ResoldreHidat();

        assertNotNull(sol);
        assertEquals(25, sol.buscarPerValor(25).getValor());
    }

    // --- CAS 7: Comprovació que no modifica l'original ---
    @Test
    public void testNoModificaOriginal() {
        Taulell t = new Taulell(3, 3);
        t.celles[0][0].posarNumero(1);

        Taulell copia = t.copia();

        t.ResoldreHidat();

        // L'original ha de romandre intacte
        assertEquals(copia.celles[0][0].getValor(), t.celles[0][0].getValor());
    }

    // --- CAS 8: Comprovació que retorna una còpia ---
    @Test
    public void testRetornaCopia() {
        Taulell t = new Taulell(3, 3);
        t.celles[0][0].posarNumero(1);

        Taulell sol = t.ResoldreHidat();

        assertNotSame(t, sol);
    }
}