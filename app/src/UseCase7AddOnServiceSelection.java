import java.util.*;

/**
 * MAIN CLASS - UseCase7AddOnServiceSelection
 * Demonstrates business extensibility using Map<String, List<Service>>.
 * @version 7.1
 */
public class UseCase7AddOnServiceSelection {

    /**
     * INNER CLASS - Service
     * Represents an optional add-on like Breakfast or Spa.
     */
    static class Service {
        private String name;
        private double price;

        public Service(String name, double price) {
            this.name = name;
            this.price = price;
        }

        public String getName() { return name; }
        public double getPrice() { return price; }
    }

    /**
     * INNER CLASS - AddOnServiceManager
     * Manages the association between a Reservation ID and its selected services.
     */
    static class AddOnServiceManager {
        // Map: Reservation ID -> List of Services (One-to-Many)
        private Map<String, List<Service>> reservationServices = new HashMap<>();

        /**
         * Adds a service to a specific reservation.
         */
        public void addService(String reservationId, Service service) {
            // computeIfAbsent creates a new ArrayList if the key doesn't exist yet
            reservationServices.computeIfAbsent(reservationId, k -> new ArrayList<>()).add(service);
            System.out.println("Added " + service.getName() + " to Reservation: " + reservationId);
        }

        /**
         * Calculates and displays the total add-on costs.
         */
        public void displayAddOns(String reservationId) {
            List<Service> services = reservationServices.get(reservationId);
            if (services == null || services.isEmpty()) {
                System.out.println("No add-ons for Reservation: " + reservationId);
                return;
            }

            double total = 0;
            System.out.println("\nAdd-on Details for " + reservationId + ":");
            for (Service s : services) {
                System.out.println("- " + s.getName() + ": " + s.getPrice());
                total += s.getPrice();
            }
            System.out.println("Total Add-on Cost: " + total + "\n");
        }
    }

    public static void main(String[] args) {
        System.out.println("--- Add-On Service Selection ---");

        // 1. Initialize the Manager
        AddOnServiceManager manager = new AddOnServiceManager();

        // 2. Define available services
        Service breakfast = new Service("Buffet Breakfast", 500.0);
        Service spa = new Service("Luxury Spa", 1500.0);
        Service wifi = new Service("Premium WiFi", 200.0);

        // 3. Simulating adding services to confirmed reservations
        // Let's assume these IDs were generated in Use Case 6
        String resId1 = "Single-101";
        String resId2 = "Double-103";

        manager.addService(resId1, breakfast);
        manager.addService(resId1, wifi);

        manager.addService(resId2, spa);
        manager.addService(resId2, breakfast);

        // 4. Display results and costs
        manager.displayAddOns(resId1);
        manager.displayAddOns(resId2);

        System.out.println("Core booking and inventory remain unchanged.");
    }
}