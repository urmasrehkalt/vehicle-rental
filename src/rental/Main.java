package rental;

import java.util.Scanner;

/** Programmi sisendpunkt: valmistab andmed ette ja käivitab menüü. */
public class Main {

    public static void main(String[] args) {
        RentalService service = new RentalService();
        try {
            SampleData.load(service);
        } catch (RentalException e) {
            System.out.println("Näidisandmete laadimine ebaõnnestus: " + e.getMessage());
        }

        Scanner scanner = new Scanner(System.in);
        new ConsoleMenu(service, new ConsoleInput(scanner)).run();
    }
}
