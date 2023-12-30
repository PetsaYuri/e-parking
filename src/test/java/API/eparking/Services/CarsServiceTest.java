package API.eparking.Services;

import API.eparking.DTO.CarDTO;
import API.eparking.Exceptions.Cars.CarWithThisNumbersAlreadyExistsException;
import API.eparking.Models.Cars;
import API.eparking.Models.Parking;
import API.eparking.Models.Users;
import API.eparking.Repositories.CarsRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class CarsServiceTest {

    @Mock
    private CarsRepository carsRepository;

    @Mock
    private UsersService usersService;

    @Mock
    private TransactionsService transactionsService;

    @InjectMocks
    private CarsService carsService;

    private Users user;
    private Cars car;
    private CarDTO carDTO;
    private Long carId;
    private Parking parkingLot;

    @BeforeEach
    public void init() {
        user = new Users("test", "test", "099", "test@gmail.com", "test");
        car = new Cars("AA1234AA", "blue", 'A', user);
        carDTO = new CarDTO("blue", "AA1234AA", 'A', null, null, user);
        carId = 1L;
        parkingLot = new Parking(1, 50, 999);
    }

    @Test
    void testFindCarById_ReturnCar() {
        when(carsRepository.getReferenceById(carId)).thenReturn(car);
        Cars receivedCar = carsService.findCarById(carId);
        assertEquals(car, receivedCar);
        verify(carsRepository).getReferenceById(carId);
    }

    @Test
    void testFindCarById_WithNonExistingId_ReturnNotFoundException() {
       when(carsRepository.getReferenceById(carId)).thenThrow(EntityNotFoundException.class);
       assertThrows(EntityNotFoundException.class, () -> carsService.findCarById(carId));
       verify(carsRepository).getReferenceById(carId);
    }

    @Test
    void testAdd_ReturnCar() throws CarWithThisNumbersAlreadyExistsException {
        UserDetails userDetails = User.builder().username(user.getEmail()).password(user.getPassword()).roles("owner").build();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, user.getPassword(),
                userDetails.getAuthorities()));

        when(carsRepository.save(any(Cars.class))).thenReturn(car);
        when(usersService.findByEmail(user.getEmail())).thenReturn(user);

        Cars receivedCar = carsService.add(carDTO);
        assertEquals(car, receivedCar);
        assertNotNull(receivedCar);

        verify(carsRepository).save(any(Cars.class));
        verify(usersService).findByEmail(user.getEmail());
    }

    @Test
    void testAdd_WithNonExistingUser_ReturnNotFoundException() {
        UserDetails userDetails = User.builder().username("test").password("test").roles("user").build();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, "test",
                userDetails.getAuthorities()));

        assertThrows(EntityNotFoundException.class, () -> carsService.add(carDTO));
    }

    @Test
    void testAdd_WithInvalidNumbers_ReturnCarWithThisNumbersAlreadyExistsException() {
        when(carsRepository.findByNumbers(car.getNumbers())).thenThrow(CarWithThisNumbersAlreadyExistsException.class);
        assertThrows(CarWithThisNumbersAlreadyExistsException.class, () -> carsService.add(carDTO));
        verify(carsRepository).findByNumbers(car.getNumbers());
    }

    @Test
    void testUpdate_ReturnCar() {
        when(carsRepository.getReferenceById(carId)).thenReturn(car);
        when(carsRepository.save(any(Cars.class))).thenReturn(car);

        Cars receivedCar = carsService.update(carId, carDTO);
        assertEquals(car, receivedCar);
        assertNotNull(receivedCar);

        verify(carsRepository).getReferenceById(carId);
        verify(carsRepository).save(any(Cars.class));
    }

    @Test
    void testUpdate_WithNonExistingId_ReturnNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> carsService.update(carId, carDTO));
    }

    @Test
    void testDelete_ReturnBoolean() {
        car.setId(carId);
        when(carsRepository.getReferenceById(carId)).thenReturn(car);
        when(carsRepository.existsById(carId)).thenReturn(true);
        when(carsRepository.save(any(Cars.class))).thenReturn(car);

        boolean isDeletedCar = carsService.delete(carId);
        assertTrue(isDeletedCar);

        verify(carsRepository).getReferenceById(carId);
        verify(carsRepository).existsById(carId);
        verify(carsRepository).save(any(Cars.class));
    }

    @Test
    void testDelete_WithNonExistingId_ReturnNotFoundException() {
        car.setId(carId);
        when(carsRepository.getReferenceById(carId)).thenReturn(car);
        assertThrows(EntityNotFoundException.class, () -> carsService.delete(carId));
        verify(carsRepository).getReferenceById(carId);
    }

    @Test
    void testSetParkingLotToCar_ReturnCars() {
        when(carsRepository.existsById(car.getId())).thenReturn(true);
        when(carsRepository.save(any(Cars.class))).thenReturn(car);

        Cars receivedCar = carsService.setParkingLotToCar(car, parkingLot);
        assertEquals(car, receivedCar);

        verify(carsRepository).existsById(car.getId());
        verify(carsRepository).save(any(Cars.class));
    }

    @Test
    void testSetParkingLotToCar_WithNonExistingCar_ReturnNotFoundException() {
        car.setId(carId);
        assertThrows(EntityNotFoundException.class, () -> carsService.setParkingLotToCar(car, parkingLot));
    }

    @Test
    void testSetParkingLotToCar_WithNullValueInParking_ReturnNotFoundException() {
        when(carsRepository.existsById(car.getId())).thenReturn(true);
        assertThrows(EntityNotFoundException.class, () -> carsService.setParkingLotToCar(car, null));
        verify(carsRepository).existsById(car.getId());
    }

    @Test
    void deleteParkingLot_ReturnCar() {
        when(carsRepository.existsById(car.getId())).thenReturn(true);
        when(carsRepository.save(any(Cars.class))).thenReturn(car);

        Cars receivedCar = carsService.deleteParkingLot(car);
        assertEquals(car, receivedCar);

        verify(carsRepository).existsById(car.getId());
        verify(carsRepository).save(any(Cars.class));
    }

    @Test
    void deleteParkingLot_WithNonExistingCar_ReturnNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> carsService.deleteParkingLot(car));
    }
}