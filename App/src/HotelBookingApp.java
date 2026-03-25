import java.util.HashMap;
import java.util.Map;

// --- Custom Exceptions ---
// Custom exceptions provide domain-specific context for errors
class BookingValidationException extends Exception {
    public BookingValidationException(String message) {
        super(message);
    }
}

class RoomUnavailableException extends Exception {
    public RoomUnavailableException(String message) {
        super(message);
    }
}

// --- Validation Logic ---
class BookingValidator {
    public static void validateInput(String guestName, String roomType) throws BookingValidationException {
        if (guestName == null || guestName.trim().isEmpty()) {
            throw new BookingValidationException("Guest name cannot be empty.");
        }
        if (roomType == null || roomType.trim().isEmpty()) {
            throw new BookingValidationException("Room type must be specified.");
        }
    }
}

// --- System State Guarding ---
class InventoryManager {
    private final Map<String, Integer> inventory = new HashMap<>();

    public InventoryManager() {
        inventory.put("SUITE", 2);
        inventory.put("SINGLE", 5);
    }

    public void checkAndDeduct(String roomType) throws RoomUnavailableException {
        String key = roomType.toUpperCase();

        // Validation: Check if room type exists
        if (!inventory.containsKey(key)) {
            throw new RoomUnavailableException("Room type '" + roomType + "' does not exist in our system.");
        }

        // Validation: Guarding against negative inventory
        int currentStock = inventory.get(key);
        if (currentStock <= 0) {
            throw new RoomUnavailableException("No " + key + " rooms currently available.");
        }

        // If all guards pass, update state
        inventory.replace(key, currentStock - 1);
        System.out.println("Inventory updated: " + key + " rooms left: " + (currentStock - 1));
    }
}

// --- Main Application ---
public class HotelBookingApp {
    private static final InventoryManager inventory = new InventoryManager();

    public static void main(String[] args) {
        // Test Case 1: Invalid Input (Empty Name)
        processBooking("", "Suite");

        // Test Case 2: Invalid Room Type
        processBooking("Alice", "Penthouse");

        // Test Case 3: Successful Bookings until exhaustion
        processBooking("Bob", "Suite");
        processBooking("Charlie", "Suite");

        // Test Case 4: Room Exhaustion (Fail-Fast)
        processBooking("Dave", "Suite");
    }

    public static void processBooking(String guestName, String roomType) {
        try {
            System.out.println("\nAttempting booking for: " + (guestName.isEmpty() ? "[Empty]" : guestName));

            // 1. Input Validation
            BookingValidator.validateInput(guestName, roomType);

            // 2. State Validation & Inventory Update
            inventory.checkAndDeduct(roomType);

            System.out.println("SUCCESS: Booking confirmed for " + guestName);

        } catch (BookingValidationException | RoomUnavailableException e) {
            // 3. Graceful Failure Handling
            System.err.println("REJECTED: " + e.getMessage());
        } catch (Exception e) {
            // Catch-all for unexpected system errors to keep the app running
            System.err.println("CRITICAL ERROR: An unexpected issue occurred.");
        } finally {
            System.out.println("System Status: Ready for next request.");
        }
    }
}