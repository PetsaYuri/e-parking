package API.eparking.Repositories;

import API.eparking.Models.Booking;
import API.eparking.Models.PromoCodes;
import API.eparking.Models.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    public List<Booking> findByUser(Users user);
}
