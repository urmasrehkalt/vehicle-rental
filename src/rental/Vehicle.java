package rental;

import java.time.Year;

/**
 * Abstraktne ülemklass kõigile sõidukitele.
 * Abstraktsest klassist ei saa objekti luua (new Vehicle(...) ei kompileeru),
 * küll aga saavad sellest pärida Car, Motorcycle ja Truck.
 */
public abstract class Vehicle implements Rentable {

    private static final int MIN_YEAR = 1950;

    // Kapseldamine: kõik väljad on private, ligipääs ainult meetodite kaudu.
    private final String id;               // final: id ei muutu pärast loomist
    private String registrationNumber;
    private String brand;
    private String model;
    private int year;
    private double dailyRate;              // baashind ühe päeva eest (eurodes)
    private boolean available = true;

    protected Vehicle(String id, String registrationNumber, String brand,
                      String model, int year, double dailyRate) {
        this.id = requireText(id, "Id");
        // Setterite kaudu, et sama kontroll kehtiks nii loomisel kui hilisemal muutmisel
        setRegistrationNumber(registrationNumber);
        setBrand(brand);
        setModel(model);
        setYear(year);
        setDailyRate(dailyRate);
    }

    /**
     * Polümorfism: iga alamklass arvutab hinna omal viisil.
     * Kutsuja ei pea teadma, kas tegu on auto, mootorratta või veoautoga.
     */
    public abstract double calculateRentalCost(int days);

    /** Sõiduki tüübi nimi (nt "Auto"), kasutatakse filtreerimisel ja väljastamisel. */
    public abstract String getTypeName();

    @Override
    public void rent() throws RentalException {
        if (!available) {
            throw new RentalException("Sõiduk " + id + " on juba laenutatud.");
        }
        available = false;
    }

    @Override
    public void returnVehicle() throws RentalException {
        if (available) {
            throw new RentalException("Sõiduk " + id + " ei ole laenutatud.");
        }
        available = true;
    }

    @Override
    public boolean isAvailable() {
        return available;
    }

    public String getId() {
        return id;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        // Registreerimismärk salvestatakse alati suurte tähtedega: "123abc" ja "123ABC" on sama
        this.registrationNumber = requireText(registrationNumber, "Registreerimismärk").toUpperCase();
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = requireText(brand, "Mark");
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = requireText(model, "Mudel");
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        int maxYear = Year.now().getValue() + 1;
        if (year < MIN_YEAR || year > maxYear) {
            throw new IllegalArgumentException(
                    "Aasta peab olema vahemikus " + MIN_YEAR + " kuni " + maxYear + ".");
        }
        this.year = year;
    }

    public double getDailyRate() {
        return dailyRate;
    }

    public void setDailyRate(double dailyRate) {
        if (dailyRate <= 0) {
            throw new IllegalArgumentException("Päevahind peab olema suurem kui 0.");
        }
        this.dailyRate = dailyRate;
    }

    /** Ühine abimeetod alamklassidele: renti päevade arv peab olema vähemalt 1. */
    protected static void requireDays(int days) {
        if (days <= 0) {
            throw new IllegalArgumentException("Päevade arv peab olema suurem kui 0.");
        }
    }

    /** Ühine abimeetod: tekst ei tohi olla null ega tühi. Tagastab kärbitud teksti. */
    protected static String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " ei tohi olla tühi.");
        }
        return value.trim();
    }

    @Override
    public String toString() {
        return String.format("[%s] %s %s %s (%d), %s, %.2f EUR/päev, %s",
                id, getTypeName(), brand, model, year, registrationNumber, dailyRate,
                available ? "saadaval" : "laenutatud");
    }
}
