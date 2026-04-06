import java.util.ArrayList;
import java.util.List;

/**
 * MAIN CLASS - UseCase8BookingHistoryReport
 * Illustrates historical tracking and reporting using a List data structure.
 * @version 8.1
 */
public class UseCase8BookingHistoryReport {

    /**
     * INNER CLASS - ConfirmedBooking
     * Represents a record in the system's history.
     */
    static class ConfirmedBooking {
        private String guestName;
        private String roomType;
        private String roomId;

        public ConfirmedBooking(String guestName, String roomType, String roomId) {
            this.guestName = guestName;
            this.roomType = roomType;
            this.roomId = roomId;
        }

        @Override
        public String toString() {
            return String.format("Guest: %-10s | Room Type: %-8s | Room ID: %s",
                    guestName, roomType, roomId);
        }
    }

    /**
     * INNER CLASS - BookingHistory
     * Acts as the "Persistence Layer" to store all confirmed bookings.
     */
    static class BookingHistory {
        private List<ConfirmedBooking> history = new ArrayList<>();

        public void addRecord(String name, String type, String id) {
            history.add(new ConfirmedBooking(name, type, id));
        }

        public List<ConfirmedBooking> getHistory() {
            return history;
        }
    }

    /**
     * INNER CLASS - ReportService
     * Logic for generating summaries from the history list.
     */
    static class ReportService {
        public void generateSummaryReport(List<ConfirmedBooking> records) {
            System.out.println("\n===== ADMIN BOOKING REPORT =====");
            System.out.println("Total Confirmed Bookings: " + records.size());
            System.out.println("---------------------------------");

            for (ConfirmedBooking record : records) {
                System.out.println(record);
            }
            System.out.println("=================================\n");
        }
    }

    public static void main(String[] args) {
        // 1. Initialize History and Reporting Services
        BookingHistory bookingHistory = new BookingHistory();
        ReportService reportService = new ReportService();

        System.out.println("System: Recording confirmed bookings into history...");

        // 2. Simulating successful allocations being sent to history
        // In a real flow, these would come from Use Case 6's AllocationService
        bookingHistory.addRecord("Abhi", "Single", "Single-101");
        bookingHistory.addRecord("Subha", "Double", "Double-103");
        bookingHistory.addRecord("Vanmathi", "Suite", "Suite-105");
        bookingHistory.addRecord("Akash", "Single", "Single-102");

        // 3. Admin requests the report
        reportService.generateSummaryReport(bookingHistory.getHistory());

        System.out.println("Report generation complete. Audit trail preserved.");
    }
}