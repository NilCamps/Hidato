package executables;

import model.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class PerfilTest {

    @Test
    public void testConstructor() {
        // Comprovar  que el constructor funciona bé
        Perfil p = new Perfil("Imanol", "Rodriguez", "iusername", "pass123");

        assertEquals("Imanol", p.getName());
        assertEquals("Rodriguez", p.getSurname());
        assertEquals("iusername", p.getUsername());
        assertEquals("pass123", p.getPassword());
    }

    @Test
    public void testInicializacioEstadistiques() {
        // Com es creen les estadístiques quan creem un perfil, ens hem d'assegurar que s'han creat i que ho fa bé
        Perfil p = new Perfil("A", "B", "user", "pass");

        // comprovem que no sigui NULL
        // a la classe estadistiques ja comprovarem que el constructor posa tots els parametres correctament
        assertNotNull(p.getEstadistiques());
    }

    @Test
    public void testSetters() {
        Perfil p = new Perfil("N", "A", "user", "pass");

        // perfil no fa cap comprovació, és un setter normal
        p.setName("Lu");
        p.setSurname("Xing");
        p.setPassword("nuevaPass");

        assertEquals("Lu", p.getName());
        assertEquals("Xing", p.getSurname());
        assertEquals("nuevaPass", p.getPassword());
    }
    @Test
    public void testCasosLimit() {
        // COmprovació amb parametres buits. Pero, igualment, com ja he posat abans, les comprovacions no es fan aqui
        Perfil p = new Perfil("", "", "", "");
        assertEquals("", p.getName());
        assertEquals("", p.getUsername());
    }
}
