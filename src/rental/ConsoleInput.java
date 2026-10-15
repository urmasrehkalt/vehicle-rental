package rental;

import java.util.Scanner;

/**
 * Kasutaja sisendi lugemine koos kontrolliga. Vale sisendi korral küsitakse uuesti,
 * seega programm ei kuku kokku, kui kasutaja kirjutab numbri asemel tähti.
 */
public class ConsoleInput {

    private final Scanner scanner;

    public ConsoleInput(Scanner scanner) {
        this.scanner = scanner;
    }

    /** Loeb ühe rea teksti. Kui sisend lõpeb, viskab Scanner NoSuchElementException. */
    public String readText(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public int readInt(String prompt) {
        while (true) {
            String line = readText(prompt);
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Palun sisesta täisarv (näiteks 5).");
            }
        }
    }

    public int readIntInRange(String prompt, int min, int max) {
        while (true) {
            int value = readInt(prompt);
            if (value >= min && value <= max) {
                return value;
            }
            System.out.println("Number peab olema vahemikus " + min + " kuni " + max + ".");
        }
    }

    /** Aktsepteerib nii punkti kui koma (12.5 ja 12,5). */
    public double readDouble(String prompt) {
        while (true) {
            String line = readText(prompt).replace(',', '.');
            try {
                double value = Double.parseDouble(line);
                // parseDouble võtab vastu ka "NaN" ja "Infinity", need ei sobi hinnaks
                if (Double.isFinite(value)) {
                    return value;
                }
            } catch (NumberFormatException e) {
                // langeb alla ja küsib uuesti
            }
            System.out.println("Palun sisesta arv (näiteks 49.90).");
        }
    }

    public boolean readYesNo(String prompt) {
        while (true) {
            String line = readText(prompt + " (j/e): ").toLowerCase();
            if (line.equals("j") || line.equals("jah")) {
                return true;
            }
            if (line.equals("e") || line.equals("ei")) {
                return false;
            }
            System.out.println("Palun vasta j (jah) või e (ei).");
        }
    }
}
