import java.util.*;

/**
 * MAIN CLASS - UseCase9ErrorHandlingValidation
 * Demonstrates Fail-Fast design and Custom Exception handling.
 * @version 9.1
 */
public class UseCase9ErrorHandlingValidation {

    /**
     * CUSTOM EXCEPTION - InvalidBookingException
     * Specific exception for domain-related booking errors.
     */
    static class InvalidBookingException extends Exception {
        public InvalidBookingException(String message) {
            super(message);
        }
    }

    /**
     * INNER CLASS - BookingValidator
     * Logic to guard the system state from invalid inputs.
     */
    static class BookingValidator {
        /**
         * Validates the booking request against system constraints.
         * @throws InvalidBookingException if room type is unknown or unavailable.
         */
        public void validateRequest(String roomType, RoomInventory inventory)
                throws InvalidBookingException {

            // 1. Validation: Check if room type exists (Case Sensitive)
            String inventoryKey = roomType + " Room";
            if (!inventory.getRoomAvailability().containsKey(inventoryKey)) {
                throw new InvalidBookingException("Error: Room type '" + roomType + "' is invalid.");
            }

            // 2. Validation: Check for room availability
            int count = inventory.getRoomAvailability().get(inventoryKey);
            if (count <= 0) {
                throw new InvalidBookingException("Error: No availability for '" + roomType + "'.");
            }
        }
    }

    public static void main(String[] args) {
        // Initialize System
        RoomInventory inventory = new RoomInventory();
        BookingValidator validator = new BookingValidator();

        System.out.println("--- Booking Validation & Error Handling ---\n");

        // Scenario 1: Valid Room Type
        try {
            System.out.println("Attempting to validate 'Single'...");
            validator.validateRequest("Single", inventory);
            System.out.println("Success: Request is valid.\n");
        } catch (InvalidBookingException e) {
            System.out.println(e.getMessage());
        }

        // Scenario 2: Invalid Room Type (Case Sensitivity Check)
        try {
            System.out.println("Attempting to validate 'single' (lowercase)...");
            validator.validateRequest("single", inventory);
        } catch (InvalidBookingException e) {
            System.err.println(e.getMessage()); // Printed to error stream
        }

        // Scenario 3: Unknown Room Type
        try {
            System.out.println("\nAttempting to validate 'Penthouse'...");
            validator.validateRequest("Penthouse", inventory);
        } catch (InvalidBookingException e) {
            System.err.println(e.getMessage());
        }

        System.out.println("\nSystem remains stable and continues running.");
    }
}