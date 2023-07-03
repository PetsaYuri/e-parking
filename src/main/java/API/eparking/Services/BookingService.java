package API.eparking.Services;

import API.eparking.Exceptions.Parking.BusyParkingLotException;
import API.eparking.Exceptions.Users.UserNotFoundException;
import API.eparking.Models.Booking;
import API.eparking.Models.Parking;
import API.eparking.Models.Users;
import API.eparking.Repositories.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

@Service
public class BookingService {

    @Value(("${booking.price}"))
    private int price;

    private final ParkingService parkingService;
    private final UsersService usersService;
    private final BookingRepository bookingRepository;

    @Autowired
    public BookingService(@Lazy ParkingService parkingService, UsersService usersService, BookingRepository bookingRepository) {
        this.parkingService = parkingService;
        this.usersService = usersService;
        this.bookingRepository = bookingRepository;
    }

    public List<Booking> getAll(Users user)   {
        if (user != null)   {
            return bookingRepository.findByUser(user);
        }
        return bookingRepository.findAll();
    }

    public Booking addBooking(Booking bookingLot, Parking parkingLot) throws UserNotFoundException, BusyParkingLotException {
        if (!parkingLot.getIsAvailable())   {
            if (bookingLot.getStart_booking().before(parkingLot.getBusyEnd()))  {
                throw new BusyParkingLotException();
            }
        }

        if (parkingLot.getIsBooking())  {
            for (Booking booking : parkingLot.getBookingLot())  {
                if(bookingLot.getStart_booking().before(booking.getEnd_booking()) && bookingLot.getEnd_booking().after(booking.getStart_booking()))  {
                    throw new BusyParkingLotException();
                }
            }
        }

        Users user = usersService.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if (user != null) {
            Booking booking = new Booking(bookingLot.getStart_booking(), bookingLot.getEnd_booking(), bookingLot.getDays(), bookingLot.getHours(), price, user, parkingLot);
            Booking newBooking = bookingRepository.save(booking);
            parkingService.bookingParkingLot(newBooking, parkingLot);
            return newBooking;
        }   else {
            throw new UserNotFoundException();
        }
    }

    public Boolean cancelBooking(Booking booking)   {
        if (parkingService.deleteBooking(booking.getParkingLot()))  {
            bookingRepository.delete(booking);
            return true;
        }
        return false;
    }

    public void deleteParkingLot(List<Booking> listBookings)   {
        for (Booking booking : listBookings)    {
            booking.setParkingLot(null);
            bookingRepository.save(booking);
        }
    }
}
