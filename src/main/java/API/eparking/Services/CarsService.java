package API.eparking.Services;

import API.eparking.DTO.CarDTO;
import API.eparking.Exceptions.Cars.CarWithThisNumbersAlreadyExistsException;
import API.eparking.Models.Cars;
import API.eparking.Models.Parking;
import API.eparking.Models.Users;
import API.eparking.Repositories.CarsRepository;
import jakarta.persistence.EntityNotFoundException;
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

    public Cars add(CarDTO car) throws CarWithThisNumbersAlreadyExistsException, EntityNotFoundException {
        if (carsRepository.findByNumbers(car.numbers()) == null) {
            Users user = usersService.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
            if (user == null) {
                throw new EntityNotFoundException();
            }

            Cars newCar = new Cars(car.numbers(), car.color(), car.type(), user);
            Cars savedCar = carsRepository.save(newCar);
            usersService.setCarToUser(user, newCar);
            return savedCar;
        }
        throw new CarWithThisNumbersAlreadyExistsException();
    }

    public Cars update(Long id, CarDTO updatedCar) throws EntityNotFoundException {
        Cars car = carsRepository.getReferenceById(id);
        if (car == null) {
            throw new EntityNotFoundException();
        }

        if (updatedCar.numbers() != null && !updatedCar.numbers().equals(car.getNumbers())) {
            car.setNumbers(updatedCar.numbers());
        }

        if (updatedCar.color() != null && !updatedCar.color().equals(car.getColor()))   {
            car.setColor(updatedCar.color());
        }

        if (Character.isLetter(updatedCar.type()) && updatedCar.type() != car.getType())   {
            car.setType(updatedCar.type());
        }

        return carsRepository.save(car);
    }

    public Boolean delete(Long id) throws EntityNotFoundException {
        Cars car = carsRepository.getReferenceById(id);
        if (carsRepository.existsById(car.getId())) {
            if(car.getUser() != null)   {
                transactionsService.deleteCar(car);
                usersService.deleteUserCar(car.getUser(), car);
                car.setUser(null);
                carsRepository.save(car);
            }
            carsRepository.delete(car);
            return true;
        }
        throw new EntityNotFoundException();
    }

    public Cars setParkingLotToCar(Cars car, Parking parking) throws EntityNotFoundException {
        if (!carsRepository.existsById(car.getId()))    {
            throw new EntityNotFoundException();
        }

        if (parking == null)    {
            throw new EntityNotFoundException();
        }

        car.setParking(parking);
        return carsRepository.save(car);
    }

    public Cars deleteParkingLot(Cars car) throws EntityNotFoundException {
        if (!carsRepository.existsById(car.getId())) {
            throw new EntityNotFoundException();
        }

        car.setParking(null);
        return carsRepository.save(car);
    }
}
