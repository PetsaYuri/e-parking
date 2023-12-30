package API.eparking.DTO;

import API.eparking.Models.Booking;
import API.eparking.Models.Cars;

import java.sql.Timestamp;
import java.util.List;

public record ParkingDTO(int price_per_day, int price_per_hour, int busy_days, int busy_hours, boolean is_available,
                         boolean is_booking, Timestamp busy_start, Timestamp busy_end, String promocode, Cars car, List<Booking> bookingLot) {
}
