# Hidato

Aplicació d'escriptori en **Java** per plantejar, generar, validar i resoldre **hidatos** sobre taulells de cel·les **quadrades, triangulars o hexagonals**.

Projecte de l'assignatura **PROP** (Projectes de Programació) del Grau en Enginyeria Informàtica de la FIB (UPC), quadrimestre de primavera 2025-26. Grup 41.4, equip de 5 persones. Nota: **7,7**.

## Què és un hidato
És un trencaclosques: es parteix d'un taulell amb uns quants nombres col·locats i cal omplir la resta de cel·les amb nombres consecutius $1..n$. Cada nombre $k$ ha de ser **veí** de $k+1$.

- Les cel·les poden ser triangles, quadrats o hexàgons.
- Algunes cel·les poden ser **forats** (no s'hi pot posar res).
- L'adjacència pot ser només per costats (`C`) o per costats i vèrtexs (`CA`).

## Funcionalitats
- **Resoldre** un hidato i comprovar si té **solució única**: cerca en profunditat (DFS) amb *backtracking* i poda.
- **Validar** un hidato proposat per l'usuari i **comprovar** la solució que hi ha escrit.
- **Generar** hidatos nous segons la forma, l'adjacència i el nombre de caselles. Primer es busca un camí hamiltonià aleatori i després es buiden caselles.
- Perfils d'usuari, estadístiques i **rànquings**.
- Repositori d'hidatos compartit entre usuaris.
- Modes de partida (normal, contrarellotge, supervivència) amb control del temps.
- Interfície gràfica amb **Swing** i persistència en **JSON** (Gson).

## Arquitectura
Arquitectura en tres capes. Cada capa només parla amb la següent a través d'un controlador.

```
presentacio (Swing, CardLayout)
        │
        ▼
domini ── controladors  (CtrlDomini: façana, un controlador per cas d'ús)
       ├─ model         (Taulell, Cella, Partida, Perfil, Rankings…)
       └─ algorismes    (SolucionarHidato, ValidarHidato, CrearHidato, ComprovarHidato)
        │
        ▼
persistència (CtrlPersistencia → fitxers JSON a Fonts/data/)
```

- **`Taulell`** representa la graella com a `Cella[][]` i amaga la geometria darrere de `obtenirVeines(x, y)`:
  - quadrat: 4 veïns, o 8 amb `CA`;
  - triangle: 3 veïns, segons si apunta amunt o avall;
  - hexàgon: 6 veïns, amb una graella *offset*.

  Així, els algorismes funcionen igual per a les tres formes.
- La **persistència** és *lazy*: a memòria només hi ha el perfil actiu. Els rànquings i el repositori d'hidatos es carreguen a l'inici perquè els comparteixen tots els usuaris.

Més detall a [`Documents/`](Documents/): justificació del *solver*, interfície gràfica i persistència.

## Format d'un hidato
Fitxer de text amb valors separats per comes. La primera línia indica `forma,adjacència,files,columnes`:

```
Q,CA,3,4
#,1,?,#
?,?,?,?
7,?,9,#
```

| Símbol | Significat |
|---|---|
| `Q` / `T` / `H` | quadrat / triangle / hexàgon |
| `C` / `CA` | adjacència per costats / per costats i angles |
| número | pista inicial |
| `?` | cel·la per omplir |
| `*` | forat (no accessible) |
| `#` | fora de l'àrea de l'hidato |

## Com executar-lo
Cal **JDK 11 o posterior** i `make`. Gson és a `Fonts/lib/`.

```bash
cd Fonts
make all     # compila a bin/ i genera HIDATO.jar
make run     # obre la interfície gràfica
make clean
```

A Windows sense `make`:

```powershell
cd Fonts
javac -encoding UTF-8 -cp "src;lib/gson-2.10.1.jar" -sourcepath src -d bin (Get-ChildItem -Recurse src -Filter *.java).FullName
java -cp "bin;lib/gson-2.10.1.jar" main
```

Hi ha un usuari de prova: `art` / `123`.

### Tests
- **Tests unitaris (JUnit 4):** a `Executables/<Classe>/`, cadascun amb un `descripcio.txt` que explica què prova. JUnit i Hamcrest són a `Fonts/lib/`.
- **Jocs de prova generals:** a `Executables/JocsProvaGenerals/`. Són del primer lliurament, quan el programa funcionava per consola, i ja no corresponen al `main` actual, que obre la interfície gràfica.

## Equip i repartiment
| Membre | Classes principals |
|---|---|
| **Nil Camps** | `Taulell`, `Cella`, `ComprovarHidato` |
| Carlos Martín López | `CtrlDomini`, `CtrlPresentacio`, `DirectoriHidatos`, lectura de fitxers |
| Imanol Rodriguez | `Perfil`, `Estadistiques`, `EntradaRanking` i els controladors de perfil |
| Arnau Rodriguez Trocho | `Partida`, `CtrlPartida`, `Dificultat`, modes de partida |
| Jaume Rossell | `SolucionarHidato`, `ValidarHidato`, `SolucioUnica`, `CrearHidato` |

## Limitacions conegudes
- **Contrasenyes en clar:** es desen tal qual al JSON del perfil. En un sistema real caldria guardar-ne un *hash* amb sal (bcrypt, Argon2).
- **Poda per distància:** el *solver* fa servir la distància de Manhattan com a cota. Això només és correcte amb quadrats i adjacència `C`. Amb diagonals (`CA`) o amb hexàgons pot descartar branques que tenen solució. Caldria la distància de Chebyshev, la distància hexagonal o una distància BFS sobre el graf de veïns.
- **Funcionalitats a mig fer:** "Guardar partida", les pistes durant la partida i la generació d'hidatos dins el mode supervivència.

## Documentació del codi
El codi està documentat amb Doxygen:

```bash
doxygen Doxyfile   # genera html/
```
