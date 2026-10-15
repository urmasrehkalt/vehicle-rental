# Koodi selgitus algajale

Lühike ülevaade põhimõistetest ja sellest, kuidas see rakendus töötab. Kõik näited on
selle projekti päris koodist. Põhimõtete (pärilus, polümorfism jne) kaitsmise spikker
on failis [README.md](README.md#kaitsmise-spikker-kus-mis-on).

## 1. Põhimõisted

| Mõiste | Mis see on | Näide selles projektis |
|---|---|---|
| **Klass** | Plaan või mall: kirjeldab, mis andmeid objekt hoiab ja mida ta oskab | `Vehicle`, `Customer`, `Rental` |
| **Objekt** | Klassi põhjal loodud konkreetne asi mälus | `new Car("V1", "123ABC", "Toyota", "Corolla", 2020, 45.0, 5, false)` on üks auto |
| **Muutuja** | Nimega koht mälus, kus hoitakse väärtust. Tal on tüüp | `int days`, `String name` |
| **Väli** | Muutuja, mis kuulub objektile ja elab nii kaua kui objekt | `private double dailyRate;` klassis `Vehicle` |
| **Lokaalne muutuja** | Muutuja meetodi sees, kaob meetodi lõppedes | `Vehicle vehicle = getVehicle(vehicleId);` klassis `RentalService` |
| **Parameeter** | Meetodi sulgudes olev muutuja, mille väärtuse annab kutsuja | `vehicleId` meetodis `rentVehicle(String vehicleId, ...)` |
| **Meetod** | Nimega koodiplokk, mis teeb midagi | `calculateRentalCost(int days)` |
| **Konstruktor** | Eriline meetod, mis käivitub objekti loomisel (`new`) ja seab algväärtused. Nimi on sama mis klassil, tagastustüüpi pole | `public Car(String id, ...)` |
| **`new`** | Loob objekti ja kutsub konstruktorit | `new Rental(vehicle, customer, days)` |
| **`this`** | Viide objektile endale. Eristab välja parameetrist, kui nimed on samad | `this.luxury = luxury;` (vasakul väli, paremal parameeter) |
| **`private` / `public`** | Kes tohib näha: `private` ainult sama klass, `public` kõik | väljad `private`, meetodid `public` |
| **Getter / setter** | Meetod välja lugemiseks / muutmiseks. Setter saab väärtust kontrollida | `getDailyRate()`, `setDailyRate(...)` |
| **`final`** | Väärtust ei saa pärast seadmist muuta | `private final String id;` |
| **`static`** | Kuulub klassile, mitte konkreetsele objektile | `private static final int MIN_YEAR = 1950;` |
| **`extends`** | Pärilus: alamklass saab ülemklassi väljad ja meetodid | `class Car extends Vehicle` |
| **`implements`** | Klass lubab täita liidese lepet | `class Vehicle implements Rentable` |
| **`@Override`** | Alamklass kirjutab ülemklassi meetodi üle. Kompilaator kontrollib, et selline meetod tõesti on | `calculateRentalCost` klassis `Car` |
| **Erind (exception)** | Veasituatsiooni teade. `throw` viskab, `try/catch` püüab | `throw new RentalException("...")` |
| **Kollektsioon** | Objekt, mis hoiab palju teisi objekte | `HashMap`, `HashSet`, `ArrayList` klassis `RentalService` |

### Primitiiv- ja viitetüübid

- **Primitiiv** hoiab väärtust ise: `int`, `double`, `boolean`. Ei saa olla `null`.
- **Viitetüüp** hoiab *viidet* objektile: `String`, `Vehicle`, `List<Rental>`. Võib olla `null`
  ("viit ei näita kuhugi").
- `Integer` on `int` objektina (saab olla `null`), `String` on viitetüüp muutumatu tekstiga.
- `==` võrdleb primitiive väärtuse järgi, aga objekte viite järgi (kas sama objekt).
  `.equals()` võrdleb sisu. Seepärast kirjutab `Customer.hasLicenseFor` `licenseCategory.equals(...)`,
  mitte `==`.

## 2. Klassid lühidalt

| Klass | Mida teeb |
|---|---|
| `Vehicle` | Abstraktne ülemklass: ühised väljad (id, mark, hind, saadaval), kontrollid, `rent()` ja `returnVehicle()` |
| `Car`, `Motorcycle`, `Truck` | Alamklassid: oma lisaväljad, oma hinnaarvutus ja nõutud juhiloa kategooria |
| `Rentable` | Liides: lepe, et laenutatav asi oskab `rent()`, `returnVehicle()`, `isAvailable()` |
| `Customer` | Klient: nimi, juhiloa kategooria, kontrollitud väljad |
| `Rental` | Üks laenutus: sõiduk, klient, päevad, summa, kuupäev, tagastatud või mitte |
| `RentalService` | Kogu loogika: hoiab kollektsioone, laenutab, tagastab, otsib, sorteerib |
| `RentalException` | Oma veatüüp reeglirikkumiste jaoks |
| `ConsoleMenu` | Menüü: küsib kasutajalt, kutsub teenust, prindib tulemuse |
| `ConsoleInput` | Sisendi lugemine: küsib uuesti, kuni sisend sobib |
| `SampleData` | Näidisandmed käivitamisel |
| `Main` | Sisendpunkt: `main` meetodist algab programm |

## 3. Mis juhtub ühe laenutuse ajal

Kasutaja valib menüüs 5 ja sisestab `V1`, `C1`, `3`.

1. `Main.main` käivitub, loob `RentalService`, laeb `SampleData` ja käivitab `ConsoleMenu.run()`.
2. `ConsoleMenu` loeb numbri (`ConsoleInput.readInt`) ja valib `switch`-iga meetodi `rentVehicle()`.
3. Menüü küsib id-d ja päevad ning kutsub `service.rentVehicle("V1", "C1", 3)`.
4. Teenus otsib `HashMap`-ist sõiduki ja kliendi id järgi. Kui ei leia, viskab `RentalException`.
5. Teenus kontrollib juhiluba (`customer.hasLicenseFor(vehicle)`).
6. Teenus loob `new Rental(vehicle, customer, 3)`. `Rental` kutsub `vehicle.calculateRentalCost(3)`.
   Kuna V1 on `Car`, käivitub `Car`-i versioon (**polümorfism**). Tulemus: 45.0 × 3 = 135.0.
7. Teenus kutsub `vehicle.rent()`: kui sõiduk on juba laenutatud, viskab erindi, muidu
   `available` muutub `false`.
8. Teenus lisab laenutuse `ArrayList`-i ja tagastab selle. Menüü prindib summa.
9. Kui kuskil viskati erind, püüab `ConsoleMenu.run()` selle kinni (`catch`) ja prindib
   "Viga: ...". Programm jätkab.

## 4. Mis juhtub, kui midagi muuta

| Muudatus | Tagajärg |
|---|---|
| `MIN_YEAR` 1950 asemel 2000 | `setYear(1990)` viskab `IllegalArgumentException`. Menüüs tuleb "Viga: ..." |
| `dailyRate` `private` asemel `public` | Mis tahes kood saaks kirjutada `vehicle.dailyRate = -5` ja valideerimine jääks vahele. See ongi kapseldamise mõte |
| `abstract` ära võtta klassilt `Vehicle` | Ei kompileeru: tavaline klass ei tohi sisaldada abstraktseid meetodeid |
| `HashSet<String>` asemel `ArrayList<String>` märkidele | Duplikaate ei välditaks automaatselt (`add` lisaks alati) ja `contains` oleks aeglasem, sest käiks listi järjest läbi |
| `LUXURY_MULTIPLIER` 1.3 asemel 1.5 | BMW 3 päeva maksab 405.00 EUR, mitte 351.00 EUR |
| `Car`-is `calculateRentalCost` ümber nimetada | Ei kompileeru: `Car` ei täida enam ülemklassi abstraktset meetodit |
| Menüüst `catch` ära võtta | `RentalException` on kontrollitud erind, seega kood ei kompileeruks (peab olema `catch` või `throws`). `IllegalArgumentException` kompileeruks, aga vigane sisend jooksutaks programmi kokku |

## 5. Enesetest

Proovi vastata enne vastuse vaatamist.

1. **`Vehicle v = new Car(...); v.calculateRentalCost(3);` Kumma klassi versioon käivitub?**
   `Car`-i oma. Valik tehakse töö ajal objekti tegeliku tüübi järgi, mitte muutuja tüübi järgi.
2. **Miks ei tohi kõik väljad olla `public`?** Siis saab igaüks väärtust ilma kontrollita muuta ja
   objekt võib minna vigasesse olekusse (negatiivne hind, tühi nimi).
3. **Mille poolest erinevad `ArrayList` ja `HashMap`?** `ArrayList` on järjestatud nimekiri, otsimine
   käib järjest. `HashMap` hoiab võti-väärtus paare, otsing võtme järgi on kiire ja järjekorda ei ole.
4. **Millal liides ja millal abstraktne klass?** Abstraktne klass, kui alamklassidel on ühine olek ja
   osa valmis koodi (`Vehicle`). Liides, kui on vaja ainult lepet, mida võivad täita ka üksteisega
   mitteseotud klassid (`Rentable`). Klass pärib ühe klassi, aga võib realiseerida mitu liidest.
5. **Mis vahe on `==` ja `.equals()`?** `==` võrdleb viiteid (kas sama objekt), `.equals()` sisu.
6. **Mis vahe on `int`, `Integer` ja `String`?** `int` on primitiiv, `Integer` on `int` objektina
   (võib olla `null`), `String` on viitetüüp tekstile.
7. **Mis vahe on gitis `commit` ja `push`?** `commit` salvestab muudatuse kohalikku ajalukku,
   `push` saadab commit'id GitHubi.
