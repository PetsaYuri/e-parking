package API.eparking.Repositories;

import API.eparking.Models.Parking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParkingRepository extends JpaRepository<Parking, Long> {

    public List<Parking> findByIsAvailable(Boolean isAvailable);
}
