package executables;

import model.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class RankingsTest {

    private Rankings r;

    @Test
    public void testSingleton() {
        // Com es un singleton hauria de retornar sempre la mateixa instància
        Rankings r1 = Rankings.getInstanciaRankings();
        Rankings r2 = Rankings.getInstanciaRankings();
        assertSame(r1, r2);
    }

    @Before
    public void setUp() {
        //  Preparem l'entorn abans de cada test
        r = Rankings.getInstanciaRankings();
        r.reset();
    }

    @Test
    public void testAfegirUnUsuari() {
        // Afegir un usuari i mirar que tot va bé
        Perfil p = new Perfil("N", "A", "user1", "p");
        p.getEstadistiques().setHidatosCompletats(5);

        r.actualitzarRankings(p.getUsername(), p.getEstadistiques());

        ArrayList<EntradaRanking> llista = r.getRankingPartidesCompletades();
        assertEquals(1, llista.size());
        assertEquals("user1", llista.get(0).getUsername());
        assertEquals(5.0f, llista.get(0).getValor(), 0);
    }

    @Test
    public void testAfegirSegonUsuariOrdenat() {
        // Afegir un segon usuari (ha de quedar ordenat)
        Perfil p1 = new Perfil("N", "A", "user1", "p");
        p1.getEstadistiques().setHidatosCompletats(5);

        Perfil p2 = new Perfil("N", "A", "user2", "p");
        p2.getEstadistiques().setHidatosCompletats(10);

        r.actualitzarRankings(p1.getUsername(), p1.getEstadistiques());
        r.actualitzarRankings(p2.getUsername(), p2.getEstadistiques());

        ArrayList<EntradaRanking> llista = r.getRankingPartidesCompletades();
        assertEquals("user2", llista.get(0).getUsername());
        assertEquals("user1", llista.get(1).getUsername());
    }

    @Test
    public void testLimitTop10() {
        // Omplir fins a 10 i intentar afegir un 11 que NO entra
        for (int i = 1; i <= 10; i++) {
            r.actualitzarRankings("user" + i, crearStats(i + 10)); // Puntuacions de 11 a 20
        }

        // Intentem afegir un usuari amb puntuació baixa (no hauria d'entrar)
        r.actualitzarRankings("dolent", crearStats(5));

        ArrayList<EntradaRanking> llista = r.getRankingPartidesCompletades();
        assertEquals(10, llista.size()); // Mantenim el límit

        // Comprovem que l'usuari "dolent" no és a la llista
        for (EntradaRanking er : llista) {
            assertNotEquals("dolent", er.getUsername());
        }
    }

    @Test
    public void testEntradaNouTopUser() {
        // Afegir un 11 que SI ha d'entrar
        for (int i = 1; i <= 10; i++) {
            r.actualitzarRankings("user" + i, crearStats(i + 10));
        }

        // Aquest usuari té 50 punts, ha d'anar directe al número 1
        r.actualitzarRankings("top", crearStats(50));

        ArrayList<EntradaRanking> llista = r.getRankingPartidesCompletades();
        assertEquals(10, llista.size());
        assertEquals("top", llista.get(0).getUsername());
        // user 1que tenia 11 punts hauria de no estar
        for (EntradaRanking er : llista) {
            assertNotEquals("user1", er.getUsername());
        }
    }

    @Test
    public void testBaixarMitjanaLlistaNoPlena() {
        // posem dos usuaris
        r.actualitzarRankings("UserA", crearStats(90));
        r.actualitzarRankings("UserB", crearStats(80));

        assertEquals("UserA", r.getRankingPuntuacioMitjana().get(0).getUsername());

        // L'usuari A empitjora la seva mitjana a 70. Ara ha de baixar al lloc 1
        r.actualitzarRankings("UserA", crearStats(70));

        ArrayList<EntradaRanking> llista = r.getRankingPuntuacioMitjana();
        assertEquals("UserB", llista.get(0).getUsername()); // B ara és primer
        assertEquals("UserA", llista.get(1).getUsername()); // A ha baixat al segon lloc
        assertEquals(70.0f, llista.get(1).getValor(), 0);
    }


    @Test
    public void testBaixarMitjanaLlistaPlena() {
        // Omplim el Top 10 amb puntuacions de 100 a 10
        for (int i = 0; i < 10; i++) {
            r.actualitzarRankings("User" + i, crearStats(100 - (i * 10)));
        }

        // User0 té 100 punts i està a la pos 0.
        assertEquals("User0", r.getRankingPuntuacioMitjana().get(0).getUsername());

        // User0 cau a 5 punts. Ha d'anar a l'ultima posicio
        r.actualitzarRankings("User0", crearStats(5));

        ArrayList<EntradaRanking> llista = r.getRankingPuntuacioMitjana();
        assertEquals(10, llista.size());
        assertEquals("User1", llista.get(0).getUsername()); // El que tenia 90 ara és primer
        assertEquals("User0", llista.get(9).getUsername()); // El que ha baixat és l'últim
        assertEquals(5.0f, llista.get(9).getValor(), 0);
    }


    @Test
    public void testMillorPuntuacioEntradaISortida() {
        // Omplim el ranking
        for (int i = 1; i <= 10; i++) {
            r.actualitzarRankings("user" + i, crearStats(i + 10));
        }

        // Afegim una puntuació que NO hauria d'entrar
        r.actualitzarRankings("Noob", crearStats(5));

        ArrayList<EntradaRanking> llista = r.getRankingMillorPuntuacio();
        assertEquals(10, llista.size());
        for (EntradaRanking er : llista) {
            assertNotEquals("Noob", er.getUsername());
        }

        // Afegim una puntuació que SI ha d'entrar
        r.actualitzarRankings("pro", crearStats(100));

        llista = r.getRankingMillorPuntuacio();
        assertEquals("pro", llista.get(0).getUsername()); // Entra a pos 0
        assertEquals(10, llista.size()); // Segueix amb size = 10
    }

    @Test
    public void testMillorPuntuacioRepetits() {
        // Verifiquem que el rànquing de Millor Puntuació SI accepta el mateix ID diversos cops
        r.actualitzarRankings("UserA", crearStats(100));
        r.actualitzarRankings("UserA", crearStats(90));

        ArrayList<EntradaRanking> llista = r.getRankingMillorPuntuacio();
        assertEquals(2, llista.size());
        assertEquals("UserA", llista.get(0).getUsername());
        assertEquals("UserA", llista.get(1).getUsername());
    }


    // funció per no repetir codi als tests
    private Estadistiques crearStats(int completades) {
        Perfil p = new Perfil("N", "A", "U", "P");
        Estadistiques e = p.getEstadistiques();
        e.setHidatosCompletats(completades);
        e.setPuntuacioMitjana(completades); // Fem que coincideixin per simplicitat
        e.setMillorPuntuacio(completades); // igual, per simplicitat
        return e;
    }
}