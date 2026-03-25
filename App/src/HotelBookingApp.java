import java.util.*;

/**
 * Add-On Service: Represents an individual optional offering.
 */
class AddOnService {
    private final String name;
    private final double price;

    public AddOnService(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() { return name; }
    public double getPrice() { return price; }

    @Override
    public String toString() {
        return String.format("%s ($%.2f)", name, price);
    }
}

/**
 * Add-On Service Manager: Manages the One-to-Many relationship
 * between Reservation IDs and their selected services.
 */
class AddOnManager {
    // Map<ReservationID, List<Service>> allows efficient lookup and multiple items.
    private final Map<String, List<AddOnService>> reservationAddOns = new HashMap<>();

    public void addServiceToReservation(String reservationId, AddOnService service) {
        reservationAddOns
                .computeIfAbsent(reservationId, k -> new ArrayList<>())
                .add(service);
        System.out.println("Service Added: " + service.getName() + " to Reservation " + reservationId);
    }

    /**
     * Cost Aggregation: Calculates total additional cost without touching core pricing.
     */
    public double calculateTotalAddOnCost(String reservationId) {
        List<AddOnService> services = reservationAddOns.getOrDefault(reservationId, Collections.emptyList());
        return services.stream()
                .mapToDouble(AddOnService::getPrice)
                .sum();
    }

    public List<AddOnService> getServicesForReservation(String reservationId) {
        return reservationAddOns.getOrDefault(reservationId, Collections.emptyList());
    }
}

public class HotelBookingApp {
    public static void main(String[] args) {
        AddOnManager addOnManager = new AddOnManager();
        String myReservationId = "DEL-452"; // Assume this came from BookingService

        // Guest selects services
        addOnManager.addServiceToReservation(myReservationId, new AddOnService("Airport Shuttle", 45.0));
        addOnManager.addServiceToReservation(myReservationId, new AddOnService("Breakfast Buffet", 25.0));

        // Calculate and display
        double extraCost = addOnManager.calculateTotalAddOnCost(myReservationId);
        System.out.println("Total Extra Charges: $" + extraCost);
        System.out.println("All Add-Ons: " + addOnManager.getServicesForReservation(myReservationId));
    }
}