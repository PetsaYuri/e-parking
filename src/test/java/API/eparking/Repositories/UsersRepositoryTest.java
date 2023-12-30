package API.eparking.Repositories;

import API.eparking.Models.Users;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UsersRepositoryTest {

    @Autowired
    private UsersRepository usersRepository;

    @Test
    void testGetReferenceById_ReturnUser() {
        Long userId = 7L;
        Users receivedUser = usersRepository.getReferenceById(userId);

        assertEquals(userId, receivedUser.getId());
        assertEquals("test", receivedUser.getFirst_name());
        assertEquals("test", receivedUser.getLast_name());
        assertEquals("test@gmail1.com12", receivedUser.getEmail());
        assertNull(receivedUser.getImage());
        assertFalse(receivedUser.isBanned());
        assertEquals("$2a$10$F6nA5Couyyky7Lvx7mZBHeK8lVmqbOlg2S2VG3iKkql23CCz13rQy", receivedUser.getPassword());
        assertEquals("12345678993", receivedUser.getPhoneNumber());
        assertEquals("user", receivedUser.getRole());
        assertEquals("11:40:56", receivedUser.getTime_registered().toString());
        assertEquals("2023-06-29", receivedUser.getDate_registered().toString());
    }

    @Test
    void testGetReferenceById_WithNonExistingId_ReturnLazyInitializationException() {
        Users receivedUser = usersRepository.getReferenceById(9999L);
        assertThrows(LazyInitializationException.class, () -> receivedUser.getEmail());
    }

    @Test
    void testFindAll_ReturnUsersList() {
        List<Users> usersList = usersRepository.findAll();
        assertFalse(usersList.isEmpty());
        assertEquals(10, usersList.size());
    }

    @Test
    void testExistsById_ReturnBoolean() {
        Long userId = 1L;
        boolean isExists = usersRepository.existsById(userId);
        assertTrue(isExists);
    }

    @Test
    void testExistsById_WithNonExistingId_ReturnBoolean() {
        boolean isExists = usersRepository.existsById(9999L);
        assertFalse(isExists);
    }

    @Test
    void testSave_ReturnUser() {
        String first_name = "test", last_name = "test", phone_number = "123456789012", email = "test01@gmail.com";
        Users user = new Users(first_name, last_name, phone_number, email, "1234");
        Users receivedUser = usersRepository.save(user);

        assertEquals(first_name, receivedUser.getFirst_name());
        assertEquals(last_name, user.getLast_name());
        assertEquals(phone_number, user.getPhoneNumber());
        assertEquals(email, user.getEmail());
    }

    @Test
    void testDelete_ReturnNothing() {
        Long userId = 36L;
        Users user = usersRepository.getReferenceById(userId);
        usersRepository.delete(user);
        Users receivedUser = usersRepository.getReferenceById(userId);
        assertThrows(LazyInitializationException.class, () -> receivedUser.getEmail());
    }

    @Test
    void testDelete_WithNonExistingId_ReturnNothing() {
        Long userId = 9999L;
        Users user = usersRepository.getReferenceById(userId);
        usersRepository.delete(user);
        assertFalse(usersRepository.existsById(userId));
    }

    @Test
    void testFindByEmail_ReturnUser() {
        String email = "test@gmail.com";
        Users receivedUser = usersRepository.findByEmail(email);
        assertNotNull(receivedUser);
        assertEquals(email, receivedUser.getEmail());
    }

    @Test
    void testFindByEmail_WithInvalidEmail_ReturnNullUser() {
        String email = "test";
        Users receivedUser = usersRepository.findByEmail(email);
        assertNull(receivedUser);
    }

    @Test
    void testFindByPhoneNumber_ReturnUser() {
        String phone_number = "12345678991";
        Users receivedUser = usersRepository.findByPhoneNumber(phone_number);
        assertNotNull(receivedUser);
        assertEquals(phone_number, receivedUser.getPhoneNumber());
    }

    @Test
    void testFindByPhoneNumber_WithInvalidPhoneNumber_ReturnNullUser() {
        String phone_number = "123";
        Users receivedUser = usersRepository.findByPhoneNumber(phone_number);
        assertNull(receivedUser);
    }

    @Test
    void testFindByRole_ReturnUsersList() {
        String role = "user";
        List<Users> receivedUsersList = usersRepository.findByRole(role);
        receivedUsersList.stream().peek(user -> assertEquals(role, user.getRole()));
    }

    @Test
    void testFindByRole_WithInvalidRole_ReturnEmptyUsersList() {
        String role = "test";
        List<Users> receivedUsersList = usersRepository.findByRole(role);
        assertTrue(receivedUsersList.isEmpty());
    }

    @Test
    void testFindByIsBanned_ReturnUsersList() {
        boolean isBanned = true;
        List<Users> receivedUsersList = usersRepository.findByIsBanned(isBanned);
        receivedUsersList.stream().peek(user -> assertEquals(isBanned, user.isBanned()));
    }
}