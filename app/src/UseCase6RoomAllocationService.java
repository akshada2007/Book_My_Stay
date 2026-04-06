import java.util.*;

/**
 * MAIN CLASS - UseCase6RoomAllocationService
 * Integrates Queue, Map, and Set to perform safe room allocation.
 * @version 6.1
 */
public class UseCase6RoomAllocationService {

    // Inner Class: Reservation Data Model (from UC5)
    static class Reservation {
        private String guestName;
        private String roomType;

        public Reservation(String guestName, String roomType) {
            this.guestName = guestName;
            this.roomType = roomType;
        }
        public String getGuestName() { return guestName; }
        public String getRoomType() { return roomType; }
    }

    /**
     * INNER CLASS - AllocationService
     * Handles the logic of dequeuing requests and assigning unique IDs.
     */
    static class AllocationService {
        // Maps Room Type -> Set of assigned Room IDs (to prevent double booking)
        private Map<String, Set<String>> allocatedRooms = new HashMap<>();

        public AllocationService() {
            allocatedRooms.put("Single", new HashSet<>());
            allocatedRooms.put("Double", new HashSet<>());
            allocatedRooms.put("Suite", new HashSet<>());
        }

        public void processAllocations(Queue<Reservation> queue, RoomInventory inventory) {
            System.out.println("Processing Room Allocations...");
            System.out.println("------------------------------");

            while (!queue.isEmpty()) {
                Reservation request = queue.poll();
                String type = request.getRoomType();

                // 1. Check Centralized Inventory
                int available = inventory.getRoomAvailability().getOrDefault(type + " Room", 0);

                if (available > 0) {
                    // 2. Generate Unique Room ID (e.g., Single-101)
                    String roomID = type + "-" + (100 + (6 - available));

                    // 3. Use Set to ensure uniqueness (Enforce No Double-Booking)
                    if (allocatedRooms.get(type).add(roomID)) {
                        // 4. Atomic Update: Decrement Inventory
                        inventory.updateAvailability(type + " Room", available - 1);

                        System.out.println("CONFIRMED: " + request.getGuestName() +
                                " assigned to " + roomID);
                    }
                } else {
                    System.out.println("FAILED: No " + type + " rooms left for " + request.getGuestName());
                }
            }
            System.out.println("------------------------------\n");
        }
    }

    public static void main(String[] args) {
        // Setup System State
        RoomInventory inventory = new RoomInventory();
        AllocationService allocationService = new AllocationService();
        Queue<Reservation> requestQueue = new LinkedList<>();

        // Simulate incoming requests in FIFO order
        requestQueue.add(new Reservation("Abhi", "Single"));
        requestQueue.add(new Reservation("Subha", "Double"));
        requestQueue.add(new Reservation("Vanmathi", "Suite"));
        requestQueue.add(new Reservation("Akash", "Single")); // Second single room request

        // Run Allocation
        allocationService.processAllocations(requestQueue, inventory);

        // Verify Inventory Synchronization
        System.out.println("Final Inventory Status:");
        inventory.getRoomAvailability().forEach((type, count) ->
                System.out.println(type + ": " + count + " left"));
    }
}