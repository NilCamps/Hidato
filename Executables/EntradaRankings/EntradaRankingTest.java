package executables;

import model.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class EntradaRankingTest {

    @Test
    public void testConstructoriGetters() {
        // Cas normal --> Tots son casos normals ja que no aqui no es fa cap comprovació
        String id = "User123";
        float punts = 95.5f;
        EntradaRanking entrada = new EntradaRanking(id, punts);

        assertEquals(id, entrada.getUsername());
        assertEquals(punts, entrada.getValor(), 0.01);
    }

    @Test
    public void testCasosLimit() {
        // Cas límit: String buida i puntuació negativa
        // A aquesta clase no es fa cap comprovació, per tant puntuació negativa i sense id s'haurien de poder fer
        EntradaRanking entrada = new EntradaRanking("", -10.0f);
        assertEquals("", entrada.getUsername());
        assertEquals(-10.0f, entrada.getValor(), 0.01);
    }

    @Test
    public void testSetters() {
        // Cas normal: modificació de parametres
        EntradaRanking entrada = new EntradaRanking("Temp", 0.0f);

        entrada.setUsername("aaa");
        entrada.setPuntuacio(100.0f);

        assertEquals("aaa", entrada.getUsername());
        assertEquals(100.0f, entrada.getValor(), 0.01);
    }

}