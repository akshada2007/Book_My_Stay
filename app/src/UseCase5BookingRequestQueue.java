import java.util.LinkedList;
import java.util.Queue;

/**
 * MAIN CLASS - UseCase5BookingRequestQueue
 * Combines the Reservation data model and Queue processing logic.
 * This implementation illustrates how requests are handled in a 
 * "First-Come-First-Served" manner using the Queue data structure.
 * @version 5.1
 */
public class UseCase5BookingRequestQueue {

    /**
     * INNER CLASS - Reservation
     * Represents a guest's intent to book a room.
     */
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
     * Application entry point.
     */
    public static void main(String[] args) {
        // 1. Initialize the Booking Request Queue
        // We use LinkedList because it implements the Queue interface in Java
        Queue<Reservation> bookingQueue = new LinkedList<>();

        System.out.println("Booking Request Queue");
        System.out.println("----------------------");

        // 2. Simulating incoming booking requests (Adding to the tail of the Queue)
        bookingQueue.add(new Reservation("Abhi", "Single"));
        bookingQueue.add(new Reservation("Subha", "Double"));
        bookingQueue.add(new Reservation("Vanmathi", "Suite"));

        // 3. Process the requests (Retrieving from the head of the Queue)
        // poll() returns null if the queue is empty
        while (!bookingQueue.isEmpty()) {
            Reservation request = bookingQueue.poll();
            System.out.println("Processing booking for Guest: " + request.getGuestName() +
                    ", Room Type: " + request.getRoomType());
        }
    }
}