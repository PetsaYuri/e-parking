package API.eparking.Services;

import API.eparking.DTO.CarDTO;
import API.eparking.Models.Cars;
import API.eparking.Models.Parking;
import API.eparking.Models.Users;
import API.eparking.Repositories.CarsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CarsService {

    private final CarsRepository carsRepository;
    private final UsersService usersService;
    private final TransactionsService transactionsService;

    @Autowired
    public CarsService(CarsRepository carsRepository, UsersService usersService, TransactionsService transactionsService)   {
        this.carsRepository = carsRepository;
        this.usersService = usersService;
        this.transactionsService = transactionsService;
    }

    public Cars findCarById(Long id)    {
        return carsRepository.getReferenceById(id);
    }

    public CarDTO add(CarDTO car)   {
        if (carsRepository.findByNumbers(car.numbers()) == null) {
            Users user = usersService.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
            Cars newCar = new Cars(car.numbers(), car.color(), car.type(), user);
            carsRepository.save(newCar);
            usersService.setCarToUser(user, newCar);
            return new CarDTO(newCar.getColor(), newCar.getNumbers(), newCar.getType(), newCar.getImage(), newCar.getParking(), newCar.getUser());
        }
        return null;
    }

    public CarDTO update(Cars car, CarDTO updatedCar){
        if (updatedCar.numbers() != null && !updatedCar.numbers().equals(car.getNumbers())) {
            car.setNumbers(updatedCar.numbers());
        }

        if (updatedCar.color() != null && !updatedCar.color().equals(car.getColor()))   {
            car.setColor(updatedCar.color());
        }

        if (Character.isLetter(updatedCar.type()) && updatedCar.type() != car.getType())   {
            car.setType(updatedCar.type());
        }

        carsRepository.save(car);
        return new CarDTO(car.getColor(), car.getNumbers(), car.getType(), car.getImage(), car.getParking(), car.getUser());
    }

    public Boolean delete(Cars car) {
        if (carsRepository.existsById(car.getId())) {
            if(car.getUser() != null)   {
                transactionsService.deleteCar(car);
                usersService.deleteTheUsersCar(car.getUser(), car);
                car.setUser(null);
                carsRepository.save(car);
            }
            carsRepository.delete(car);
            return true;
        }
        return false;
    }

    public Cars setParkingLotToCar(Cars car, Parking parking)   {
        car.setParking(parking);
        return carsRepository.save(car);
    }

    public Cars deleteParkingLot(Cars car) {
        car.setParking(null);
        return carsRepository.save(car);
    }
}
