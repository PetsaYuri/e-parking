package API.eparking.Repositories;

import API.eparking.Models.Cars;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarsRepository extends JpaRepository<Cars, Long> {

    Cars findByNumbers(String numbers);
}
