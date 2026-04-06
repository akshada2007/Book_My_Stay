import java.util.Map;

/**
 * MAIN CLASS - UseCase4RoomSearch
 * Combined version containing both the SearchService logic and the main execution.
 * @version 4.1
 */
public class UseCase4RoomSearch {

    /**
     * INNER CLASS - SearchService
     * Handles read-only access to inventory and room information.
     */
    static class SearchService {

        /**
         * Searches and displays rooms that have availability > 0.
         */
        public void searchAvailableRooms(RoomInventory inventory, Room[] rooms) {
            System.out.println("--- Available Rooms Search Results ---");
            boolean found = false;

            Map<String, Integer> availabilityMap = inventory.getRoomAvailability();

            for (Room room : rooms) {
                String className = room.getClass().getSimpleName();
                String searchKey = formatKey(className);

                int availableCount = availabilityMap.getOrDefault(searchKey, 0);

                // Only show rooms that are actually in stock
                if (availableCount > 0) {
                    System.out.println("\nRoom Type: " + searchKey);
                    room.displayRoomDetails();
                    System.out.println("Current Availability: " + availableCount);
                    found = true;
                }
            }

            if (!found) {
                System.out.println("No rooms are currently available.");
            }
            System.out.println("--------------------------------------\n");
        }

        private String formatKey(String className) {
            if (className.equals("SingleRoom")) return "Single Room";
            if (className.equals("DoubleRoom")) return "Double Room";
            if (className.equals("SuiteRoom")) return "Suite Room";
            return className;
        }
    }

    /**
     * Application entry point.
     */
    public static void main(String[] args) {
        System.out.println("Guest initiates a room search...\n");

        // 1. Setup State (Inventory)
        RoomInventory inventory = new RoomInventory();

        // 2. Setup Data (Room Objects)
        Room[] hotelRooms = {
                new SingleRoom(),
                new DoubleRoom(),
                new SuiteRoom()
        };

        // 3. Initialize the Inner Search Service
        SearchService searchService = new SearchService();

        // 4. Perform the Search
        searchService.searchAvailableRooms(inventory, hotelRooms);

        System.out.println("Search complete. System state remains unchanged.");
    }
}