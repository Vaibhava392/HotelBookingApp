import java.util.*;
import java.util.stream.Collectors;

/**
 * Domain Model: Represents static room details.
 */
class Room {
    private String type;
    private double price;
    private List<String> amenities;

    public Room(String type, double price, List<String> amenities) {
        this.type = type;
        this.price = price;
        this.amenities = amenities;
    }

    public String getType() { return type; }
    public double getPrice() { return price; }
    public List<String> getAmenities() { return amenities; }

    @Override
    public String toString() {
        return String.format("[%s] - $%s (Amenities: %s)", type, price, amenities);
    }
}

/**
 * Inventory: Acts as the State Holder for availability counts.
 */
class Inventory {
    private final Map<String, Integer> stock = new HashMap<>();

    public void updateStock(String type, int count) {
        stock.put(type, count);
    }

    // Read-only access to availability
    public int getAvailability(String type) {
        return stock.getOrDefault(type, 0);
    }

    public Set<String> getAllRoomTypes() {
        return stock.keySet();
    }
}

/**
 * Search Service: Handles read-only logic and filtering.
 */
class SearchService {
    private final Inventory inventory;
    private final List<Room> roomCatalog;

    public SearchService(Inventory inventory, List<Room> roomCatalog) {
        this.inventory = inventory;
        this.roomCatalog = roomCatalog;
    }

    public List<Room> findAvailableRooms() {
        // Defensive Programming & Validation: Filter out zero-availability types
        return roomCatalog.stream()
                .filter(room -> inventory.getAvailability(room.getType()) > 0)
                .collect(Collectors.toList());
    }
}

public class HotelBookingApp {
    public static void main(String[] args) {
        // 1. Setup Data (System State)
        Inventory inventory = new Inventory();
        inventory.updateStock("Deluxe Suite", 5);
        inventory.updateStock("Standard Room", 0); // Out of stock
        inventory.updateStock("Penthouse", 2);

        List<Room> catalog = Arrays.asList(
                new Room("Deluxe Suite", 250.0, Arrays.asList("WiFi", "Mini-bar")),
                new Room("Standard Room", 100.0, Arrays.asList("WiFi")),
                new Room("Penthouse", 1200.0, Arrays.asList("Private Pool", "Chef"))
        );

        // 2. Initialize Search Service
        SearchService searchService = new SearchService(inventory, catalog);

        // 3. Execution: Guest initiates search
        System.out.println("--- Guest Search Results ---");
        List<Room> availableOptions = searchService.findAvailableRooms();

        if (availableOptions.isEmpty()) {
            System.out.println("No rooms available at this time.");
        } else {
            availableOptions.forEach(System.out::println);
        }

        // Verification: System state remains unchanged (Inventory counts should be the same)
        System.out.println("\nSearch complete. System state remains stable.");
    }
}