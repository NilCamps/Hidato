package Executables.DirectoriHidatos;

import domini.model.DirectoriHidatos;
import domini.model.Taulell;
import org.junit.Test;
import static org.junit.Assert.*;

public class DirectoriHidatosTest {

    @Test
    public void testConstructorBuit() {
        DirectoriHidatos directori = new DirectoriHidatos();
        assertEquals(0, directori.getNumHidatosTotals());
    }

    @Test
    public void testAfegirHidato() {
        DirectoriHidatos directori = new DirectoriHidatos();
        Taulell t1 = new Taulell(10, Taulell.TAdjacencia.C, Taulell.TForma.QUADRAT, 3, 3);
        t1.posarPistaInicial(0, 0, 1);
        
        directori.afegirHidato(t1);
        
        //aixi sabem si es guarda l'hidato
        assertEquals(1, directori.getNumHidatosTotals());
        assertTrue(directori.existeixHidato(10));
        assertEquals(t1, directori.getHidato(10));
    }

    @Test
    public void testSolucioAutomatica() {
        DirectoriHidatos directori = new DirectoriHidatos();
        Taulell t1 = new Taulell(5, Taulell.TAdjacencia.C, Taulell.TForma.QUADRAT, 3, 3);
        t1.posarPistaInicial(0, 0, 1); // Posem una pista inicial perque el solver funcioni i no retorni null
        
        directori.afegirHidato(t1);
        
        //aixi sabem si la solucio es guarda tambe
        assertNotNull(directori.getHidatoSolucio(5));
    }

    @Test
    public void testCodiDuplicat() {
        DirectoriHidatos directori = new DirectoriHidatos();
        Taulell t1 = new Taulell(1, Taulell.TAdjacencia.C, Taulell.TForma.QUADRAT, 3, 3);
        Taulell t2 = new Taulell(1, Taulell.TAdjacencia.C, Taulell.TForma.QUADRAT, 3, 3); // Mateix codi
        
        directori.afegirHidato(t1);
        directori.afegirHidato(t2);
        
        //aixi sabem si fa be la gestió de duplicats
        assertEquals(1, directori.getNumHidatosTotals());
    }

    @Test
    public void testGetHidatoInexistent() {
        DirectoriHidatos directori = new DirectoriHidatos();
        
        //Intentam recuperar un codi que no hem afegit mai
        assertNull(directori.getHidato(999));
        assertFalse(directori.existeixHidato(999));
    }

    @Test
    public void testMultiplesHidatos() {
        DirectoriHidatos directori = new DirectoriHidatos();
        directori.afegirHidato(new Taulell(1, Taulell.TAdjacencia.C, Taulell.TForma.QUADRAT, 3, 3));
        directori.afegirHidato(new Taulell(2, Taulell.TAdjacencia.C, Taulell.TForma.QUADRAT, 3, 3));
        directori.afegirHidato(new Taulell(3, Taulell.TAdjacencia.C, Taulell.TForma.QUADRAT, 3, 3));
        
        //veure si amb multiples funciona be;
        assertEquals(3, directori.getNumHidatosTotals());
    }
}