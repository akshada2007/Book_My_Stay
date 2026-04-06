import java.util.*;

/**
 * MAIN CLASS - UseCase11ConcurrentBookingSimulation
 * Demonstrates Thread Safety and Synchronized access to shared resources.
 * @version 11.1
 */
public class UseCase11ConcurrentBookingSimulation {

    // Inner Class: Reservation Data Model
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
     * INNER CLASS - ConcurrentBookingProcessor
     * Handles processing booking requests using Multiple Threads.
     */
    static class ConcurrentBookingProcessor extends Thread {
        private String threadName;
        private Queue<Reservation> sharedQueue;
        private RoomInventory inventory;

        public ConcurrentBookingProcessor(String name, Queue<Reservation> queue, RoomInventory inv) {
            this.threadName = name;
            this.sharedQueue = queue;
            this.inventory = inv;
        }

        @Override
        public void run() {
            while (true) {
                Reservation request = null;

                // CRITICAL SECTION 1: Accessing the shared Queue
                synchronized (sharedQueue) {
                    if (sharedQueue.isEmpty()) break;
                    request = sharedQueue.poll();
                }

                if (request != null) {
                    processBooking(request);
                }
            }
        }

        /**
         * Logic for allocating a room. 
         * Synchronized on 'inventory' to prevent Race Conditions.
         */
        private void processBooking(Reservation request) {
            String typeKey = request.getRoomType() + " Room";

            // CRITICAL SECTION 2: Updating shared Inventory
            synchronized (inventory) {
                int available = inventory.getRoomAvailability().getOrDefault(typeKey, 0);

                if (available > 0) {
                    // Simulate processing time
                    try { Thread.sleep(50); } catch (InterruptedException e) {}

                    inventory.updateAvailability(typeKey, available - 1);
                    System.out.println("[" + threadName + "] CONFIRMED: " +
                            request.getGuestName() + " (Remaining: " + (available - 1) + ")");
                } else {
                    System.out.println("[" + threadName + "] FAILED: No rooms for " + request.getGuestName());
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // 1. Initialize Shared Resources
        RoomInventory inventory = new RoomInventory(); // Shared Inventory
        Queue<Reservation> sharedQueue = new LinkedList<>(); // Shared Queue

        // 2. Load the Queue with many requests
        for (int i = 1; i <= 10; i++) {
            sharedQueue.add(new Reservation("Guest-" + i, "Single"));
        }

        System.out.println("--- Starting Concurrent Booking Simulation ---");
        System.out.println("Initial Inventory: 5 Single Rooms\n");

        // 3. Create multiple threads (Processors) acting as concurrent users/services
        ConcurrentBookingProcessor p1 = new ConcurrentBookingProcessor("Thread-Alpha", sharedQueue, inventory);
        ConcurrentBookingProcessor p2 = new ConcurrentBookingProcessor("Thread-Beta", sharedQueue, inventory);

        // 4. Start Threads
        p1.start();
        p2.start();

        // 5. Wait for both to finish
        p1.join();
        p2.join();

        System.out.println("\nFinal Inventory: " + inventory.getRoomAvailability().get("Single Room") + " left.");
        System.out.println("Simulation complete. System state remained consistent.");
    }
}