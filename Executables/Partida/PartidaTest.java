package executables;

import domini.model.*;
import static org.junit.Assert.*;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class PartidaTest {

    @Test
    public void testConstructorPerDefecte() {
        // La partida per defecte ha de començar amb 100 punts i un taulell 1x1 incomplet
        Partida partida = new Partida();

        assertEquals(100.0f, partida.getPuntuacio(), 0.01);
        assertTrue(partida.isPositionOk(0, 0));
        assertFalse(partida.checkCompletat());
        System.out.println("testConstructorPerDefecte executat correctament");
    }

    @Test
    public void testPenalitzaError() {
        // Cas normal: si la puntuacio continua sent positiva, es resten 10 punts
        Partida partida = new Partida();
        partida.penalitzaError();

        assertEquals(90.0f, partida.getPuntuacio(), 0.01);
        System.out.println("testPenalitzaError executat correctament");
    }

    @Test
    public void testPenalitzaErrorCasLimit() throws Exception {
        // Cas limit: si restar 10 faria la puntuacio no positiva, no s'ha de modificar
        Partida partida = new Partida();
        setField(partida, "puntuacio", 10.0f);

        partida.penalitzaError();

        assertEquals(10.0f, partida.getPuntuacio(), 0.01);
        System.out.println("testPenalitzaErrorCasLimit executat correctament");
    }

    @Test
    public void testComprovaSolucio() throws Exception {
        // Cas normal: es compara el numero amb el taulell resolt
        Partida partida = new Partida();
        Taulell taulellResolt = new Taulell();
        taulellResolt.posarNumero(0, 0, 1);
        setField(partida, "t_resolt", taulellResolt);

        assertTrue(partida.comprovaSolucio(1, 0, 0));
        assertFalse(partida.comprovaSolucio(2, 0, 0));
        System.out.println("testComprovaSolucio executat correctament");
    }

    @Test
    public void testIsNumberAvailable() throws Exception {
        // Cas normal: es consulta si un numero segueix disponible
        Partida partida = new Partida();
        Set<Integer> disponibles = new HashSet<>();
        disponibles.add(1);
        disponibles.add(3);
        setField(partida, "NumerosDisponibles", disponibles);

        assertTrue(partida.isNumberAvailable(1));
        assertFalse(partida.isNumberAvailable(2));
        System.out.println("testIsNumberAvailable executat correctament");
    }

    @Test
    public void testModificaHidato() throws Exception {
        // Cas normal: posar un numero al taulell i eliminar-lo dels disponibles
        Partida partida = new Partida();
        Taulell taulell = new Taulell();
        Set<Integer> disponibles = new HashSet<>();
        disponibles.add(1);

        setField(partida, "t", taulell);
        setField(partida, "NumerosDisponibles", disponibles);

        partida.modificaHidato(1, 0, 0);

        assertEquals(1, taulell.getCella(0, 0).getValor());
        assertFalse(partida.isNumberAvailable(1));
        assertTrue(partida.checkCompletat());
        System.out.println("testModificaHidato executat correctament");
    }

    @Test
    public void testIsPositionOkCasLimit() {
        // Cas limit: una posicio fora del taulell no es valida
        Partida partida = new Partida();

        assertFalse(partida.isPositionOk(-1, 0));
        assertFalse(partida.isPositionOk(0, 1));
        System.out.println("testIsPositionOkCasLimit executat correctament");
    }

    @Test
    public void testCheckCompletatInicialment() {
        // El taulell inicial de la partida per defecte encara no esta complet
        Partida partida = new Partida();

        assertFalse(partida.checkCompletat());
        System.out.println("testCheckCompletatInicialment executat correctament");
    }

    private void setField(Object objecte, String nomCamp, Object valor) throws Exception {
        Field field = objecte.getClass().getDeclaredField(nomCamp);
        field.setAccessible(true);
        field.set(objecte, valor);
    }
}
