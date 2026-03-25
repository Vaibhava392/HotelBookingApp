import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

// --- Models ---
class Reservation {
    private final String bookingId;
    private final String guestName;
    private final String roomType;
    private final double price;

    public Reservation(String bookingId, String guestName, String roomType, double price) {
        this.bookingId = bookingId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.price = price;
    }

    public double getPrice() { return price; }

    @Override
    public String toString() {
        return String.format("[%s] Guest: %-10s | Room: %-10s | Paid: $%.2f",
                bookingId, guestName, roomType, price);
    }
}

// --- Historical Tracking ---
class BookingHistory {
    // List preserves insertion order, serving as a chronological audit trail
    private final List<Reservation> history = new ArrayList<>();

    public void record(Reservation reservation) {
        history.add(reservation);
    }

    public List<Reservation> getAllRecords() {
        // Return unmodifiable list to ensure reporting doesn't modify stored data
        return Collections.unmodifiableList(history);
    }
}

// --- Reporting Service ---
class BookingReportService {
    public void generateSummary(List<Reservation> records) {
        System.out.println("\n--- Booking Operational Report ---");
        System.out.println("Total Bookings: " + records.size());

        double totalRevenue = records.stream()
                .mapToDouble(Reservation::getPrice)
                .sum();

        System.out.printf("Total Revenue: $%.2f%n", totalRevenue);
        System.out.println("----------------------------------");
    }

    public void showDetailedAudit(List<Reservation> records) {
        System.out.println("\n--- Chronological Audit Trail ---");
        records.forEach(System.out::println);
    }
}

// --- Main Application ---
public class HotelBookingApp {
    public static void main(String[] args) {
        // Initialize components
        BookingHistory history = new BookingHistory();
        BookingReportService reportService = new BookingReportService();

        // 1. Simulate Bookings (Flow: Confirmed -> Added to History)
        System.out.println("Processing bookings...");
        history.record(new Reservation("BK-101", "Alice", "Suite", 250.00));
        history.record(new Reservation("BK-102", "Bob", "Single", 100.00));
        history.record(new Reservation("BK-103", "Charlie", "Double", 150.00));

        // 2. Admin Request: Operational Visibility
        List<Reservation> allRecords = history.getAllRecords();

        // 3. Generate Reports
        reportService.showDetailedAudit(allRecords);
        reportService.generateSummary(allRecords);
    }
}