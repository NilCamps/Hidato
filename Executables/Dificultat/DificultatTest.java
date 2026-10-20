package executables;

import domini.model.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class DificultatTest {

    @Test
    public void testConstructorPerDefecte() {
        // La dificultat per defecte ha de ser la facil
        Dificultat dificultat = new Dificultat();

        assertEquals(5, dificultat.getNpistes());
        assertTrue(dificultat.checkUndos());
        assertEquals(1.0f, dificultat.getMultiplicador(), 0.01);
    }

    @Test
    public void testConstructorParametritzatFacil() {
        // Cas valid: dificultat facil
        Dificultat dificultat = new Dificultat(5, true, 1.0f);

        assertEquals(5, dificultat.getNpistes());
        assertTrue(dificultat.checkUndos());
        assertEquals(1.0f, dificultat.getMultiplicador(), 0.01);
    }

    @Test
    public void testConstructorParametritzatMitja() {
        // Cas valid: dificultat mitjana
        Dificultat dificultat = new Dificultat(3, true, 1.5f);

        assertEquals(3, dificultat.getNpistes());
        assertTrue(dificultat.checkUndos());
        assertEquals(1.5f, dificultat.getMultiplicador(), 0.01);
    }

    @Test
    public void testConstructorParametritzatDificil() {
        // Cas valid: dificultat dificil
        Dificultat dificultat = new Dificultat(0, false, 2.0f);

        assertEquals(0, dificultat.getNpistes());
        assertFalse(dificultat.checkUndos());
        assertEquals(2.0f, dificultat.getMultiplicador(), 0.01);
    }

    @Test
    public void testConstructorParametresInvalids() {
        // Cas invalid: si els parametres no existeixen, s'ha de tornar a la facil
        Dificultat dificultat = new Dificultat(4, true, 9.0f);

        assertEquals(5, dificultat.getNpistes());
        assertTrue(dificultat.checkUndos());
        assertEquals(1.0f, dificultat.getMultiplicador(), 0.01);
    }

    @Test
    public void testModificaDificultatAmbParametresValids() {
        // Cas valid: es canvia correctament a una dificultat existent
        Dificultat dificultat = new Dificultat();
        dificultat.modificaDificultat(3, true, 1.5f);

        assertEquals(3, dificultat.getNpistes());
        assertTrue(dificultat.checkUndos());
        assertEquals(1.5f, dificultat.getMultiplicador(), 0.01);
    }

    @Test
    public void testModificaDificultatAmbParametresInvalids() {
        // Cas invalid: no s'haurien de modificar els valors actuals
        Dificultat dificultat = new Dificultat();
        dificultat.modificaDificultat(7, false, 4.0f);

        assertEquals(5, dificultat.getNpistes());
        assertTrue(dificultat.checkUndos());
        assertEquals(1.0f, dificultat.getMultiplicador(), 0.01);
    }
}
