package API.eparking.Conrollers;

import API.eparking.Exceptions.Parking.BusyParkingLotException;
import API.eparking.Exceptions.Users.UserNotFoundException;
import API.eparking.Models.Booking;
import API.eparking.Models.Parking;
import API.eparking.Models.Users;
import API.eparking.Services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/booking")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public List<Booking> getAll(@RequestParam(value = "id_user", required = false) Users user)   {
        return bookingService.getAll(user);
    }

    @GetMapping("{id}")
    public Booking getOne(@PathVariable("id") Booking booking)  {
        return booking;
    }

    @PostMapping
    public ResponseEntity add(@RequestBody Booking booking, @RequestParam("parking_lot") Parking parkingLot)    {
        try {
            Booking newBooking = bookingService.addBooking(booking, parkingLot);
            return ResponseEntity.ok().body(newBooking);
        }   catch (UserNotFoundException ex)    {
            return ResponseEntity.status(400).body("User not found");
        }   catch (BusyParkingLotException ex)  {
            return ResponseEntity.status(400).body("This parking lot is not available");
        }
    }

    @DeleteMapping("/{id}")
    public Boolean cancel(@PathVariable("id") Booking booking)    {
        return bookingService.cancelBooking(booking);
    }
}
