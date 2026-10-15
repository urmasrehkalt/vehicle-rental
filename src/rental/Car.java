package rental;

/**
 * Sõiduauto. Pärib kõik Vehicle väljad ja meetodid ning lisab omad.
 * Hinnareegel: luksusauto on 30% kallim.
 */
public class Car extends Vehicle {

    private static final double LUXURY_MULTIPLIER = 1.3;

    private int seats;
    private boolean luxury;

    public Car(String id, String registrationNumber, String brand, String model,
               int year, double dailyRate, int seats, boolean luxury) {
        super(id, registrationNumber, brand, model, year, dailyRate); // ülemklassi konstruktor
        setSeats(seats);
        this.luxury = luxury;
    }

    @Override
    public double calculateRentalCost(int days) {
        requireDays(days);
        double cost = getDailyRate() * days;
        return luxury ? cost * LUXURY_MULTIPLIER : cost;
    }

    @Override
    public String getTypeName() {
        return "Auto";
    }

    @Override
    public String getRequiredLicense() {
        return "B";
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        if (seats < 2 || seats > 9) {
            throw new IllegalArgumentException("Istekohti peab olema 2 kuni 9.");
        }
        this.seats = seats;
    }

    public boolean isLuxury() {
        return luxury;
    }

    public void setLuxury(boolean luxury) {
        this.luxury = luxury;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(", kohti: %d%s", seats, luxury ? ", luksus" : "");
    }
}
