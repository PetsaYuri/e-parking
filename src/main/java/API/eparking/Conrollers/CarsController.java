package API.eparking.Conrollers;

import API.eparking.DTO.CarDTO;
import API.eparking.Models.Cars;
import API.eparking.Repositories.UsersRepository;
import API.eparking.Services.CarsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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
    public CarDTO getById(@PathVariable("id") Cars car)   {
        return new CarDTO(car.getColor(), car.getNumbers(), car.getType(), car.getImage(), car.getParking(), car.getUser());
    }

    @PostMapping
    public CarDTO add(@RequestBody CarDTO car)   {
        return carsService.add(car);
    }

    @PutMapping("{id}")
    public CarDTO update(@PathVariable("id") Cars car, @RequestBody CarDTO updatedCar)   {
        return carsService.update(car, updatedCar);
    }

    @DeleteMapping("{id}")
    public Boolean delete(@PathVariable("id") Cars car) {
        return carsService.delete(car);
    }
}
