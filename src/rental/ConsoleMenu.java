package rental;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Tekstimenüü. Siin on ainult kasutajaga suhtlemine: küsib andmeid, kutsub
 * RentalService meetodeid ja väljastab tulemuse. Reeglid ja arvutused on teenuses.
 */
public class ConsoleMenu {

    private final RentalService service;
    private final ConsoleInput input;

    public ConsoleMenu(RentalService service, ConsoleInput input) {
        this.service = service;
        this.input = input;
    }

    public void run() {
        boolean running = true;
        try {
            while (running) {
                printMenu();
                int choice = input.readInt("Vali tegevus: ");
                try {
                    switch (choice) {
                        case 1: printVehicles(service.getAllVehicles()); break;
                        case 2: addVehicle(); break;
                        case 3: addCustomer(); break;
                        case 4: printCustomers(); break;
                        case 5: rentVehicle(); break;
                        case 6: returnVehicle(); break;
                        case 7: printRentals(); break;
                        case 8: searchMenu(); break;
                        case 9: calculatePrice(); break;
                        case 0:
                            running = false;
                            System.out.println("Head aega!");
                            break;
                        default:
                            System.out.println("Tundmatu valik, vali 0 kuni 9.");
                    }
                } catch (RentalException | IllegalArgumentException e) {
                    // Äriloogika ja sisendi vead näitame kasutajale, programm jätkab tööd
                    System.out.println("Viga: " + e.getMessage());
                }
            }
        } catch (NoSuchElementException e) {
            // Sisend lõppes (nt Ctrl+D), sulgeme rahulikult
            System.out.println();
            System.out.println("Sisend lõppes, programm sulgub.");
        }
    }

    private void printMenu() {
        System.out.println();
        System.out.println("=== Sõidukite rendisüsteem ===");
        System.out.println("1. Sõidukite nimekiri");
        System.out.println("2. Lisa sõiduk");
        System.out.println("3. Lisa klient");
        System.out.println("4. Klientide nimekiri");
        System.out.println("5. Laenuta sõiduk");
        System.out.println("6. Tagasta sõiduk");
        System.out.println("7. Laenutuste nimekiri");
        System.out.println("8. Otsi, filtreeri, sorteeri");
        System.out.println("9. Arvuta rendihind");
        System.out.println("0. Välju");
    }

    private void printVehicles(List<Vehicle> vehicles) {
        if (vehicles.isEmpty()) {
            System.out.println("Midagi ei leitud.");
            return;
        }
        for (Vehicle vehicle : vehicles) {
            System.out.println(vehicle);
        }
    }

    private void printCustomers() {
        List<Customer> customers = service.getAllCustomers();
        if (customers.isEmpty()) {
            System.out.println("Kliente pole veel lisatud.");
            return;
        }
        for (Customer customer : customers) {
            System.out.println(customer);
        }
    }

    private void printRentals() {
        List<Rental> rentals = service.getAllRentals();
        if (rentals.isEmpty()) {
            System.out.println("Laenutusi pole veel tehtud.");
            return;
        }
        for (Rental rental : rentals) {
            System.out.println(rental);
        }
    }

    private void addVehicle() throws RentalException {
        System.out.println("Sõiduki tüüp: 1 = Auto, 2 = Mootorratas, 3 = Veoauto");
        int type = input.readIntInRange("Vali tüüp: ", 1, 3);

        String id = input.readText("Id (nt V10): ");
        String plate = input.readText("Registreerimismärk: ");
        String brand = input.readText("Mark: ");
        String model = input.readText("Mudel: ");
        int year = input.readInt("Valmistamisaasta: ");
        double rate = input.readDouble("Päevahind (EUR): ");

        Vehicle vehicle;
        switch (type) {
            case 1:
                int seats = input.readInt("Istekohtade arv: ");
                boolean luxury = input.readYesNo("Kas luksusauto?");
                vehicle = new Car(id, plate, brand, model, year, rate, seats, luxury);
                break;
            case 2:
                int engineCc = input.readInt("Mootori maht (cm3): ");
                vehicle = new Motorcycle(id, plate, brand, model, year, rate, engineCc);
                break;
            default:
                double capacity = input.readDouble("Kandevõime (kg): ");
                vehicle = new Truck(id, plate, brand, model, year, rate, capacity);
        }
        service.addVehicle(vehicle);
        System.out.println("Lisatud: " + vehicle);
    }

    private void addCustomer() throws RentalException {
        String id = input.readText("Kliendi id (nt C10): ");
        String name = input.readText("Nimi: ");
        String category = input.readText("Juhiloa kategooria (A, B või C): ");
        Customer customer = new Customer(id, name, category);
        service.addCustomer(customer);
        System.out.println("Lisatud: " + customer);
    }

    private void rentVehicle() throws RentalException {
        String vehicleId = input.readText("Sõiduki id: ");
        String customerId = input.readText("Kliendi id: ");
        int days = input.readInt("Mitmeks päevaks: ");
        Rental rental = service.rentVehicle(vehicleId, customerId, days);
        System.out.printf("Laenutus tehtud. Summa kokku: %.2f EUR%n", rental.getTotalCost());
    }

    private void returnVehicle() throws RentalException {
        String vehicleId = input.readText("Sõiduki id: ");
        Rental rental = service.returnVehicle(vehicleId);
        System.out.println("Sõiduk tagastatud. " + rental);
    }

    /** Näitab, kuidas sama meetodikutse annab erinevatele tüüpidele erineva hinna. */
    private void calculatePrice() throws RentalException {
        String vehicleId = input.readText("Sõiduki id: ");
        int days = input.readInt("Päevade arv: ");
        Vehicle vehicle = service.getVehicle(vehicleId);
        System.out.printf("%s %s %s, %d päeva: %.2f EUR%n",
                vehicle.getTypeName(), vehicle.getBrand(), vehicle.getModel(),
                days, vehicle.calculateRentalCost(days));
    }

    private void searchMenu() {
        System.out.println("1. Otsi margi või mudeli järgi");
        System.out.println("2. Filtreeri tüübi järgi");
        System.out.println("3. Ainult saadaolevad");
        System.out.println("4. Filtreeri maksimaalse päevahinna järgi");
        int choice = input.readIntInRange("Vali: ", 1, 4);

        List<Vehicle> result;
        switch (choice) {
            case 1:
                result = service.searchByText(input.readText("Otsingutekst: "));
                break;
            case 2:
                System.out.println("Tüüp: 1 = Auto, 2 = Mootorratas, 3 = Veoauto");
                int type = input.readIntInRange("Vali tüüp: ", 1, 3);
                String[] typeNames = {"Auto", "Mootorratas", "Veoauto"};
                result = service.filterByType(typeNames[type - 1]);
                break;
            case 3:
                result = service.filterAvailable();
                break;
            default:
                result = service.filterByMaxDailyRate(input.readDouble("Maksimaalne päevahind (EUR): "));
        }

        System.out.println("Sorteerimine: 0 = ei sorteeri, 1 = hinna järgi, 2 = uuemad enne");
        int sort = input.readIntInRange("Vali: ", 0, 2);
        if (sort == 1) {
            result = service.sortByDailyRate(result);
        } else if (sort == 2) {
            result = service.sortByYearNewestFirst(result);
        }
        printVehicles(result);
    }
}
