package rental;

/**
 * Erind äriloogika vigade jaoks (näiteks sõiduk on juba laenutatud).
 * Kontrollitud erind (extends Exception): kutsuja peab selle käsitlema try/catch plokiga.
 */
public class RentalException extends Exception {

    public RentalException(String message) {
        super(message);
    }
}
