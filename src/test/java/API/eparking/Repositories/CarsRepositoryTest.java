package API.eparking.Repositories;

import API.eparking.Models.Cars;
import API.eparking.Models.Users;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CarsRepositoryTest {

    @Autowired
    private CarsRepository carsRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Test
    void testGetReferenceById_ReturnCar() {
        Long carId = 2L;
        Cars receivedCar = carsRepository.getReferenceById(carId);

        assertEquals(carId, receivedCar.getId());
        assertEquals("green", receivedCar.getColor());
        assertNull(receivedCar.getImage());
        assertEquals("AO5321AB", receivedCar.getNumbers());
        assertEquals('C', receivedCar.getType());
        assertNull(receivedCar.getParking());
    }

    @Test
    void testGetReferenceById_WithNonExistingId_ReturnLazyInitializationException() {
        assertThrows(LazyInitializationException.class, () -> carsRepository.getReferenceById(9999L).getColor());
    }

    @Test
    void testExistsById_ReturnBoolean() {
        boolean isExistsCar = carsRepository.existsById(2L);
        assertTrue(isExistsCar);
    }

    @Test
    void testExistsById_WithNonExistingId_ReturnBoolean() {
        boolean isExistsCar = carsRepository.existsById(9999L);
        assertFalse(isExistsCar);
    }

    @Test
    void testSave_ReturnCar() {
        String color = "black", numbers = "AA4758AA";
        char type = 'C';
        Users user = usersRepository.getReferenceById(1L);
        Cars car = new Cars(numbers, color, type, user);

        Cars savedCar = carsRepository.save(car);
        assertNotNull(savedCar);
        assertEquals(color, savedCar.getColor());
        assertEquals(numbers, savedCar.getNumbers());
        assertEquals(type, savedCar.getType());
        assertEquals(user, savedCar.getUser());
    }

    @Test
    void testDelete_ReturnNothing() {
        Long carId = 10L;
        Cars car = carsRepository.getReferenceById(carId);
        carsRepository.delete(car);
        assertFalse(carsRepository.existsById(carId));
    }

    @Test
    void testDelete_WithNonExistingId_Return() {
        Long carId = 9999L;
        Cars car = carsRepository.getReferenceById(carId);
        assertThrows(EntityNotFoundException.class, () -> carsRepository.delete(car));
    }

    @Test
    void findByNumbers() {
    }
}