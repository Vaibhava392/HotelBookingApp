import java.io.*;
import java.util.Properties;

// --- Persistence Service ---
class PersistenceService {
    private final String STORAGE_FILE = "hotel_state.properties";

    // Serialization: Saving in-memory state to a file
    public void saveState(int inventory, int totalBookings) {
        try (OutputStream output = new FileOutputStream(STORAGE_FILE)) {
            Properties prop = new Properties();
            prop.setProperty("inventory", String.valueOf(inventory));
            prop.setProperty("totalBookings", String.valueOf(totalBookings));

            prop.store(output, "System State Snapshot");
            System.out.println(">>> STATE PERSISTED: Inventory and Bookings saved to disk.");
        } catch (IOException io) {
            System.err.println("Error saving state: " + io.getMessage());
        }
    }

    // Deserialization: Restoring state from a file
    public int[] loadState() {
        File file = new File(STORAGE_FILE);
        if (!file.exists()) {
            System.out.println(">>> NO PERSISTED DATA FOUND: Starting with fresh state.");
            return new int[]{5, 0}; // Default: 5 rooms, 0 bookings
        }

        try (InputStream input = new FileInputStream(STORAGE_FILE)) {
            Properties prop = new Properties();
            prop.load(input);

            int inventory = Integer.parseInt(prop.getProperty("inventory"));
            int totalBookings = Integer.parseInt(prop.getProperty("totalBookings"));

            System.out.println(">>> STATE RECOVERED: Restored from last session.");
            return new int[]{inventory, totalBookings};
        } catch (IOException | NumberFormatException e) {
            System.err.println("RECOVERY FAILED: Corrupted file. Using defaults.");
            return new int[]{5, 0};
        }
    }
}

// --- Stateful Application ---
public class HotelBookingApp {
    private static int roomInventory;
    private static int bookingCount;
    private static final PersistenceService persistence = new PersistenceService();

    public static void main(String[] args) {
        // 1. System Startup: Recover State
        int[] recoveredState = persistence.loadState();
        roomInventory = recoveredState[0];
        bookingCount = recoveredState[1];

        displayStatus("Startup Status");

        // 2. Simulate Operations
        System.out.println("\n--- Processing New Booking ---");
        if (roomInventory > 0) {
            roomInventory--;
            bookingCount++;
            System.out.println("Booking successful!");
        }

        // 3. System Shutdown: Persist State
        displayStatus("Pre-Shutdown Status");
        persistence.saveState(roomInventory, bookingCount);
        System.out.println("Application Closed safely.");
    }

    private static void displayStatus(String label) {
        System.out.println("[" + label + "] Rooms Available: " + roomInventory + " | Total Bookings: " + bookingCount);
    }
}