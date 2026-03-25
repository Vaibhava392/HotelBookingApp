import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

// --- Shared Resource: The Booking System ---
class ThreadSafeHotel {
    private int availableRooms = 2; // Limited supply to test contention
    private final Queue<String> bookingQueue = new LinkedList<>();

    // 'synchronized' ensures only one thread enters this method at a time
    public synchronized void processBooking(String guestName) {
        System.out.println("[Thread " + Thread.currentThread().getId() + "] Processing: " + guestName);

        // Critical Section: Checking and updating inventory
        if (availableRooms > 0) {
            // Simulate processing time to increase the chance of a race condition if not synchronized
            try { Thread.sleep(100); } catch (InterruptedException e) { }

            availableRooms--;
            bookingQueue.add(guestName);
            System.out.println("SUCCESS: " + guestName + " secured a room. Rooms left: " + availableRooms);
        } else {
            System.err.println("REJECTED: No rooms left for " + guestName);
        }
    }

    public int getFinalRoomCount() {
        return availableRooms;
    }
}

// --- Main Application: Simulating Concurrent Users ---
public class HotelBookingApp {
    public static void main(String[] args) throws InterruptedException {
        ThreadSafeHotel hotel = new ThreadSafeHotel();

        // Create a pool of threads to simulate multiple users acting at once
        ExecutorService executor = Executors.newFixedThreadPool(5);

        String[] guests = {"Alice", "Bob", "Charlie", "Dave", "Eve"};

        System.out.println("--- Starting Concurrent Booking Simulation ---");
        System.out.println("Initial Rooms: 2 | Total Guests: 5\n");

        for (String guest : guests) {
            executor.submit(() -> hotel.processBooking(guest));
        }

        // Shut down the executor and wait for all threads to finish
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        System.out.println("\n--- Simulation Complete ---");
        System.out.println("Final Inventory Count: " + hotel.getFinalRoomCount());

        if (hotel.getFinalRoomCount() < 0) {
            System.err.println("CRITICAL FAILURE: Race condition detected (Negative Inventory)!");
        } else {
            System.out.println("SYSTEM INTEGRITY: Inventory consistent.");
        }
    }
}