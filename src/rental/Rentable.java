package rental;

/**
 * Liides: leping selle kohta, mida "laenutatav asi" peab oskama.
 * Liides ütleb MIDA tehakse, aga mitte KUIDAS.
 */
public interface Rentable {

    /** Märgib objekti laenutatuks. Viskab erindi, kui see on juba laenutatud. */
    void rent() throws RentalException;

    /** Märgib objekti tagastatuks. Viskab erindi, kui see polnudki laenutatud. */
    void returnVehicle() throws RentalException;

    boolean isAvailable();
}
