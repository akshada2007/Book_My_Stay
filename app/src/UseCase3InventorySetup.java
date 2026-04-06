/**
 * MAIN CLASS - UseCase3InventorySetup
 * Demonstrates how room availability is managed using a centralized inventory.
 * @version 3.1
 */
public class UseCase3InventorySetup {

    public static void main(String[] args) {
        System.out.println("Hotel Room Inventory Status\n");

        // Initialize the centralized inventory
        RoomInventory inventory = new RoomInventory();

        // Create Room objects to get characteristics (Beds, Size, Price)
        Room single = new SingleRoom();
        Room doubleRm = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Display Single Room Status
        System.out.println("Single Room:");
        single.displayRoomDetails();
        System.out.println("Available Rooms: " + inventory.getRoomAvailability().get("Single Room") + "\n");

        // Display Double Room Status
        System.out.println("Double Room:");
        doubleRm.displayRoomDetails();
        System.out.println("Available Rooms: " + inventory.getRoomAvailability().get("Double Room") + "\n");

        // Display Suite Room Status
        System.out.println("Suite Room:");
        suite.displayRoomDetails();
        System.out.println("Available Rooms: " + inventory.getRoomAvailability().get("Suite Room"));
    }
}