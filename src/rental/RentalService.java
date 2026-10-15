package rental;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Rendisüsteemi põhiloogika. Menüü (Main) ainult kutsub siinseid meetodeid,
 * seega on kasutajaliides ja äriloogika üksteisest lahus.
 *
 * Andmestruktuuride valik:
 *  - HashMap<String, Vehicle> / HashMap<String, Customer>: otsing id järgi on kiire (O(1)),
 *    ei pea kogu nimekirja läbi käima.
 *  - HashSet<String> registreerimismärgid: set ei luba duplikaate,
 *    contains() on kiire. Seega saab kahte sama numbrimärgiga autot kergesti keelata.
 *  - ArrayList<Rental>: laenutusi lisatakse järjest ja nende järjekord on oluline.
 */
public class RentalService {

    private final Map<String, Vehicle> vehicles = new HashMap<>();
    private final Map<String, Customer> customers = new HashMap<>();
    private final Set<String> registrationNumbers = new HashSet<>();
    private final List<Rental> rentals = new ArrayList<>();

    public void addVehicle(Vehicle vehicle) throws RentalException {
        if (vehicles.containsKey(vehicle.getId())) {
            throw new RentalException("Sõiduk id-ga " + vehicle.getId() + " on juba olemas.");
        }
        // add() tagastab false, kui sama väärtus oli juba hulgas
        if (!registrationNumbers.add(vehicle.getRegistrationNumber())) {
            throw new RentalException(
                    "Registreerimismärk " + vehicle.getRegistrationNumber() + " on juba kasutusel.");
        }
        vehicles.put(vehicle.getId(), vehicle);
    }

    public void addCustomer(Customer customer) throws RentalException {
        if (customers.containsKey(customer.getId())) {
            throw new RentalException("Klient id-ga " + customer.getId() + " on juba olemas.");
        }
        customers.put(customer.getId(), customer);
    }

    public Vehicle getVehicle(String id) throws RentalException {
        Vehicle vehicle = vehicles.get(id);
        if (vehicle == null) {
            throw new RentalException("Sõidukit id-ga " + id + " ei leitud.");
        }
        return vehicle;
    }

    public Customer getCustomer(String id) throws RentalException {
        Customer customer = customers.get(id);
        if (customer == null) {
            throw new RentalException("Klienti id-ga " + id + " ei leitud.");
        }
        return customer;
    }

    /** Laenutab sõiduki kliendile. Tagastab loodud laenutuse. */
    public Rental rentVehicle(String vehicleId, String customerId, int days) throws RentalException {
        Vehicle vehicle = getVehicle(vehicleId);
        Customer customer = getCustomer(customerId);

        // Rental luuakse enne: kui päevade arv on vigane, viskab ta erindi
        // ja sõiduk jääb saadavaks.
        Rental rental = new Rental(vehicle, customer, days);
        vehicle.rent();   // viskab erindi, kui sõiduk on juba laenutatud
        rentals.add(rental);
        return rental;
    }

    /** Võtab sõiduki tagasi. Tagastab laenutuse, mis lõpetati. */
    public Rental returnVehicle(String vehicleId) throws RentalException {
        Vehicle vehicle = getVehicle(vehicleId);
        Rental active = findActiveRental(vehicleId);
        if (active == null) {
            throw new RentalException("Sõiduk " + vehicleId + " ei ole laenutatud.");
        }
        vehicle.returnVehicle();
        active.markReturned();
        return active;
    }

    private Rental findActiveRental(String vehicleId) {
        for (Rental rental : rentals) {
            if (rental.getVehicle().getId().equals(vehicleId) && !rental.isReturned()) {
                return rental;
            }
        }
        return null;
    }

    /**
     * Tagastab koopia, mitte sisemist kollektsiooni. Nii ei saa väljaspool
     * klassi nimekirja kogemata muuta (kapseldamine).
     * HashMap ei hoia järjekorda, seepärast sorteerime id järgi.
     */
    public List<Vehicle> getAllVehicles() {
        List<Vehicle> result = new ArrayList<>(vehicles.values());
        result.sort(Comparator.comparing(Vehicle::getId));
        return result;
    }

    /**
     * Otsib sõidukeid, mille mark või mudel sisaldab otsingutekstu (suur- ja väiketähti ei vahet).
     * Tühi tulemus on tühi nimekiri, mitte viga.
     */
    public List<Vehicle> searchByText(String text) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Otsingutekst ei tohi olla tühi.");
        }
        String needle = text.trim().toLowerCase();
        List<Vehicle> result = new ArrayList<>();
        for (Vehicle vehicle : getAllVehicles()) {
            if (vehicle.getBrand().toLowerCase().contains(needle)
                    || vehicle.getModel().toLowerCase().contains(needle)) {
                result.add(vehicle);
            }
        }
        return result;
    }

    /** Filtreerib tüübi järgi, nt "Auto", "Mootorratas" või "Veoauto" (vt getTypeName). */
    public List<Vehicle> filterByType(String typeName) {
        List<Vehicle> result = new ArrayList<>();
        for (Vehicle vehicle : getAllVehicles()) {
            if (vehicle.getTypeName().equalsIgnoreCase(typeName)) {
                result.add(vehicle);
            }
        }
        return result;
    }

    public List<Vehicle> filterAvailable() {
        List<Vehicle> result = new ArrayList<>();
        for (Vehicle vehicle : getAllVehicles()) {
            if (vehicle.isAvailable()) {
                result.add(vehicle);
            }
        }
        return result;
    }

    /** Sõidukid, mille päevahind on kuni antud summa (piir kaasa arvatud). */
    public List<Vehicle> filterByMaxDailyRate(double maxRate) {
        if (maxRate <= 0) {
            throw new IllegalArgumentException("Maksimaalne hind peab olema suurem kui 0.");
        }
        List<Vehicle> result = new ArrayList<>();
        for (Vehicle vehicle : getAllVehicles()) {
            if (vehicle.getDailyRate() <= maxRate) {
                result.add(vehicle);
            }
        }
        return result;
    }

    /** Sorteerib päevahinna järgi odavamast kallimani. Algset nimekirja ei muudeta. */
    public List<Vehicle> sortByDailyRate(List<Vehicle> list) {
        List<Vehicle> sorted = new ArrayList<>(list);
        sorted.sort(Comparator.comparingDouble(Vehicle::getDailyRate));
        return sorted;
    }

    /** Sorteerib aasta järgi uuemast vanemani. Algset nimekirja ei muudeta. */
    public List<Vehicle> sortByYearNewestFirst(List<Vehicle> list) {
        List<Vehicle> sorted = new ArrayList<>(list);
        sorted.sort(Comparator.comparingInt(Vehicle::getYear).reversed());
        return sorted;
    }

    public List<Customer> getAllCustomers() {
        List<Customer> result = new ArrayList<>(customers.values());
        result.sort(Comparator.comparing(Customer::getId));
        return result;
    }

    public List<Rental> getAllRentals() {
        return new ArrayList<>(rentals);
    }
}
