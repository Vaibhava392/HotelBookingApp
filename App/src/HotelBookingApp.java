/**
 * Hotel Booking Management System - Use Case 2
 * This version introduces Abstract Classes, Inheritance, and Encapsulation.
 * * @author [Your Name]
 * @version 1.1
 */

// --- Domain Layer: Abstraction & Inheritance ---

/**
 * Abstract class representing the general concept of a Hotel Room.
 * It cannot be instantiated directly.
 */
abstract class Room {
    private String roomType;
    private double pricePerNight;

    public Room(String roomType, double pricePerNight) {
        this.roomType = roomType;
        this.pricePerNight = pricePerNight;
    }

    // Encapsulation: Accessors for private fields
    public String getRoomType() { return roomType; }
    public double getPricePerNight() { return pricePerNight; }

    /**
     * Abstract method to be implemented by specific room types
     * to describe their unique features.
     */
    public abstract String getRoomFeatures();

    @Override
    public String toString() {
        return String.format("[%s] - Price: $%.2f | Features: %s",
                roomType, pricePerNight, getRoomFeatures());
    }
}

class SingleRoom extends Room {
    public SingleRoom() { super("Single Room", 100.0); }

    @Override
    public String getRoomFeatures() {
        return "1 Twin Bed, High-speed Wi-Fi, Workspace";
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() { super("Double Room", 180.0); }

    @Override
    public String getRoomFeatures() {
        return "2 Queen Beds, Mini-fridge, City View";
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() { super("Luxury Suite", 350.0); }

    @Override
    public String getRoomFeatures() {
        return "King Bed, Living Area, Private Balcony, Complimentary Breakfast";
    }
}

// --- Application Entry Point ---

public class HotelBookingApp {

    // Static Availability Representation (Intentionally limited)
    // This demonstrates the "Pre-Data Structure" state of the app.
    private static int singleRoomAvailability = 5;
    private static int doubleRoomAvailability = 3;
    private static int suiteRoomAvailability = 2;

    public static void main(String[] args) {
        System.out.println("=== Hotel Booking Management System v1.1 ===");
        System.out.println("Initializing Room Catalog...\n");

        // Polymorphism: Using the 'Room' reference for different implementations
        Room single = new SingleRoom();
        Room dbl = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Displaying Catalog and Availability
        displayRoomStatus(single, singleRoomAvailability);
        displayRoomStatus(dbl, doubleRoomAvailability);
        displayRoomStatus(suite, suiteRoomAvailability);

        System.out.println("\nSystem initialized. Catalog display complete.");
    }

    /**
     * Helper method to print room details and current stock.
     */
    private static void displayRoomStatus(Room room, int count) {
        System.out.println(room.toString());
        System.out.println("   Current Availability: " + count + " rooms remaining");
        System.out.println("--------------------------------------------------");
    }
}
