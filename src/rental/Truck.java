package rental;

/**
 * Veoauto.
 * Hinnareegel: päevahinnale lisandub kandevõimega seotud tasu,
 * 5 EUR iga kandevõime tonni kohta päevas.
 */
public class Truck extends Vehicle {

    private static final double FEE_PER_TON_PER_DAY = 5.0;

    private double loadCapacityKg;

    public Truck(String id, String registrationNumber, String brand, String model,
                 int year, double dailyRate, double loadCapacityKg) {
        super(id, registrationNumber, brand, model, year, dailyRate);
        setLoadCapacityKg(loadCapacityKg);
    }

    @Override
    public double calculateRentalCost(int days) {
        requireDays(days);
        double extraPerDay = (loadCapacityKg / 1000.0) * FEE_PER_TON_PER_DAY;
        return (getDailyRate() + extraPerDay) * days;
    }

    @Override
    public String getTypeName() {
        return "Veoauto";
    }

    @Override
    public String getRequiredLicense() {
        return "C";
    }

    public double getLoadCapacityKg() {
        return loadCapacityKg;
    }

    public void setLoadCapacityKg(double loadCapacityKg) {
        if (loadCapacityKg <= 0) {
            throw new IllegalArgumentException("Kandevõime peab olema suurem kui 0.");
        }
        this.loadCapacityKg = loadCapacityKg;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(", kandevõime: %.0f kg", loadCapacityKg);
    }
}
