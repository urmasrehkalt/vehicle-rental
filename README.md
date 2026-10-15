# Sõidukite rendisüsteem

Java käsurearakendus sõidukite rentimiseks. Koolitöö (TAK25): objektorienteeritud
programmeerimine, andmetüübid, andmestruktuurid ja Git.

Rakendus hoiab sõidukite (auto, mootorratas, veoauto) ja klientide nimekirja,
laseb sõidukeid laenutada ja tagastada ning arvutab rendihinda. Sõidukeid saab
otsida, filtreerida ja sorteerida. Graafilist kasutajaliidest ja andmebaasi ei ole,
andmed elavad programmi mälus ja kaovad programmi sulgemisel. Käivitamisel laetakse
näidisandmed (6 sõidukit, 3 klienti).

## Sisukord

1. [Kiirstart](#kiirstart)
2. [JDK paigaldamine](#jdk-paigaldamine)
3. [Käivitamine](#käivitamine)
4. [Fish shell](#fish-shell)
5. [Kasutamine](#kasutamine)
6. [Reeglid](#reeglid)
7. [Projekti struktuur](#projekti-struktuur)
8. [Testimine](#testimine)
9. [Tõrkeotsing](#tõrkeotsing)
10. [Kaitsmise spikker](#kaitsmise-spikker-kus-mis-on)
11. [Git](#git)

## Kiirstart

Kui JDK 17 või uuem on juba paigaldatud (kontroll: `java -version` ja `javac -version`):

**fish:**

```fish
git clone https://github.com/urmasrehkalt/vehicle-rental.git
cd vehicle-rental
./run.fish
```

**bash / zsh:**

```bash
git clone https://github.com/urmasrehkalt/vehicle-rental.git
cd vehicle-rental
./run.sh
```

Skript kompileerib kõik failid ja käivitab programmi. Kui JDK-d veel ei ole,
alusta jaotisest [JDK paigaldamine](#jdk-paigaldamine). Fishi kohta vt ka
[Fish shell](#fish-shell).

## JDK paigaldamine

Vaja on **JDK** (Java Development Kit), mitte ainult JRE, sest programmi tuleb
kompileerida (`javac`). Nõutud versioon on 17 või uuem, arendatud ja testitud on
JDK 21-ga. Kõik allolevad käsud on testitud macOS-il.

### macOS (Homebrew)

```bash
brew install openjdk@21
```

Homebrew paigaldab selle JDK **PATH-ist välja** ("keg-only"), seega käsk `java -version`
annab pärast paigaldust ikka veateate "Unable to locate a Java Runtime". Vali üks lahendus:

**A) Lisa JDK oma terminali PATH-i** (püsivalt):

fish:

```fish
fish_add_path /opt/homebrew/opt/openjdk@21/bin
```

zsh (macOS vaikeshell):

```bash
echo 'export PATH="/opt/homebrew/opt/openjdk@21/bin:$PATH"' >> ~/.zshrc
```

Ava terminal uuesti ja kontrolli: `java -version` peab näitama `openjdk version "21..."`.
Intel Macil on asukoht `/usr/local/opt/openjdk@21/bin`.

**B) Ära muuda midagi:** `./run.fish` ja `./run.sh` leiavad Homebrew JDK ise üles, ka siis, kui see pole PATH-is.

### Windows

```powershell
winget install EclipseAdoptium.Temurin.21.JDK
```

Ava pärast paigaldust uus terminaliaken ja kontrolli `java -version`.
Alternatiiv: laadi installer alla aadressilt <https://adoptium.net>.

### Linux (Debian/Ubuntu)

```bash
sudo apt update
sudo apt install openjdk-21-jdk     # vanemal Ubuntul: openjdk-17-jdk
```

## Käivitamine

Kõik käsud tehakse **projekti juurkaustas** (seal, kus asub see README).

### 1. Skriptiga (macOS ja Linux, lihtsaim)

```fish
./run.fish      # fish
```

```bash
./run.sh        # bash / zsh
```

Mõlemad skriptid teevad sama: leiavad JDK, kompileerivad kausta `out/` ja käivitavad programmi.
Kui tuleb "Permission denied", anna skriptile käivitusõigus: `chmod +x run.fish run.sh`.

### 2. Käsitsi (kõigil süsteemidel)

macOS ja Linux:

```bash
# kompileeri kõik lähtefailid kausta out/
javac -encoding UTF-8 -d out src/rental/*.java

# käivita programm
java -cp out rental.Main
```

Windows (cmd või PowerShell):

```powershell
dir /s /b src\*.java > sources.txt
javac -encoding UTF-8 -d out @sources.txt
java -cp out rental.Main
```

Täpitähed (õ, ä, ö, ü) võivad Windowsi terminalis vigaselt kuvada. Käivita enne `chcp 65001`.

Mida käsud teevad:
- `javac` tõlgib `.java` lähtefailid `.class` baitkoodiks.
- `-d out` paneb tulemuse kausta `out/` (see on `.gitignore`-s, sest on tuletatud).
- `-encoding UTF-8` ütleb, et lähtefailides on täpitähti.
- `java -cp out rental.Main` käivitab klassi `Main` paketist `rental`; `-cp out` ütleb, kust klasse otsida.

### 3. Käivitatava JAR-failina

```bash
javac -encoding UTF-8 -d out src/rental/*.java
jar --create --file rental.jar --main-class rental.Main -C out .
java -jar rental.jar
```

JAR on üks fail, mida saab kopeerida ükskõik kuhu ja käivitada käsuga `java -jar rental.jar`.

### 4. IDE-s (IntelliJ IDEA, VS Code, Eclipse)

1. Ava kaust `vehicle-rental` projektina (IntelliJ: *File > Open*).
2. Märgi `src` lähtekoodi juurkaustaks (*Mark Directory as > Sources Root*), kui IDE ei tee seda ise.
3. Ava `src/rental/Main.java` ja vajuta rohelist käivitusnuppu (*Run*) `main` meetodi kõrval.

## Fish shell

Kõik käsud selles README-s töötavad fishis (testitud fish 4.9-ga), välja arvatud
Windowsi ja bashi/zshi näited. Fishi erinevused, mida võib vaja minna:

| Tegevus | bash / zsh | fish |
|---|---|---|
| Skripti käivitamine | `./run.sh` | `./run.fish` (`./run.sh` töötab ka, sest skript käivitub bashiga) |
| Muutuja ajutiselt PATH-i | `export PATH="/uus/tee:$PATH"` | `set -x PATH /uus/tee $PATH` |
| JDK püsivalt PATH-i | rida faili `~/.zshrc` | `fish_add_path /opt/homebrew/opt/openjdk@21/bin` |
| Käsud järjest, peatu vea korral | `javac ... && java ...` | `javac ... && java ...` (fish 3+) või `javac ...; and java ...` |

Kontroll, et JDK on fishis nähtav:

```fish
java -version
javac -version
```

Kui need annavad veateate (macOS: "Unable to locate a Java Runtime"), vt
[macOS paigaldus](#macos-homebrew). `./run.fish` töötab ka siis.

## Kasutamine

Programm näitab numbritega menüüd. Sisesta tegevuse number ja vajuta Enter.

```
=== Sõidukite rendisüsteem ===
1. Sõidukite nimekiri
2. Lisa sõiduk
3. Lisa klient
4. Klientide nimekiri
5. Laenuta sõiduk
6. Tagasta sõiduk
7. Laenutuste nimekiri
8. Otsi, filtreeri, sorteeri
9. Arvuta rendihind
0. Välju
```

Näidisandmete ID-d: sõidukid `V1` kuni `V6`, kliendid `C1` kuni `C3`.

| Klient | Juhiluba | Sobiv sõiduk |
|---|---|---|
| `C1` Mari Tamm | B | autod `V1`, `V2`, `V3` |
| `C2` Jaan Kask | A | mootorrattad `V4`, `V5` |
| `C3` Liis Mägi | C | veoauto `V6` |

### Näidissessioon

Sisestatud tekst on märgitud `>`-ga.

```
Vali tegevus: > 5
Sõiduki id: > V1
Kliendi id: > C1
Mitmeks päevaks: > 3
Laenutus tehtud. Summa kokku: 135.00 EUR

Vali tegevus: > 5
Sõiduki id: > V1
Kliendi id: > C1
Mitmeks päevaks: > 2
Viga: Sõiduk V1 on juba laenutatud.

Vali tegevus: > 5
Sõiduki id: > V6
Kliendi id: > C1
Mitmeks päevaks: > 1
Viga: Mari Tamm (kategooria B) ei tohi laenutada sõidukit V6, vaja on kategooriat C.

Vali tegevus: > 9
Sõiduki id: > V4
Päevade arv: > 7
Mootorratas Honda CB650R, 7 päeva: 238.00 EUR

Vali tegevus: > 0
Head aega!
```

Andmed elavad ainult mälus: programmi sulgemisel taastub järgmisel käivitusel algne
näidisandmestik.

## Reeglid

### Hinnareeglid

| Tüüp | Hinna arvutus |
|---|---|
| Auto | päevahind × päevad, luksusauto +30% |
| Mootorratas | päevahind × päevad, alates 7 päevast -15% |
| Veoauto | (päevahind + 5 EUR iga kandevõime tonni kohta) × päevad |

### Juhiloa reegel

Sõidukit saab laenutada ainult klient, kelle juhiloa kategooria täpselt klapib:
auto vajab kategooriat **B**, mootorratas **A**, veoauto **C**. Kategooriat küsib
iga alamklass ise (`getRequiredLicense()`), kontrolli teeb `Customer.hasLicenseFor(...)`
ja seda kasutab `RentalService.rentVehicle`.

### Muud reeglid

- Sama sõidukit ei saa kaks korda laenutada enne tagastamist.
- Sõiduki id ja registreerimismärk peavad olema unikaalsed (märgi suur- ja väiketähti ei eristata).
- Päevade arv peab olema vähemalt 1.

## Projekti struktuur

```
vehicle-rental/
  run.sh                kompileerib ja käivitab (bash / zsh, macOS ja Linux)
  run.fish              sama fish shellile
  README.md             see dokument
  .gitignore            jätab välja out/, *.class, *.jar
  src/rental/
    Vehicle.java          abstraktne ülemklass kõigile sõidukitele
    Car.java              alamklass: auto
    Motorcycle.java       alamklass: mootorratas
    Truck.java            alamklass: veoauto
    Rentable.java         liides: laenutatav asi
    Customer.java         klient (kapseldatud ja valideeritud väljad)
    Rental.java           üks laenutus (sõiduk, klient, päevad, summa)
    RentalService.java    põhiloogika: laenutus, tagastus, otsing, sorteerimine
    RentalException.java  oma erind äriloogika vigade jaoks
    ConsoleMenu.java      tekstimenüü (ainult kasutajaga suhtlemine)
    ConsoleInput.java     sisendi lugemine ja kontroll
    SampleData.java       näidisandmed
    Main.java             programmi sisendpunkt
```

Kasutajaliides (`ConsoleMenu`) ja loogika (`RentalService`) on teadlikult lahus:
teenus ei tea midagi `System.out`-ist ega `Scanner`-ist.

## Testimine

Automaattestide raamistikku (JUnit) ei kasutata. Testitakse käsitsi allolevate
stsenaariumidega.

### Kiire suitsutest

Kontrollib, et programm käivitub ja sulgub (macOS ja Linux). Peab lõppema sõnadega "Head aega!":

```bash
printf '1\n0\n' | java -cp out rental.Main | tail -1
```

### Käsitsi stsenaariumid

Iga stsenaarium: käivita programm ja tee sammud. Oodatud tulemus on sulgudes.

1. **Laenutus:** valik 5, `V1`, `C1`, `3` (summa 135.00 EUR). Valik 1 näitab V1 olekut "laenutatud".
2. **Topeltlaenutus:** korda sammu 1 (viga: sõiduk V1 on juba laenutatud).
3. **Tagastus:** valik 6, `V1` (tagastatud). Korda (viga: sõiduk ei ole laenutatud).
4. **Tundmatu id:** valik 5, `X`, `C1`, `2` (viga: sõidukit ei leitud).
5. **Vigane päevade arv:** valik 5, `V1`, `C1`, `0` (viga, sõiduk jääb saadavaks). Sisesta `abc` (küsitakse uuesti).
6. **Polümorfne hind:** valik 9 koos `V2`/3 (351.00 EUR, luksusauto), `V4`/7 (238.00 EUR, nädalasoodustus), `V6`/2 (235.00 EUR, veoauto).
7. **Otsing:** valik 8, 1, `toy` (leiab Toyota). Tekst `zzz` (Midagi ei leitud).
8. **Filter ja sort:** valik 8, 4, `50,5` (komaga sisend töötab), sorteeri hinna järgi.
9. **Duplikaat:** lisa sõiduk id-ga `V1` või märgiga `123ABC` (viga: juba olemas).
10. **Vale kategooria:** lisa klient kategooriaga `Z` (viga: peab olema A, B või C).
11. **Vale juhiluba:** valik 5, `V6` (veoauto), `C1` (kategooria B), `1` (viga: vaja on kategooriat C, sõiduk jääb saadavaks). Sobiv paar: `V6` ja `C3`.
12. **Sisendi lõpp:** sulge sisend Ctrl+D-ga (programm sulgub rahulikult, ei kuku kokku).

## Tõrkeotsing

| Probleem | Põhjus ja lahendus |
|---|---|
| `Unable to locate a Java Runtime` (macOS) | JDK puudub või pole PATH-is. Vt [macOS paigaldus](#macos-homebrew). `./run.sh` leiab Homebrew JDK ise. |
| `javac: command not found` | Paigaldatud on ainult JRE või PATH on vale. Vaja on JDK. |
| `Error: Could not find or load main class rental.Main` | Käsk on tehtud vales kaustas või pole kompileeritud. Mine projekti juurkausta ja kompileeri uuesti (`javac ...`). `-cp out` ja `rental.Main` peavad täpselt olema nii. |
| `UnsupportedClassVersionError` | Kompileerisid uuema JDK-ga kui käivitad. Kasuta mõlemaks sama JDK-d, kontrolli `java -version` ja `javac -version`. |
| `error: file not found: src/rental/*.java` | Oled vales kaustas. `ls src/rental` peab näitama `.java` faile. |
| Täpitähed näevad välja `Ã¶`, `?` | Terminali kodeering pole UTF-8. Windowsis `chcp 65001`, muus terminalis kontrolli UTF-8 seadet. Kompileerimisel kasuta alati `-encoding UTF-8`. |
| `Permission denied: ./run.sh` või `./run.fish` | `chmod +x run.sh run.fish` |
| fish: `Unknown command: java` | JDK pole fishi PATH-is. `fish_add_path /opt/homebrew/opt/openjdk@21/bin` või kasuta `./run.fish`. |
| Programm kordab "Palun sisesta täisarv" | Menüüs ootab programm numbrit. Sisesta number, mitte tekst. |
| Muudatused koodis ei avaldu | Kompileeri uuesti. `./run.sh` teeb seda iga kord. |

## Kaitsmise spikker: kus mis on

**Pärilus:** `Car`, `Motorcycle` ja `Truck` pärivad klassist `Vehicle` (`extends`).
Alamklass saab ülemklassilt id, margi, hinna jne ning lisab omad väljad
(`seats`, `engineCc`, `loadCapacityKg`). Konstruktoris kutsub `super(...)` ülemklassi konstruktorit.

**Polümorfism:** meetod `calculateRentalCost(days)` on `Vehicle`-is abstraktne ja iga
alamklass kirjutab selle üle (`@Override`). `Rental` kutsub `vehicle.calculateRentalCost(days)`
ja ei tea, kas sõiduk on auto, mootorratas või veoauto: õige versioon valitakse töö ajal
objekti tegeliku tüübi järgi. Samamoodi töötab `getRequiredLicense()`. Vaata ka menüü valikut 9.

**Abstraktsioon:** `Vehicle` on abstraktne klass (objekti ei saa `new Vehicle(...)`-ga luua)
ja `Rentable` on liides (ainult meetodite lepe, ilma koodita).
- Abstraktne klass: ühine olek (väljad) ja osa valmis koodi, nt `rent()`. Üks ülemklass.
- Liides: ainult leping "mida peab oskama". Klass võib realiseerida mitut liidest.

**Kapseldamine:** kõik väljad on `private`, ligipääs getterite ja setterite kaudu.
Setterid kontrollivad väärtust (hind > 0, aasta 1950 kuni järgmine aasta, tekst pole tühi,
juhiloa kategooria A/B/C). Kontroll on ühes kohas, nii et vigast objekti ei saa tekitada.
`id` on `final`, sest see ei tohi muutuda. `RentalService` tagastab nimekirjadest
koopiad, et väljastpoolt ei saaks sisemist kollektsiooni muuta.

**Kollektsioonid ja miks:**

| Kollektsioon | Kus | Miks |
|---|---|---|
| `HashMap<String, Vehicle>` | `RentalService` | Otsing id järgi on kiire, ei pea kogu nimekirja läbi käima |
| `HashMap<String, Customer>` | `RentalService` | Sama põhjus |
| `HashSet<String>` | `RentalService` | Registreerimismärgid peavad olema unikaalsed, hulk ei luba duplikaate |
| `ArrayList<Rental>` | `RentalService` | Laenutuste järjekord on oluline, läbi käiakse järjest |

**Andmetüübid:** `int` (päevad, aasta, kohad), `double` (hinnad, kandevõime), `boolean`
(saadaval, luksus, tagastatud), `String` (nimed, id-d), `LocalDate` (laenutuse algus).
Hinna jaoks oleks päris süsteemis parem `BigDecimal`, sest `double` on ümardamisel ebatäpne.
Siin kasutasin `double`-it lihtsuse pärast.

**Veakäsitlus:** kaks erindi liiki.
- `IllegalArgumentException`: vigane väärtus (negatiivne hind, tühi nimi).
- `RentalException` (oma, kontrollitud): reeglirikkumine (sõiduk juba laenutatud, id-d ei leitud, vale juhiluba).

Menüü püüab mõlemad kinni ja näitab teksti, programm jätkab. Numbrisisend küsitakse
uuesti, kuni see on korrektne.

**Otsing, filter, sort:** `searchByText`, `filterByType`, `filterAvailable`,
`filterByMaxDailyRate`, `sortByDailyRate`, `sortByYearNewestFirst` (`Comparator`).
Sorteerimine teeb koopia, algne nimekiri jääb samaks.

## Git

Iga funktsionaalsus ja parandus tehti eraldi GitHubi issue'na ja eraldi branchil, kuhu
tehti commit sõnumiga `#<issue>: <kirjeldus>`. Valmis töö liideti `main`-i squash-merge'iga,
seega `main`-is on iga issue kohta üks commit. Ajalugu: `git log --oneline`.
