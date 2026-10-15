package rental;

/**
 * Mootorratas.
 * Hinnareegel: alates 7 päevast saab 15% soodustust (nädalahind).
 */
public class Motorcycle extends Vehicle {

    private static final int DISCOUNT_FROM_DAYS = 7;
    private static final double WEEKLY_DISCOUNT = 0.15;

    private int engineCc;

    public Motorcycle(String id, String registrationNumber, String brand, String model,
                      int year, double dailyRate, int engineCc) {
        super(id, registrationNumber, brand, model, year, dailyRate);
        setEngineCc(engineCc);
    }

    @Override
    public double calculateRentalCost(int days) {
        requireDays(days);
        double cost = getDailyRate() * days;
        if (days >= DISCOUNT_FROM_DAYS) {
            cost = cost * (1 - WEEKLY_DISCOUNT);
        }
        return cost;
    }

    @Override
    public String getTypeName() {
        return "Mootorratas";
    }

    @Override
    public String getRequiredLicense() {
        return "A";
    }

    public int getEngineCc() {
        return engineCc;
    }

    public void setEngineCc(int engineCc) {
        if (engineCc < 50 || engineCc > 2500) {
            throw new IllegalArgumentException("Mootori maht peab olema 50 kuni 2500 cm3.");
        }
        this.engineCc = engineCc;
    }

    @Override
    public String toString() {
        return super.toString() + ", mootor: " + engineCc + " cm3";
    }
}
