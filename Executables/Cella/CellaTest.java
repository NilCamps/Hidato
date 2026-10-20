package Executables.Cella;

import domini.model.Cella;
import org.junit.Test;

import static org.junit.Assert.*;

public class CellaTest {

    @Test
    public void testConstructorCelLaBuida() {
        Cella c = new Cella(1, 2, Cella.BUIDA);

        assertEquals(1, c.getPosX());
        assertEquals(2, c.getPosY());
        assertEquals(0, c.getValor());
        assertTrue(c.esBuida());
        assertFalse(c.esForat());
        assertFalse(c.teValor());
        assertFalse(c.esInicial());
        assertTrue(c.esEditable());
    }

    @Test
    public void testConstructorCelLaInicial() {
        Cella c = new Cella(0, 0, 5, true);

        assertEquals(5, c.getValor());
        assertTrue(c.esInicial());
        assertFalse(c.esEditable());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorPosicioNegativa() {
        new Cella(-1, 0, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorValorInvalid() {
        new Cella(0, 0, -2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorInicialAmbValorNoPositiu() {
        new Cella(0, 0, 0, true);
    }

    @Test
    public void testPosarNumeroCorrecte() {
        Cella c = new Cella(0, 0, 0);
        c.posarNumero(4);

        assertEquals(4, c.getValor());
        assertTrue(c.teValor());
        assertFalse(c.esEditable());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPosarNumeroNoPositiu() {
        Cella c = new Cella(0, 0, 0);
        c.posarNumero(0);
    }

    @Test(expected = IllegalStateException.class)
    public void testPosarNumeroEnInicial() {
        Cella c = new Cella(0, 0, 7, true);
        c.posarNumero(8);
    }

    @Test(expected = IllegalStateException.class)
    public void testPosarNumeroEnForat() {
        Cella c = new Cella(0, 0, Cella.FORAT);
        c.posarNumero(3);
    }

    @Test(expected = IllegalStateException.class)
    public void testPosarNumeroEnCelLaAmbValor() {
        Cella c = new Cella(0, 0, 2);
        c.posarNumero(3);
    }

    @Test
    public void testBuidarCelLaEditable() {
        Cella c = new Cella(0, 0, 0);
        c.buidar();

        assertEquals(0, c.getValor());
        assertTrue(c.esBuida());
    }

    @Test(expected = IllegalStateException.class)
    public void testBuidarCelLaInicial() {
        Cella c = new Cella(0, 0, 5, true);
        c.buidar();
    }

    @Test
    public void testCopia() {
        Cella original = new Cella(1, 1, 9, true);
        Cella copia = original.copia();

        assertNotSame(original, copia);
        assertEquals(original.getPosX(), copia.getPosX());
        assertEquals(original.getPosY(), copia.getPosY());
        assertEquals(original.getValor(), copia.getValor());
        assertEquals(original.esInicial(), copia.esInicial());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorCopiaNull() {
        new Cella((Cella) null);
    }

    @Test
    public void testToTokenFormatForat() {
        Cella c = new Cella(0, 0, Cella.FORAT);
        assertEquals("*", c.toTokenFormat());
    }

    @Test
    public void testToTokenFormatBuida() {
        Cella c = new Cella(0, 0, Cella.BUIDA);
        assertEquals("?", c.toTokenFormat());
    }

    @Test
    public void testToTokenFormatAmbValor() {
        Cella c = new Cella(0, 0, 7);
        assertEquals("7", c.toTokenFormat());
    }

    @Test(expected = IllegalStateException.class)
    public void testBuidarCelLaAmbValor() {
        Cella c = new Cella(0, 0, 3);
        c.buidar();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorPosicioYNegativa() {
        new Cella(0, -1, 0);
    }

    @Test
    public void testEsEditableFalseEnForat() {
        Cella c = new Cella(0, 0, Cella.FORAT);
        assertFalse(c.esEditable());
    }
}
