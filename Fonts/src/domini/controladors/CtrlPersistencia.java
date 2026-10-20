/**
 * @file CtrlPersistencia.java
 * @brief Controlador encarregat de la gestió de persistència de dades del sistema en format JSON.
 * @author Imanol
 */


package domini.controladors;

import java.time.Instant;
import java.util.ArrayList;

import com.google.gson.*;

import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;

import domini.model.*;


/**
 * @class CtrlPersistencia
 * @brief Aquesta classe actua com a capa de persistència per a tota l'aplicació.
 * * S'encarrega d'escriure i llegir objectes Java al disc utilitzant la llibreria GSON.
 * Gestiona l'emmagatzematge de Perfils, Rànquings, Partides (amb polimorfisme) i Taulells,
 * organitzant-los en una estructura de directoris dins del directori /data/.
 */
public class CtrlPersistencia {
    /** @brief Ruta arrel on es desen totes les dades de l'aplicació (/data). */
    private String rutaData;

    /**
     * @brief Constructor del controlador de persistència.
     * * Defineix la ruta arrel i comprova si el directori principal existeix; si no, el crea.
     */
    public CtrlPersistencia() {

        //System.out.println("Start Controlador Persistencia");

        this.rutaData = "data/";
        File directorio = new File(rutaData);
        if (!directorio.exists()) {
            directorio.mkdirs();
            // System.out.println("/data/ creada");
        } else {
            System.out.println("La carpeta /data/ estaba ya creada");
        }

    }

    /**
     * @brief Desa l'estat d'un perfil d'usuari (i les seves estadístiques) al disc.
     * @param p Objecte Perfil a emmagatzemar.
     */
    public void storePerfil(Perfil p) {
        if (p == null) {
            System.out.println("Error: S'ha intentat guardar un perfil nul.");
            return;
        }

        String rutaDirectoriUsuari = this.rutaData + "users/" + p.getUsername() + "/";
        String rutaArxiu = rutaDirectoriUsuari + "perfil.json";

        File directorio = new File(rutaDirectoriUsuari);
        if (!directorio.exists()) {
            directorio.mkdirs();
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter writer = new FileWriter(rutaArxiu)) {
            gson.toJson(p, writer);
            //System.out.println("Perfil de '" + p.getUsername() + "' guardat correctament a " + rutaArxiu);
        } catch (IOException e) {
            System.out.println("Error a l'intentar guardar el perfil de " + p.getUsername());
            System.out.println(e.getMessage());
        }
    }

    /**
     * @brief Carrega un perfil d'usuari des del disc i el retorna.
     * * A més de llegir el fitxer JSON, reconstrueix manualment la dependència bidireccional
     * entre el Perfil i les seves Estadistiques per compensar l'atribut transient.
     * @param username Identificador de l'usuari que es vol carregar.
     * @return Retorna l'objecte Perfil carregat o null si l'arxiu no existeix o hi ha hagut un error.
     */
    public Perfil loadPerfil(String username) {

        String ruta = this.rutaData + "users/" + username + "/perfil.json";
        File jsonfile = new File(ruta);


        if (!jsonfile.exists()) {
            System.out.println("Avís: No s'ha trobat cap perfil amb username: '" + username + "'.");
            return null;
        }

        Gson gson = new Gson();

        try (FileReader reader = new FileReader(jsonfile)) {

            Perfil perfilCarregat = gson.fromJson(reader, Perfil.class);

            // como he puesto transient ahora tengo que hacer esto de manera manual
            if (perfilCarregat != null && perfilCarregat.getEstadistiques() != null) {
                perfilCarregat.getEstadistiques().setPerfil(perfilCarregat);
            }

            //System.out.println("Perfil de '" + username + "' carregat i vinculat amb èxit des de disc!");
            return perfilCarregat;

        } catch (IOException e) {
            System.out.println("Error al llegir l'arxiu de perfil de: " + username);
            System.out.println(e.getMessage());
            return null;
        }
    }


    /**
     * @brief Classe interna que fa de wrapper per agrupar les tres llistes de rànquings.
     * * Això permet guardar tot el Singleton de Rankings en un sol fitxer JSON.
     */
    class WrapperRankings {
        public ArrayList<EntradaRanking> rPuntuacioMitjana;
        public ArrayList<EntradaRanking> rPartidesCompletades;
        public ArrayList<EntradaRanking> rMillorPuntuacio;
    }

    /**
     * @brief Extreu les dades del Singleton Rankings i les guarda al disc.
     */
    public void guardarRankings() {
        String rutaDirectoriRankings = this.rutaData + "rankings/";
        String rutaArxiu = rutaDirectoriRankings + "rankings.json";

        File directori = new File(rutaDirectoriRankings);
        if (!directori.exists()) {
            directori.mkdirs();
        }

        Rankings instRankings = Rankings.getInstanciaRankings();

        WrapperRankings dades = new WrapperRankings();
        dades.rPuntuacioMitjana = instRankings.getRPuntuacioMitjana();
        dades.rPartidesCompletades = instRankings.getRPartidesCompletades();
        dades.rMillorPuntuacio = instRankings.getRMillorPuntuacio();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(rutaArxiu)) {
            gson.toJson(dades, writer);
            // System.out.println("Ranquings guardats correctament a " + rutaArxiu);
        } catch (IOException e) {
            System.out.println("Error al guardar els rànquings: " + e.getMessage());
        }
    }

    /**
     * @brief Llegeix el fitxer global de rànquings del disc i bolca les dades al Singleton.
     * * Si el fitxer no existeix, inicialitza el Singleton buit i el guarda.
     */
    public void cargarRankings() {
        String rutaDirectoriRankings = this.rutaData + "rankings/";
        String rutaArxiu = rutaDirectoriRankings + "rankings.json";

        File directori = new File(rutaDirectoriRankings);
        if (!directori.exists()) {
            directori.mkdirs();
        }

        File jsonfile = new File(rutaArxiu);
        if (!jsonfile.exists()) {
            Rankings.getInstanciaRankings();
            guardarRankings();
            return;
        }

        Gson gson = new Gson();
        try (FileReader reader = new FileReader(jsonfile)) {


            WrapperRankings dadesCarregades = gson.fromJson(reader, WrapperRankings.class);

            if (dadesCarregades != null) {

                domini.model.Rankings inst = domini.model.Rankings.getInstanciaRankings();

                if (dadesCarregades.rPuntuacioMitjana != null)
                    inst.setRPuntuacioMitjana(dadesCarregades.rPuntuacioMitjana);

                if (dadesCarregades.rPartidesCompletades != null)
                    inst.setRPartidesCompletades(dadesCarregades.rPartidesCompletades);

                if (dadesCarregades.rMillorPuntuacio != null)
                    inst.setRMillorPuntuacio(dadesCarregades.rMillorPuntuacio);

            }

        } catch (IOException e) {
            System.out.println("Error al llegir l'arxiu de rànquings: " + e.getMessage());
        }
    }

    // PARTIDA

    /**
     * @brief Adaptador intern per permetre a GSON serialitzar i deserialitzar objectes Instant (temps inici de partida).
     */
    private static class InstantAdapter implements JsonSerializer<java.time.Instant>, JsonDeserializer<java.time.Instant> {
        public JsonElement serialize(Instant src, java.lang.reflect.Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.toString());
        }
        public Instant deserialize(JsonElement json, java.lang.reflect.Type typeOfT, JsonDeserializationContext context) {
            return Instant.parse(json.getAsString());
        }
    }

    /**
     * @brief Adaptador intern per permetre a GSON serialitzar i deserialitzar objectes Duration (temps transcorregut).
     */
    private static class DurationAdapter implements JsonSerializer<java.time.Duration>, JsonDeserializer<java.time.Duration> {
        public JsonElement serialize(java.time.Duration src, java.lang.reflect.Type typeOfSrc, JsonSerializationContext context) {
            return new com.google.gson.JsonPrimitive(src.toString());
        }
        public java.time.Duration deserialize(JsonElement json, java.lang.reflect.Type typeOfT, JsonDeserializationContext context) {
            return java.time.Duration.parse(json.getAsString());
        }
    }

    /**
     * @brief Classe interna que fa de wrapper per resoldre el problema del polimorfisme a GSON.
     * * Guarda un String amb el tipus de partida per saber quina subclasse de Partida instanciar al carregar.
     */
    private class WrapperPartida {
        String tipo; // "NORMAL", "CONTRARRELLOTGE" o "SUPERVIVENCIA"
        Partida partida;
    }


    /**
     * @brief Desa l'estat d'una partida a mitges generant un nom automàtic (partida1, partida2...).
     * @param username Identificador de l'usuari propietari de la partida.
     * @param nomPartida Pot ser el nom d'una partida específica o null/buit si encara no es sap.
     * @param p L'objecte Partida (pot ser Normal, Contrarrellotge o Supervivencia).
     */
    public void storePartida(String username, String nomPartida, Partida p) {
        String rutaDirectori = this.rutaData + "users/" + username + "/partidas/";
        File directorio = new File(rutaDirectori);
        if (!directorio.exists()) directorio.mkdirs();

        String nomFinal = nomPartida;

        // Si ens passen null o un text buit, autogenerem el nom buscant el màxim
        if (nomFinal == null || nomFinal.trim().isEmpty()) {
            int maxId = 0;
            File[] arxius = directorio.listFiles();
            if (arxius != null) {
                for (File f : arxius) {
                    if (f.isFile() && f.getName().startsWith("partida") && f.getName().endsWith(".json")) {
                        try {
                            String numStr = f.getName().replace("partida", "").replace(".json", "");
                            int num = Integer.parseInt(numStr);
                            if (num > maxId) maxId = num;
                        } catch (NumberFormatException ignored) { }
                    }
                }
            }
            nomFinal = "partida" + (maxId + 1);
        }

        String rutaArxiu = rutaDirectori + nomFinal + ".json";

        WrapperPartida wrapper = new WrapperPartida();
        wrapper.partida = p;

        // Polimorfisme
        if (p.getClass().getSimpleName().equals("Normal")) wrapper.tipo = "NORMAL";
        else if (p.getClass().getSimpleName().equals("Contrarrellotge")) wrapper.tipo = "CONTRARRELLOTGE";
        else wrapper.tipo = "SUPERVIVENCIA";

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(java.time.Instant.class, new InstantAdapter())
                .registerTypeAdapter(java.time.Duration.class, new DurationAdapter())
                .setPrettyPrinting()
                .create();

        try (FileWriter writer = new FileWriter(rutaArxiu)) {
            gson.toJson(wrapper, writer);
            System.out.println("Partida guardada automàticament com '" + nomFinal + "'");
        } catch (IOException e) {
            System.out.println("Error al guardar la partida '" + nomFinal + "': " + e.getMessage());
        }
    }



    /**
     * @brief Carrega una partida en curs guardada al disc, resolent el polimorfisme automàticament.
     * @param username Identificador de l'usuari propietari.
     * @param nomPartida Nom de l'arxiu a carregar.
     * @return Retorna l'objecte Partida (com la subclasse concreta adequada) o null si falla.
     */
    public Partida loadPartida(String username, String nomPartida) {
        String ruta = this.rutaData + "users/" + username + "/partidas/" + nomPartida + ".json";
        File file = new File(ruta);

        if (!file.exists()) {
            System.out.println("No s'ha trobat la partida '" + nomPartida + "'.");
            return null;
        }

        try (FileReader reader = new FileReader(file)) {
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            String tipo = jsonObject.get("tipo").getAsString();
            JsonElement partidaJson = jsonObject.get("partida");

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(java.time.Instant.class, new InstantAdapter())
                    .registerTypeAdapter(java.time.Duration.class, new DurationAdapter())
                    .create();

            System.out.println("Partida '" + nomPartida + "' carregada correctament.");

            if (tipo.equals("NORMAL"))
                return gson.fromJson(partidaJson, Normal.class);
            if (tipo.equals("CONTRARRELLOTGE"))
                return gson.fromJson(partidaJson, Contrarrellotge.class);

            return gson.fromJson(partidaJson, Supervivencia.class);

        } catch (Exception e) {
            System.out.println("Error al carregar la partida: " + e.getMessage());
            return null;
        }
    }

    /**
     * @brief Retorna una llista amb els noms de totes les partides guardades d'un usuari.
     * @param username Identificador de l'usuari.
     * @return ArrayList amb els noms, sense extensió .json, de les partides.
     */
    public ArrayList<String> getPartidesUsuari(String username) {
        ArrayList<String> llistaPartides = new ArrayList<>();
        String rutaDirectori = this.rutaData + "users/" + username + "/partidas/";

        File carpeta = new File(rutaDirectori);

        // Si la carpeta existeix i és un directori, llegim el que hi ha a dins
        if (carpeta.exists() && carpeta.isDirectory()) {
            File[] arxius = carpeta.listFiles();
            if (arxius != null) {
                for (File arxiu : arxius) {
                    if (arxiu.isFile() && arxiu.getName().endsWith(".json")) {

                        String nomNet = arxiu.getName().replace(".json", "");
                        llistaPartides.add(nomNet);
                    }
                }
            }
        }
        return llistaPartides;
    }

    // GESTIÓ DE REPOSITORI HIDATOS

    /**
     * @brief Desa un taulell buit per jugar a la carpeta d'originals del directori.
     * @param t Objecte Taulell a guardar.
     */
    public void storeTaulell(Taulell t) {
        if (t == null) return;
        String rutaDirectori = this.rutaData + "repositoris/originals/";
        // Usem el codi del taulell com a nom de l'arxiu
        String rutaArxiu = rutaDirectori + t.getCodi() + ".json";

        File folder = new File(rutaDirectori);
        if (!folder.exists()) folder.mkdirs();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(rutaArxiu)) {
            gson.toJson(t, writer);
            System.out.println("Taulell " + t.getCodi() + " guardat correctament.");
        } catch (IOException e) {
            System.err.println("Error al guardar taulell original: " + e.getMessage());
        }
    }

    /**
     * @brief Desa un taulell resolt a la carpeta de solucions del directori.
     * @param tsol Objecte Taulell amb la solució a guardar.
     */
    public void storeTaulellSolucio(Taulell tsol) {
        if (tsol == null) return;
        String rutaDirectori = this.rutaData + "repositoris/solucions/";
        String rutaArxiu = rutaDirectori + tsol.getCodi() + ".json";

        File folder = new File(rutaDirectori);
        if (!folder.exists()) folder.mkdirs();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(rutaArxiu)) {
            gson.toJson(tsol, writer);
            System.out.println("Solució del taulell " + tsol.getCodi() + " guardada.");
        } catch (IOException e) {
            System.err.println("Error al guardar solució: " + e.getMessage());
        }
    }


    /**
     * @brief Carrega un taulell original per ser jugat a partir del seu codi.
     * @param codi Identificador numèric de l'hidato.
     * @return Retorna l'objecte Taulell o null si no existeix.
     */
    public Taulell loadTaulell(int codi) {
        String rutaArxiu = this.rutaData + "repositoris/originals/" + codi + ".json";
        File file = new File(rutaArxiu);
        if (!file.exists()) return null;

        Gson gson = new Gson();
        try (FileReader reader = new FileReader(file)) {
            return gson.fromJson(reader, Taulell.class);
        } catch (IOException e) {
            System.err.println("Error al carregar taulell " + codi + ": " + e.getMessage());
            return null;
        }
    }

    /**
     * @brief Carrega la solució d'un taulell a partir del seu codi.
     * @param codi Identificador numèric de l'hidato resolt.
     * @return Retorna l'objecte Taulell resolt o null si no existeix.
     */
    public Taulell loadTaulellSolucio(int codi) {
        String rutaArxiu = this.rutaData + "repositoris/solucions/" + codi + ".json";
        File file = new File(rutaArxiu);
        if (!file.exists()) return null;

        Gson gson = new Gson();
        try (FileReader reader = new FileReader(file)) {
            return gson.fromJson(reader, Taulell.class);
        } catch (IOException e) {
            System.err.println("Error al carregar solució " + codi + ": " + e.getMessage());
            return null;
        }
    }

    // CARREGAR TOTS ELS TAULELLS

    /**
     * @brief Llegeix tots els arxius JSON de la carpeta d'originals i els carrega.
     * @return ArrayList amb tots els taulells originals emmagatzemats al sistema.
     */
    public ArrayList<Taulell> loadTaulellsOriginals() {
        ArrayList<Taulell> llista = new ArrayList<>();
        File folder = new File(this.rutaData + "repositoris/originals/");

        // Comprovem que la carpeta existeix i té fitxers
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles((dir, name) -> name.endsWith(".json"));

            if (files != null) {
                Gson gson = new Gson();
                for (File f : files) {
                    try (FileReader reader = new FileReader(f)) {
                        Taulell t = gson.fromJson(reader, Taulell.class);
                        if (t != null) llista.add(t);
                    } catch (IOException e) {
                        System.err.println("Error al carregar l'arxiu: " + f.getName());
                    }
                }
            }
        }
        // System.out.println("S'han carregat " + llista.size() + " taulells  des de disc.");
        return llista;
    }

    /**
     * @brief Llegeix tots els arxius JSON de la carpeta de solucions i els carrega.
     * @return ArrayList amb tots els taulells solució emmagatzemats al sistema.
     */
    public ArrayList<Taulell> loadTaulellsSolucions() {
        ArrayList<Taulell> llista = new ArrayList<>();
        File folder = new File(this.rutaData + "repositoris/solucions/");

        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles((dir, name) -> name.endsWith(".json"));

            if (files != null) {
                Gson gson = new Gson();
                for (File f : files) {
                    try (FileReader reader = new FileReader(f)) {
                        Taulell t = gson.fromJson(reader, Taulell.class);
                        if (t != null) llista.add(t);
                    } catch (IOException e) {
                        System.err.println("Error al carregar l'arxiu: " + f.getName());
                    }
                }
            }
        }
        // System.out.println("S'han carregat " + llista.size() + " solucions des de disc.");
        return llista;
    }

    /**
     * @brief Mètode auxiliar per comprovar si un usuari ja existeix.
     * * Simplement comprova si existeix la carpeta de l'usuari en comptes de llegir el JSON sencer.
     * @param username Nom d'usuari a comprovar.
     * @return Retorna true si l'usuari ja té un directori creat, false en cas contrari.
     */
    public boolean existeixPerfil(String username) {
        File carpetaUsuari = new File(this.rutaData + "users/" + username);
        return carpetaUsuari.exists();
    }


}
