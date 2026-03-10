import java.util.HashMap;
import java.util.Map;

/**
 * Hotel Booking Management System - Use Case 3
 * This version introduces the HashMap to centralize room inventory.
 * * @author [Your Name]
 * @version 1.2
 */

// --- Domain Layer (from Use Case 2) ---

abstract class Room {
    private String roomType;
    private double pricePerNight;

    public Room(String roomType, double pricePerNight) {
        this.roomType = roomType;
        this.pricePerNight = pricePerNight;
    }

    public String getRoomType() { return roomType; }
    public abstract String getRoomFeatures();

    @Override
    public String toString() {
        return String.format("[%s] - Features: %s", roomType, getRoomFeatures());
    }
}

class SingleRoom extends Room {
    public SingleRoom() { super("Single", 100.0); }
    public String getRoomFeatures() { return "1 Twin Bed, Workspace"; }
}

class DoubleRoom extends Room {
    public DoubleRoom() { super("Double", 180.0); }
    public String getRoomFeatures() { return "2 Queen Beds, City View"; }
}

class SuiteRoom extends Room {
    public SuiteRoom() { super("Suite", 350.0); }
    public String getRoomFeatures() { return "King Bed, Private Balcony"; }
}

// --- New Inventory Layer ---

/**
 * RoomInventory encapsulates the HashMap used to track availability.
 * It provides a "Single Source of Truth" for the system state.
 */
class RoomInventory {
    // Key: Room Type Name (String), Value: Available Count (Integer)
    private Map<String, Integer> inventory;

    public RoomInventory() {
        this.inventory = new HashMap<>();
    }

    /**
     * Registers or updates the count for a specific room type.
     * Demonstrates O(1) average time complexity for insertion/updates.
     */
    public void updateAvailability(String roomType, int count) {
        inventory.put(roomType, count);
    }

    /**
     * Retrieves current availability. Returns 0 if room type is not found.
     */
    public int getAvailableCount(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    /**
     * Prints the entire state of the inventory.
     */
    public void displayInventory() {
        System.out.println("Current Inventory Status:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(" - " + entry.getKey() + ": " + entry.getValue() + " rooms available");
        }
    }
}

// --- Application Entry Point ---

public class HotelBookingApp {
    public static void main(String[] args) {
        System.out.println("=== Hotel Booking Management System v1.2 ===\n");

        // 1. Initialize Inventory Component
        RoomInventory inventoryManager = new RoomInventory();

        // 2. Register Room Types (Centralizing the state)
        inventoryManager.updateAvailability("Single", 10);
        inventoryManager.updateAvailability("Double", 5);
        inventoryManager.updateAvailability("Suite", 2);

        // 3. Create Domain Objects
        Room s = new SingleRoom();
        Room d = new DoubleRoom();
        Room ste = new SuiteRoom();

        // 4. Display System State
        System.out.println("Room Catalog Details:");
        System.out.println(s + " | Availability: " + inventoryManager.getAvailableCount("Single"));
        System.out.println(d + " | Availability: " + inventoryManager.getAvailableCount("Double"));
        System.out.println(ste + " | Availability: " + inventoryManager.getAvailableCount("Suite"));

        System.out.println("\n--------------------------------------------------");
        inventoryManager.displayInventory();
        System.out.println("--------------------------------------------------");
    }
}

