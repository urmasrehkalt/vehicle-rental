# Sõidukite rendisüsteem

Java käsurearakendus sõidukite rentimiseks. Koolitöö (TAK25): objektorienteeritud
programmeerimine, andmetüübid, andmestruktuurid ja Git.

Rakendus hoiab sõidukite (auto, mootorratas, veoauto) ja klientide nimekirja,
laseb sõidukeid laenutada ja tagastada ning arvutab rendihinda. Sõidukeid saab
otsida, filtreerida ja sorteerida. Graafilist kasutajaliidest ja andmebaasi ei ole,
andmed elavad programmi mälus ja kaovad programmi sulgemisel. Käivitamisel laetakse
näidisandmed (6 sõidukit, 3 klienti).

## Nõuded

- JDK 17 või uuem (arendatud JDK 21-ga)

Versiooni saab kontrollida käsuga `java -version`.

## Kompileerimine ja käivitamine

Projekti juurkaustast:

```bash
# kompileeri kõik lähtefailid kausta out/
javac -encoding UTF-8 -d out src/rental/*.java

# käivita programm
java -cp out rental.Main
```

## Kasutamine

Programm näitab numbritega menüüd. Sisesta tegevuse number ja vajuta Enter.

```
1. Sõidukite nimekiri          6. Tagasta sõiduk
2. Lisa sõiduk                 7. Laenutuste nimekiri
3. Lisa klient                 8. Otsi, filtreeri, sorteeri
4. Klientide nimekiri          9. Arvuta rendihind
5. Laenuta sõiduk              0. Välju
```

Näidisandmete ID-d: sõidukid `V1` kuni `V6`, kliendid `C1` kuni `C3`.

## Hinnareeglid

| Tüüp | Hinna arvutus |
|---|---|
| Auto | päevahind × päevad, luksusauto +30% |
| Mootorratas | päevahind × päevad, alates 7 päevast -15% |
| Veoauto | (päevahind + 5 EUR iga kandevõime tonni kohta) × päevad |

## Projekti struktuur

```
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

## Käsitsi testistsenaariumid

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
11. **Sisendi lõpp:** sulge sisend Ctrl+D-ga (programm sulgub rahulikult, ei kuku kokku).

## Kaitsmise spikker: kus mis on

**Pärilus:** `Car`, `Motorcycle` ja `Truck` pärivad klassist `Vehicle` (`extends`).
Alamklass saab ülemklassilt id, margi, hinna jne ning lisab omad väljad
(`seats`, `engineCc`, `loadCapacityKg`). Konstruktoris kutsub `super(...)` ülemklassi konstruktorit.

**Polümorfism:** meetod `calculateRentalCost(days)` on `Vehicle`-is abstraktne ja iga
alamklass kirjutab selle üle (`@Override`). `Rental` kutsub `vehicle.calculateRentalCost(days)`
ja ei tea, kas sõiduk on auto, mootorratas või veoauto: õige versioon valitakse töö ajal
objekti tegeliku tüübi järgi. Vaata ka menüü valikut 9.

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
- `RentalException` (oma, kontrollitud): reeglirikkumine (sõiduk juba laenutatud, id-d ei leitud).

Menüü püüab mõlemad kinni ja näitab teksti, programm jätkab. Numbrisisend küsitakse
uuesti, kuni see on korrektne.

**Otsing, filter, sort:** `searchByText`, `filterByType`, `filterAvailable`,
`filterByMaxDailyRate`, `sortByDailyRate`, `sortByYearNewestFirst` (`Comparator`).
Sorteerimine teeb koopia, algne nimekiri jääb samaks.

## Git

Iga funktsionaalsus tehti eraldi GitHubi issue'na ja eraldi branchil, kuhu tehti commit
sõnumiga `#<issue>: <kirjeldus>`. Valmis töö liideti `main`-i squash-merge'iga.
Ajalugu: `git log --oneline`.
