import java.util.*;

/**
 * MAIN CLASS - UseCase10BookingCancellation
 * Demonstrates state reversal and inventory rollback using a Stack (LIFO).
 * @version 10.1
 */
public class UseCase10BookingCancellation {

    /**
     * INNER CLASS - CancellationService
     * Handles the logic of undoing a booking and restoring inventory.
     */
    static class CancellationService {
        // Stack to track room IDs that have been released (Last-In-First-Out)
        private Stack<String> releasedRooms = new Stack<>();

        /**
         * Cancels a booking and performs a state rollback.
         * @param roomId The ID of the room to be returned to inventory
         * @param roomType The category of the room
         * @param inventory The centralized inventory to increment
         */
        public void cancelBooking(String roomId, String roomType, RoomInventory inventory) {
            System.out.println("Initiating cancellation for Room ID: " + roomId);

            // 1. Record the room ID in the rollback structure
            releasedRooms.push(roomId);

            // 2. Inventory Restoration: Increment the count
            String inventoryKey = roomType + " Room";
            int currentCount = inventory.getRoomAvailability().getOrDefault(inventoryKey, 0);
            inventory.updateAvailability(inventoryKey, currentCount + 1);

            System.out.println("SUCCESS: Room " + roomId + " released. Inventory updated.");
            System.out.println("Last released room (LIFO): " + releasedRooms.peek());
            System.out.println("----------------------------------------------");
        }
    }

    public static void main(String[] args) {
        // Initialize System State
        RoomInventory inventory = new RoomInventory();
        CancellationService cancellationService = new CancellationService();

        // Initial State Check
        System.out.println("Initial Inventory (Single Rooms): " +
                inventory.getRoomAvailability().get("Single Room") + "\n");

        // Scenario: A guest cancels their 'Single-101' booking
        // This simulates a rollback of an operation from Use Case 6
        cancellationService.cancelBooking("Single-101", "Single", inventory);

        // Verify Inventory Rollback
        System.out.println("Final Inventory (Single Rooms): " +
                inventory.getRoomAvailability().get("Single Room"));

        System.out.println("\nSystem state restored consistently.");
    }
}