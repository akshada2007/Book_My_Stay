import java.io.*;
import java.util.*;

/**
 * MAIN CLASS - UseCase12DataPersistenceRecovery
 * Demonstrates Object Serialization and Deserialization for system recovery.
 * @version 12.1
 */
public class UseCase12DataPersistenceRecovery {

    /**
     * INNER CLASS - SystemState
     * A wrapper class that holds all data we want to save.
     * MUST implement Serializable to be written to a file.
     */
    static class SystemState implements Serializable {
        private static final long serialVersionUID = 1L; // Ensures version compatibility
        Map<String, Integer> inventory;
        List<String> history;

        public SystemState(Map<String, Integer> inventory, List<String> history) {
            this.inventory = inventory;
            this.history = history;
        }
    }

    /**
     * INNER CLASS - PersistenceService
     * Logic for saving and loading the SystemState to/from a file.
     */
    static class PersistenceService {
        private static final String DATA_FILE = "hotel_state.ser";

        /**
         * Saves the current state to a physical file.
         */
        public void saveState(SystemState state) {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
                oos.writeObject(state);
                System.out.println("SYSTEM: State serialized and saved to " + DATA_FILE);
            } catch (IOException e) {
                System.err.println("ERROR: Could not save state: " + e.getMessage());
            }
        }

        /**
         * Loads the state from the file if it exists.
         */
        public SystemState loadState() {
            File file = new File(DATA_FILE);
            if (!file.exists()) {
                System.out.println("SYSTEM: No previous state found. Starting fresh.");
                return null;
            }

            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
                System.out.println("SYSTEM: Restoring state from " + DATA_FILE + "...");
                return (SystemState) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("ERROR: Recovery failed: " + e.getMessage());
                return null;
            }
        }
    }

    public static void main(String[] args) {
        PersistenceService persistence = new PersistenceService();

        // 1. Attempt System Recovery
        SystemState recoveredState = persistence.loadState();

        Map<String, Integer> currentInventory;
        List<String> bookingHistory;

        if (recoveredState != null) {
            currentInventory = recoveredState.inventory;
            bookingHistory = recoveredState.history;
            System.out.println("RECOVERY SUCCESSFUL. Current History size: " + bookingHistory.size());
        } else {
            // Initializing fresh state if no file exists
            currentInventory = new HashMap<>();
            currentInventory.put("Single Room", 5);
            bookingHistory = new ArrayList<>();
        }

        // 2. Simulate a new booking during this session
        System.out.println("\n--- Processing New Booking ---");
        bookingHistory.add("Guest: Rahul | Room: Single-105");
        currentInventory.put("Single Room", currentInventory.get("Single Room") - 1);

        System.out.println("New Booking Added. History Count: " + bookingHistory.size());
        System.out.println("Inventory: " + currentInventory.get("Single Room") + " left.");

        // 3. Prepare for Shutdown: Save everything
        SystemState stateToSave = new SystemState(currentInventory, bookingHistory);
        persistence.saveState(stateToSave);

        System.out.println("\nApplication shutting down safely. Try running it again to see the data persist!");
    }
}