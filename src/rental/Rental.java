package rental;

import java.time.LocalDate;

/**
 * Üks laenutus: milline klient laenutas millist sõidukit ja mitmeks päevaks.
 * Summa arvutatakse laenutuse loomise hetkel ja jääb pärast seda samaks.
 */
public class Rental {

    private final Vehicle vehicle;
    private final Customer customer;
    private final int days;
    private final double totalCost;
    private final LocalDate startDate;
    private boolean returned = false;

    public Rental(Vehicle vehicle, Customer customer, int days) {
        if (vehicle == null || customer == null) {
            throw new IllegalArgumentException("Sõiduk ja klient peavad olema määratud.");
        }
        this.vehicle = vehicle;
        this.customer = customer;
        this.days = days;
        // Polümorfism: vehicle võib olla Car, Motorcycle või Truck,
        // õige hinnaarvutus valitakse töö ajal objekti tegeliku tüübi järgi.
        // See viskab erindi ka siis, kui days <= 0.
        this.totalCost = vehicle.calculateRentalCost(days);
        this.startDate = LocalDate.now();
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public Customer getCustomer() {
        return customer;
    }

    public int getDays() {
        return days;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public boolean isReturned() {
        return returned;
    }

    /** Märgib laenutuse lõpetatuks. Tagastada saab ainult ühe korra. */
    public void markReturned() throws RentalException {
        if (returned) {
            throw new RentalException("See laenutus on juba tagastatud.");
        }
        returned = true;
    }

    @Override
    public String toString() {
        return String.format("%s -> %s, %d päeva, %.2f EUR, algus %s, %s",
                customer.getName(), vehicle.getId(), days, totalCost, startDate,
                returned ? "tagastatud" : "käimas");
    }
}
