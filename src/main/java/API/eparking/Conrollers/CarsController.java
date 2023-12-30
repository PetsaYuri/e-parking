package API.eparking.Conrollers;

import API.eparking.DTO.CarDTO;
import API.eparking.Exceptions.Cars.CarWithThisNumbersAlreadyExistsException;
import API.eparking.Models.Cars;
import API.eparking.Repositories.UsersRepository;
import API.eparking.Services.CarsService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cars")
public class CarsController {

    private final CarsService carsService;

    @Autowired
    @Lazy
    public CarsController(CarsService carsService,UsersRepository usersRepository) {
        this.carsService = carsService;
    }

    @GetMapping("{id}")
    public ResponseEntity<CarDTO> getById(@PathVariable("id") Long id)   {
        try {
            Cars car = carsService.findCarById(id);
            return ResponseEntity.ok(new CarDTO(car.getColor(), car.getNumbers(), car.getType(), car.getImage(), car.getParking(), car.getUser()));
        }   catch (EntityNotFoundException ex)  {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity add(@RequestBody CarDTO car)   {
        try {
            Cars receivedCar = carsService.add(car);
            return ResponseEntity.ok(new CarDTO(receivedCar.getColor(), receivedCar.getNumbers(), receivedCar.getType(),
                    receivedCar.getImage(), receivedCar.getParking(), receivedCar.getUser()));
        }   catch (CarWithThisNumbersAlreadyExistsException ex) {
            return ResponseEntity.badRequest().body(ex);
        }   catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<CarDTO> update(@PathVariable("id") Long id, @RequestBody CarDTO updatedCar)   {
        try {
            Cars receivedCar = carsService.update(id, updatedCar);
            return ResponseEntity.ok(new CarDTO(receivedCar.getColor(), receivedCar.getNumbers(), receivedCar.getType(),
                    receivedCar.getImage(), receivedCar.getParking(), receivedCar.getUser()));
        }   catch (EntityNotFoundException ex)  {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Boolean> delete(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(carsService.delete(id));
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
