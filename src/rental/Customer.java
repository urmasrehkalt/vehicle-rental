package rental;

/**
 * Klient. Kõik väljad on private ja iga muutmine käib läbi kontrolliga setteri.
 */
public class Customer {

    private final String id;
    private String name;
    private String licenseCategory;   // juhiloa kategooria: A (mootorratas), B (auto), C (veoauto)

    public Customer(String id, String name, String licenseCategory) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Kliendi id ei tohi olla tühi.");
        }
        this.id = id.trim();
        setName(name);
        setLicenseCategory(licenseCategory);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().length() < 2) {
            throw new IllegalArgumentException("Nimi peab olema vähemalt 2 tähemärki.");
        }
        this.name = name.trim();
    }

    public String getLicenseCategory() {
        return licenseCategory;
    }

    public void setLicenseCategory(String licenseCategory) {
        // "b" ja "B" on sama kategooria, seega teisendame alati suurtähtedeks
        String category = licenseCategory == null ? "" : licenseCategory.trim().toUpperCase();
        if (!category.matches("[ABC]")) {
            throw new IllegalArgumentException("Juhiloa kategooria peab olema A, B või C.");
        }
        this.licenseCategory = category;
    }

    /** Kas kliendi juhiluba sobib antud sõiduki jaoks (kategooriad peavad täpselt klappima). */
    public boolean hasLicenseFor(Vehicle vehicle) {
        return licenseCategory.equals(vehicle.getRequiredLicense());
    }

    @Override
    public String toString() {
        return String.format("[%s] %s (kategooria %s)", id, name, licenseCategory);
    }
}
