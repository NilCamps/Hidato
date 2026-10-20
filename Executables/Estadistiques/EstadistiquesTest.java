package executables;

import model.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class EstadistiquesTest {

    @Test
    public void testConstructor() {
        // Comprovació de que el constructor va bé
        Perfil p = new Perfil("AA", "BB", "user", "pass");
        Estadistiques e = new Estadistiques(p);

        assertEquals(0, e.getHidatosJugats());
        assertEquals(0, e.getHidatosCompletats());
        assertEquals(0.0f, e.getPuntuacioMitjana(), 0);
        assertEquals(0.0f, e.getMillorPuntuacio(), 0);

        // sobre tot important que la relació Estadistiques <--> Perfil s'ha fet correctament
        assertEquals(p, e.getPerfil());
    }

    @Test
    public void testSettersGetters() {
        Perfil p = new Perfil("A", "B", "user", "pass");
        Estadistiques e = new Estadistiques(p);

        // Cas valors normals
        e.setHidatosJugats(21);
        e.setHidatosCompletats(1);
        e.setPuntuacioMitjana(85.5f);
        e.setMillorPuntuacio(99.0f);

        assertEquals(21, e.getHidatosJugats());
        assertEquals(1, e.getHidatosCompletats());
        assertEquals(85.5f, e.getPuntuacioMitjana(), 0);
        assertEquals(99.0f, e.getMillorPuntuacio(), 0);
    }

    @Test
    public void testCasosLimit() {
        // Com a la resta de clases, la comprovació no es fa a la classe, per tant parametres negatius s'accepten
        Perfil p = new Perfil("A", "B", "user", "pass");
        Estadistiques e = new Estadistiques(p);

        e.setHidatosJugats(-1);
        e.setPuntuacioMitjana(-50.0f);

        assertEquals(-1, e.getHidatosJugats());
        assertEquals(-50.0f, e.getPuntuacioMitjana(), 0);
    }


}