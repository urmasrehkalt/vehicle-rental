package rental;

/** Näidisandmed, et programmi saaks kohe proovida. */
public final class SampleData {

    private SampleData() {
    }

    public static void load(RentalService service) throws RentalException {
        service.addVehicle(new Car("V1", "123ABC", "Toyota", "Corolla", 2020, 45.0, 5, false));
        service.addVehicle(new Car("V2", "456DEF", "BMW", "520d", 2022, 90.0, 5, true));
        service.addVehicle(new Car("V3", "789GHI", "Volkswagen", "Golf", 2018, 35.0, 5, false));
        service.addVehicle(new Motorcycle("V4", "111MOT", "Honda", "CB650R", 2021, 40.0, 650));
        service.addVehicle(new Motorcycle("V5", "222MOT", "Yamaha", "MT-07", 2019, 35.0, 689));
        service.addVehicle(new Truck("V6", "333TRK", "MAN", "TGL", 2017, 80.0, 7500));

        service.addCustomer(new Customer("C1", "Mari Tamm", "B"));
        service.addCustomer(new Customer("C2", "Jaan Kask", "A"));
        service.addCustomer(new Customer("C3", "Liis Mägi", "C"));
    }
}
