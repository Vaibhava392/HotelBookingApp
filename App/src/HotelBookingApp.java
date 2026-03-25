import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;

/**
 * Reservation: Represents the guest's intent to book.
 * Contains only the data necessary to identify the request.
 */
class ReservationRequest {
    private final String requestId;
    private final String guestName;
    private final String roomType;

    public ReservationRequest(String guestName, String roomType) {
        this.requestId = UUID.randomUUID().toString().substring(0, 8);
        this.guestName = guestName;
        this.roomType = roomType;
    }

    @Override
    public String toString() {
        return String.format("Request ID: %s | Guest: %s | Room: %s", requestId, guestName, roomType);
    }
}

/**
 * Booking Request Queue: Manages the intake and order of requests.
 */
class BookingQueue {
    // Using a LinkedList as a Queue to maintain FIFO (First-In-First-Out)
    private final Queue<ReservationRequest> queue = new LinkedList<>();

    // Adds a request to the end of the line
    public void enqueue(ReservationRequest request) {
        System.out.println("Enqueuing: " + request);
        queue.add(request);
    }

    // Prepares the next request for the allocation system (but doesn't process it yet)
    public ReservationRequest peekNext() {
        return queue.peek();
    }

    public int getQueueSize() {
        return queue.size();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

public class HotelBookingApp {
    public static void main(String[] args) {
        BookingQueue bookingQueue = new BookingQueue();

        // 1. Simulation: Guests submitting requests rapidly
        System.out.println("--- Intake Stage: Receiving Requests ---");
        bookingQueue.enqueue(new ReservationRequest("Alice", "Deluxe Suite"));
        bookingQueue.enqueue(new ReservationRequest("Bob", "Penthouse"));
        bookingQueue.enqueue(new ReservationRequest("Charlie", "Deluxe Suite"));

        // 2. Verification of Requirements
        System.out.println("\n--- Queue Status ---");
        System.out.println("Total requests waiting: " + bookingQueue.getQueueSize());

        // 3. Demonstrating Order Preservation (FIFO)
        System.out.println("Next request to be processed: " + bookingQueue.peekNext());

        System.out.println("\nRequirement Check: No inventory has been mutated. Requests are simply ordered.");
    }
}