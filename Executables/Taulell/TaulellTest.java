package Executables.Taulell;

import domini.model.Cella;
import domini.model.Taulell;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class TaulellTest {

    private Taulell taulellQuadrat3x3() {
        return new Taulell(1, Taulell.TAdjacencia.C, Taulell.TForma.QUADRAT, 3, 3);
    }

    @Test
    public void testConstructorValid() {
        Taulell t = new Taulell(7, Taulell.TAdjacencia.CA, Taulell.TForma.QUADRAT, 2, 4);

        assertEquals(7, t.getCodi());
        assertEquals(2, t.getFiles());
        assertEquals(4, t.getColumnes());
        assertEquals(Taulell.TAdjacencia.CA, t.getAdjacencia());
        assertEquals(Taulell.TForma.QUADRAT, t.getForma());
        assertEquals(8, t.getNCaselles());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorAdjacenciaNull() {
        new Taulell(1, null, Taulell.TForma.QUADRAT, 2, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorFormaNull() {
        new Taulell(1, Taulell.TAdjacencia.C, null, 2, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorMidaInvalida() {
        new Taulell(1, Taulell.TAdjacencia.C, Taulell.TForma.QUADRAT, 0, 2);
    }

    @Test
    public void testPosicioValida() {
        Taulell t = taulellQuadrat3x3();

        assertTrue(t.posicioValida(0, 0));
        assertTrue(t.posicioValida(2, 2));
        assertFalse(t.posicioValida(-1, 0));
        assertFalse(t.posicioValida(3, 0));
        assertFalse(t.posicioValida(0, 3));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetCellaForaLimits() {
        Taulell t = taulellQuadrat3x3();
        t.getCella(5, 5);
    }

    @Test
    public void testMarcarForaArea() {
        Taulell t = taulellQuadrat3x3();
        t.marcarForaArea(1, 1);

        assertTrue(t.esForaArea(1, 1));
        assertEquals(8, t.getNCaselles());
    }

    @Test
    public void testConvertirEnForat() {
        Taulell t = taulellQuadrat3x3();
        t.convertirEnForat(0, 0);

        assertTrue(t.getCella(0, 0).esForat());
        assertFalse(t.esCasellaValida(0, 0));
        assertEquals(8, t.getNCaselles());
    }

    @Test
    public void testPosarPistaInicial() {
        Taulell t = taulellQuadrat3x3();
        t.posarPistaInicial(0, 1, 3);

        assertEquals(3, t.getCella(0, 1).getValor());
        assertTrue(t.getCella(0, 1).esInicial());
        assertFalse(t.esCasellaUsable(0, 1));
    }

    @Test(expected = IllegalStateException.class)
    public void testPosarPistaInicialEnForat() {
        Taulell t = taulellQuadrat3x3();
        t.convertirEnForat(1, 1);
        t.posarPistaInicial(1, 1, 4);
    }

    @Test
    public void testEsCasellaUsable() {
        Taulell t = taulellQuadrat3x3();
        assertTrue(t.esCasellaUsable(1, 1));

        t.posarPistaInicial(0, 0, 1);
        assertFalse(t.esCasellaUsable(0, 0));

        t.convertirEnForat(0, 1);
        assertFalse(t.esCasellaUsable(0, 1));

        t.posarNumero(1, 1, 2);
        assertFalse(t.esCasellaUsable(1, 1));
    }

    @Test
    public void testPotPosarNumeroFalseSiDuplicat() {
        Taulell t = taulellQuadrat3x3();
        t.posarNumero(0, 0, 5);

        assertFalse(t.potPosarNumero(1, 1, 5));
    }

    @Test
    public void testPosarNumeroCorrecte() {
        Taulell t = taulellQuadrat3x3();
        t.posarNumero(1, 1, 4);

        assertEquals(4, t.getCella(1, 1).getValor());
    }

    @Test(expected = IllegalStateException.class)
    public void testPosarNumeroInvalid() {
        Taulell t = taulellQuadrat3x3();
        t.posarNumero(0, 0, 3);
        t.posarNumero(1, 1, 3);
    }

    @Test
    public void testBuscarPerValor() {
        Taulell t = taulellQuadrat3x3();
        t.posarNumero(2, 1, 7);

        Cella c = t.buscarPerValor(7);
        assertNotNull(c);
        assertEquals(2, c.getPosX());
        assertEquals(1, c.getPosY());
    }

    @Test
    public void testNumerosDisponibles() {
        Taulell t = taulellQuadrat3x3();
        t.posarPistaInicial(0, 0, 1);
        t.posarNumero(0, 1, 2);

        Set<Integer> disp = t.NumerosDisponibles();
        assertFalse(disp.contains(1));
        assertFalse(disp.contains(2));
        assertTrue(disp.contains(3));
        assertTrue(disp.contains(9));
    }

    @Test
    public void testCompletatFalse() {
        Taulell t = taulellQuadrat3x3();
        assertFalse(t.Completat());
    }

    @Test
    public void testCompletatTrue() {
        Taulell t = new Taulell(1, Taulell.TAdjacencia.CA, Taulell.TForma.QUADRAT, 2, 2);
        t.posarNumero(0, 0, 1);
        t.posarNumero(0, 1, 2);
        t.posarNumero(1, 0, 3);
        t.posarNumero(1, 1, 4);

        assertTrue(t.Completat());
    }

    @Test
    public void testValorsDuplicats() {
        Taulell t = new Taulell(1, Taulell.TAdjacencia.C, Taulell.TForma.QUADRAT, 2, 2);
        String[][] tokens = {{"1", "2"}, {"2", "4"}};
        t.carregarDesDeFormatExtern(tokens, false);

        assertTrue(t.teValorsDuplicats());
    }

    @Test
    public void testValorsForaDeRang() {
        Taulell t = new Taulell(1, Taulell.TAdjacencia.C, Taulell.TForma.QUADRAT, 2, 2);
        String[][] tokens = {{"1", "2"}, {"3", "9"}};
        t.carregarDesDeFormatExtern(tokens, false);

        assertTrue(t.teValorsForaDeRang());
    }

    @Test
    public void testTaulellNoConnectat() {
        Taulell t = taulellQuadrat3x3();
        t.marcarForaArea(1, 0);
        t.marcarForaArea(1, 1);
        t.marcarForaArea(1, 2);

        assertFalse(t.esTaulellConnectat());
    }

    @Test
    public void testObtenirVeinesQuadratCantonada() {
        Taulell t = new Taulell(1, Taulell.TAdjacencia.C, Taulell.TForma.QUADRAT, 3, 3);
        List<Cella> veines = t.obtenirVeines(0, 0);

        assertEquals(2, veines.size());
    }

    @Test
    public void testObtenirVeinesQuadratCentreCA() {
        Taulell t = new Taulell(1, Taulell.TAdjacencia.CA, Taulell.TForma.QUADRAT, 3, 3);
        List<Cella> veines = t.obtenirVeines(1, 1);

        assertEquals(8, veines.size());
    }

    @Test
    public void testCarregarDesDeLiniesAmbInicials() {
        List<String> linies = java.util.Arrays.asList(
                "Q,CA,2,2",
                "1,?",
                "?,4"
        );

        Taulell t = Taulell.carregarDesDeLinies(linies, 99, true);
        assertTrue(t.getCella(0, 0).esInicial());
        assertTrue(t.getCella(1, 1).esInicial());
        assertFalse(t.getCella(0, 1).esInicial());
    }

    @Test
    public void testCarregarDesDeLiniesFormatEspecial() {
        List<String> linies = java.util.Arrays.asList(
                "Q,C,2,3",
                "#,*,?",
                "1,?,2"
        );

        Taulell t = Taulell.carregarDesDeLinies(linies, 10, false);
        assertNull(t.getCella(0, 0));
        assertTrue(t.getCella(0, 1).esForat());
        assertTrue(t.getCella(0, 2).esBuida());
        assertEquals(1, t.getCella(1, 0).getValor());
    }

    @Test
    public void testEsEstructuraValidaTrue() {
        Taulell t = new Taulell(1, Taulell.TAdjacencia.CA, Taulell.TForma.QUADRAT, 2, 2);
        t.posarPistaInicial(0, 0, 1);

        assertTrue(t.esEstructuraValida());
    }

    @Test
    public void testEsEstructuraValidaFalsePerBuit() {
        Taulell t = new Taulell(1, Taulell.TAdjacencia.C, Taulell.TForma.QUADRAT, 2, 2);

        assertFalse(t.esEstructuraValida());
    }

    @Test
    public void testEstaBuitTrue() {
        Taulell t = new Taulell(1, Taulell.TAdjacencia.C, Taulell.TForma.QUADRAT, 2, 2);
        assertTrue(t.estaBuit());
    }

    @Test
    public void testTePistesInicialsTrue() {
        Taulell t = new Taulell(1, Taulell.TAdjacencia.C, Taulell.TForma.QUADRAT, 2, 2);
        t.posarPistaInicial(0, 0, 1);

        assertTrue(t.tePistesInicials());
    }

    @Test(expected = IllegalStateException.class)
    public void testBuidarCasellaAmbNumeroLlanCaExcepcio() {
        Taulell t = new Taulell(1, Taulell.TAdjacencia.C, Taulell.TForma.QUADRAT, 2, 2);
        t.posarNumero(0, 0, 1);
        t.buidarCasella(0, 0);
    }

    @Test
    public void testBuscarPerValorNoTrobat() {
        Taulell t = taulellQuadrat3x3();
        assertNull(t.buscarPerValor(99));
    }   

    @Test
    public void testCopiaTaulellIndependent() {
        Taulell original = new Taulell(1, Taulell.TAdjacencia.C, Taulell.TForma.QUADRAT, 2, 2);
        original.posarPistaInicial(0, 0, 1);

        Taulell copia = original.copia();

        assertNotSame(original, copia);
        assertNotSame(original.getCella(0, 0), copia.getCella(0, 0));
        assertEquals(original.getCella(0, 0).getValor(), copia.getCella(0, 0).getValor());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCarregarDesDeLiniesCapcaleraIncorrecta() {
        List<String> linies = java.util.Arrays.asList(
                "Q,C,2",
                "1,?",
                "?,2"
        );

        Taulell.carregarDesDeLinies(linies, 1, false);
    }

    
}
