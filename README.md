# Sõidukite rendisüsteem

Java käsurearakendus sõidukite rentimiseks. Koolitöö (TAK25): objektorienteeritud
programmeerimine, andmetüübid, andmestruktuurid ja Git.

Rakendus võimaldab hoida sõidukite nimekirja (auto, mootorratas, veoauto),
laenutada ja tagastada sõidukeid ning arvutada rendihinda. Graafilist
kasutajaliidest ja andmebaasi ei ole, andmed elavad programmi mälus.

## Nõuded

- JDK 17 või uuem (arendatud JDK 21-ga)

Versiooni saab kontrollida käsuga `java -version`.

## Kompileerimine ja käivitamine

Projekti juurkaustast:

```bash
# kompileeri kõik lähtefailid kausta out/
javac -d out src/rental/*.java

# käivita programm
java -cp out rental.Main
```

## Projekti struktuur

```
src/rental/
  Vehicle.java          abstraktne ülemklass kõigile sõidukitele
  Car.java              alamklass: auto (luksusauto +30%)
  Motorcycle.java       alamklass: mootorratas (alates 7 päevast -15%)
  Truck.java            alamklass: veoauto (lisatasu kandevõime järgi)
  Customer.java         klient (kapseldatud ja valideeritud väljad)
  Rental.java           üks laenutus (sõiduk, klient, päevad, summa)
  RentalService.java    põhiloogika: HashMap, HashSet ja ArrayList kollektsioonid
  Rentable.java         liides: laenutatav asi
  RentalException.java  oma erind äriloogika vigade jaoks
```

Dokumentatsiooni jätkatakse töö käigus.
