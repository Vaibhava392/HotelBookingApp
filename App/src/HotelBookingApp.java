import java.util.*;

// --- Custom Exception ---
class CancellationException extends Exception {
    public CancellationException(String message) {
        super(message);
    }
}

// --- Rollback & Inventory Management ---
class ReservationSystem {
    private int availableRooms = 5;
    // Stack tracks Room IDs for LIFO rollback behavior
    private final Stack<String> allocatedRoomIds = new Stack<>();
    // Map stores active bookings for validation
    private final Map<String, String> activeBookings = new HashMap<>();

    public void confirmBooking(String bookingId, String roomId) {
        activeBookings.put(bookingId, roomId);
        allocatedRoomIds.push(roomId);
        availableRooms--;
        System.out.println("Confirmed: " + bookingId + " assigned to Room " + roomId);
    }

    public void cancelBooking(String bookingId) throws CancellationException {
        System.out.println("\nProcessing cancellation for: " + bookingId);

        // 1. Validation: Does the reservation exist?
        if (!activeBookings.containsKey(bookingId)) {
            throw new CancellationException("Cancellation Failed: Booking ID " + bookingId + " not found.");
        }

        // 2. LIFO Rollback: Retrieve the Room ID
        String roomIdToRelease = activeBookings.remove(bookingId);

        // In a real LIFO rollback, we'd ensure the room being cancelled
        // matches the top of the stack if we were doing a pure 'Undo'
        allocatedRoomIds.remove(roomIdToRelease);

        // 3. Inventory Restoration
        availableRooms++;

        System.out.println("SUCCESS: Room " + roomIdToRelease + " is now vacant.");
        System.out.println("System State: " + availableRooms + " rooms available.");
    }
}

// --- Main Application ---
public class HotelBookingApp {
    public static void main(String[] args) {
        ReservationSystem system = new ReservationSystem();

        // Setup: Confirm some bookings
        system.confirmBooking("BK-999", "101A");
        system.confirmBooking("BK-888", "102B");

        try {
            // Test Case 1: Valid Cancellation
            system.cancelBooking("BK-888");

            // Test Case 2: Duplicate Cancellation (Validation Check)
            system.cancelBooking("BK-888");

        } catch (CancellationException e) {
            System.err.println("ERROR: " + e.getMessage());
        }

        try {
            // Test Case 3: Non-existent Booking
            system.cancelBooking("BK-000");
        } catch (CancellationException e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }
}