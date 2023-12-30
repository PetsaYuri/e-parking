package API.eparking.Services;

import API.eparking.DTO.UserDTO;
import API.eparking.Exceptions.Users.*;
import API.eparking.Models.Cars;
import API.eparking.Models.Users;
import API.eparking.Repositories.UsersRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UsersServiceTest {

    @Mock
    private UsersRepository usersRepository;

    @InjectMocks
    private UsersService usersService;

    private Users user;
    private UserDTO userDTO;
    private Long userId;
    private Cars car;

    @BeforeEach
    public void init() {
        user = new Users("test", "test", "099", "test@gmail.com", "test");
        userDTO = new UserDTO(user.getPassword(), user.getEmail(), user.getPhoneNumber(), user.getFirst_name(), user.getLast_name(),
                user.getImage(), user.getRole(), user.isBanned(), user.getCars(), user.getReviews());
        userId = 105L;
        car = new Cars("AA1234AA", "blue", 'A', user);
    }

    @Test
    void testGetAllUsers_ReturnUsersList() {
        List<Users> users = new ArrayList<>();
        users.add(user);

        when(usersRepository.findAll()).thenReturn(users);
        List<Users> receivedUsers = usersRepository.findAll();
        assertEquals(users, receivedUsers);
        verify(usersRepository).findAll();
    }

    @Test
    void testGetUserById_ReturnUser() {
        when(usersRepository.getReferenceById(userId)).thenReturn(user);
        Users receivedUser = usersRepository.getReferenceById(userId);
        assertEquals(user, receivedUser);
        verify(usersRepository).getReferenceById(userId);
    }

    @Test
    void testGetUserById_ReturnNotFoundException() {
        when(usersRepository.getReferenceById(userId)).thenThrow(EntityNotFoundException.class);
        assertThrows(EntityNotFoundException.class, () -> usersService.getById(userId));
        verify(usersRepository).getReferenceById(userId);
    }

    @Test
    void testCreateUser_ReturnUser() throws EmailAlreadyExistsException, PhoneAlreadyExistsException {
        when(usersRepository.save(Mockito.any(Users.class))).thenReturn(user);
        Users savedUser = usersService.add(userDTO);
        assertEquals(user, savedUser);
        verify(usersRepository).save(any(Users.class));
    }

    @Test
    void testCreateUser_ReturnEmailAlreadyExistsException() {
        when(usersRepository.save(any(Users.class))).thenThrow(EmailAlreadyExistsException.class);
        assertThrows(EmailAlreadyExistsException.class, () -> usersService.add(userDTO));
        verify(usersRepository).save(any(Users.class));
    }

    @Test
    void testCreateUser_ReturnPhoneAlreadyExistsException() {
        when(usersRepository.save(any(Users.class))).thenThrow(PhoneAlreadyExistsException.class);
        assertThrows(PhoneAlreadyExistsException.class, () -> usersService.add(userDTO));
        verify(usersRepository).save(any(Users.class));
    }

    @Test
    void testUpdateUser_ReturnUserDTO() {
        when(usersRepository.getReferenceById(userId)).thenReturn(user);
        when(usersRepository.save(any(Users.class))).thenReturn(user);
        Users updatedUser = usersService.update(userId, userDTO);
        assertEquals(user, updatedUser);
        verify(usersRepository).getReferenceById(userId);
        verify(usersRepository).save(any(Users.class));
    }

    @Test
    void testUpdateUser_ReturnNotFoundException() {
        when(usersRepository.getReferenceById(userId)).thenThrow(EntityNotFoundException.class);
        assertThrows(EntityNotFoundException.class, () -> usersService.update(userId, userDTO));
        verify(usersRepository).getReferenceById(userId);
    }

    @Test
    void testUpdateUser_ReturnNullPointerException() {
        when(usersRepository.getReferenceById(userId)).thenReturn(user);
        assertThrows(NullPointerException.class, () -> usersService.update(userId, new UserDTO(null, null, null, null, null,
                null, null, false, null, null)));
        verify(usersRepository).getReferenceById(userId);
    }

    @Test
    void testDeleteUser_ReturnBoolean() {
        when(usersRepository.getReferenceById(userId)).thenReturn(user);
        assertTrue(usersService.delete(userId));
        verify(usersRepository).getReferenceById(userId);
        verify(usersRepository).delete(user);
    }

    @Test
    void testDeleteUser_ReturnNotFoundException() {
        when(usersRepository.getReferenceById(userId)).thenThrow(EntityNotFoundException.class);
        assertThrows(EntityNotFoundException.class, () -> usersService.delete(userId));
        verify(usersRepository).getReferenceById(userId);
    }

    @Test
    void testGetByField_ReturnUser() throws UserNotFoundException {
        when(usersRepository.findByEmail(any(String.class))).thenReturn(user);
        Users receivedUser = usersService.getByField(user.getEmail(), null);
        assertEquals(user, receivedUser);
        verify(usersRepository).findByEmail(any(String.class));
    }

    @Test
    void testGetByField_ReturnNotFoundException() {
        when(usersRepository.findByEmail(user.getEmail())).thenThrow(UserNotFoundException.class);
        assertThrows(UserNotFoundException.class, () -> usersService.getByField(user.getEmail(), null));
        verify(usersRepository).findByEmail(user.getEmail());
    }

    @Test
    void testSetCarToUser_ReturnUser() {
        user.setCars(new ArrayList<>());
        user.setId(userId);

        when(usersRepository.existsById(any(Long.class))).thenReturn(true);
        when(usersRepository.save(any(Users.class))).thenReturn(user);
        Users receivedUser = usersService.setCarToUser(user, car);

        assertFalse(receivedUser.getCars().isEmpty());
        assertEquals(user, receivedUser);

        verify(usersRepository).existsById(any(Long.class));
        verify(usersRepository).save(any(Users.class));
    }

    @Test
    void testSetCarToUser_ReturnUserNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> usersService.setCarToUser(user, car));
    }

    @Test
    void testDeleteUserCar_ReturnUser() {
        UserDetails userDetails = User.builder().username(user.getEmail()).password(user.getPassword()).roles("owner").build();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails,
                user.getPassword(), userDetails.getAuthorities()));

        List<Cars> carsList = new ArrayList<>();
        carsList.add(car);
        user.setCars(carsList);
        user.setId(userId);
        user.setRole("owner");

        when(usersRepository.existsById(any(Long.class))).thenReturn(true);
        when(usersRepository.save(any(Users.class))).thenReturn(user);
        when(usersRepository.findByEmail(any(String.class))).thenReturn(user);

        Users receivedUser = usersService.deleteUserCar(user, car);

        assertFalse(receivedUser.getCars().contains(car));

        verify(usersRepository).existsById(any(Long.class));
        verify(usersRepository).save(any(Users.class));
    }

    @Test
    void testDeleteUserCar_ReturnUserNotFoundException() {
        UserDetails userDetails = User.builder().username(user.getEmail()).password(user.getPassword()).roles("owner").build();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails,
                user.getPassword(), userDetails.getAuthorities()));
        when(usersRepository.findByEmail(any(String.class))).thenReturn(user);
        assertThrows(EntityNotFoundException.class, () -> usersService.deleteUserCar(user, car));
        verify(usersRepository).findByEmail(any(String.class));
    }

    @Test
    void testDeleteUserCar_ReturnUserNotHaveThisCarException() {
        UserDetails userDetails = User.builder().username(user.getEmail()).password(user.getPassword()).roles("user").build();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails,
                user.getPassword(), userDetails.getAuthorities()));
        user.setId(userId);

        when(usersRepository.findByEmail(any(String.class))).thenReturn(user);
        when(usersRepository.existsById(any(Long.class))).thenReturn(true);

        assertThrows(UserNotHaveThisCarException.class, () -> usersService.deleteUserCar(user, car));

        verify(usersRepository).findByEmail(any(String.class));
        verify(usersRepository).existsById(any(Long.class));
    }

    @Test
    void testFindByEmail_ReturnUser() {
        when(usersRepository.findByEmail(any(String.class))).thenReturn(user);
        Users receivedUser = usersService.findByEmail(user.getEmail());
        assertEquals(user, receivedUser);
        verify(usersRepository).findByEmail(user.getEmail());
    }

    @Test
    void testFindByEmail_ReturnNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> usersService.findByEmail(user.getEmail()));
    }

    @Test
    void testSetAvatar_ReturnUser() {
        String filename = "filename1234";

        when(usersRepository.getReferenceById(userId)).thenReturn(user);
        user.setImage(filename);
        when(usersRepository.save(any(Users.class))).thenReturn(user);

        Users receivedUser = usersService.setAvatar(userId, filename);
        assertEquals(user, receivedUser);
        assertEquals(filename, receivedUser.getImage());

        verify(usersRepository).getReferenceById(userId);
        verify(usersRepository).save(any(Users.class));
    }

    @Test
    void testSetAvatar_ReturnNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> usersService.setAvatar(userId, "filename"));
    }

    @Test
    void testSetRoleAdmin_ReturnUser() {
        UserDetails userDetails = User.builder().username(user.getEmail()).password(user.getPassword()).roles("owner").build();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails,
                user.getPassword(), userDetails.getAuthorities()));

        when(usersRepository.getReferenceById(userId)).thenReturn(user);
        user.setRole("admin");
        when(usersRepository.save(any(Users.class))).thenReturn(user);

        Users receivedUser = usersService.setRoleAdmin(userId);
        assertEquals(user, receivedUser);
        assertEquals("admin", receivedUser.getRole());

        verify(usersRepository).getReferenceById(userId);
        verify(usersRepository).save(any(Users.class));
    }

    @Test
    void testSetRoleAdmin_ReturnNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> usersService.setRoleAdmin(userId));
    }

    @Test
    void testSetRoleAdmin_ReturnUserHasNoRightsException() {
        UserDetails userDetails = User.builder().username(user.getEmail()).password(user.getPassword()).roles("user").build();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, user.getPassword(), userDetails.getAuthorities()));

        when(usersRepository.getReferenceById(userId)).thenReturn(user);
        assertThrows(UserHasNoRightsException.class, () -> usersService.setRoleAdmin(userId));
        verify(usersRepository).getReferenceById(userId);
    }

    @Test
    void testRemoveAdmin_ReturnUser() {
        UserDetails userDetails = User.builder().username(user.getEmail()).password(user.getPassword()).roles("owner").build();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, user.getPassword(), userDetails.getAuthorities()));

        when(usersRepository.getReferenceById(userId)).thenReturn(user);
        when(usersRepository.save(any(Users.class))).thenReturn(user);

        Users receivedUser = usersService.removeAdmin(userId);
        assertEquals(user, receivedUser);
        assertEquals("user", receivedUser.getRole());

        verify(usersRepository).getReferenceById(userId);
        verify(usersRepository).save(any(Users.class));
    }

    @Test
    void testRemoveAdmin_ReturnNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> usersService.removeAdmin(userId));
    }

    @Test
    void testRemoveAdmin_ReturnUserHasNoRightsException() {
        UserDetails userDetails = User.builder().username(user.getEmail()).password(user.getPassword()).roles("user").build();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, user.getPassword(), userDetails.getAuthorities()));

        when(usersRepository.getReferenceById(userId)).thenReturn(user);
        assertThrows(UserHasNoRightsException.class, () -> usersService.removeAdmin(userId));
        verify(usersRepository).getReferenceById(userId);
    }

    @Test
    void testBanToUser_ReturnUser() {
        UserDetails userDetails = User.builder().username(user.getEmail()).password(user.getPassword()).roles("admin").build();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, user.getPassword(), userDetails.getAuthorities()));

        when(usersRepository.getReferenceById(userId)).thenReturn(user);
        user.setBanned(true);
        when(usersRepository.save(any(Users.class))).thenReturn(user);

        Users receivedUser = usersService.banTo(userId);
        assertEquals(user, receivedUser);
        assertTrue(receivedUser.isBanned());

        verify(usersRepository).getReferenceById(userId);
        verify(usersRepository).save(any(Users.class));
    }

    @Test
    void testBanToUser_ReturnNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> usersService.banTo(userId));
    }

    @Test
    void testBanToUser_ReturnUserHasNoRightsException() {
        UserDetails userDetails = User.builder().username(user.getEmail()).password(user.getPassword()).roles("user").build();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, user.getPassword(), userDetails.getAuthorities()));

        when(usersRepository.getReferenceById(userId)).thenReturn(user);
        assertThrows(UserHasNoRightsException.class, () -> usersService.banTo(userId));
        verify(usersRepository).getReferenceById(userId);
    }

    @Test
    void testUnbanToUser_ReturnUser() {
        UserDetails userDetails = User.builder().username(user.getEmail()).password(user.getPassword()).roles("admin").build();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, user.getPassword(), userDetails.getAuthorities()));

        when(usersRepository.getReferenceById(userId)).thenReturn(user);
        when(usersRepository.save(any(Users.class))).thenReturn(user);

        Users receivedUser = usersService.unbanTo(userId);
        assertEquals(user, receivedUser);
        assertFalse(receivedUser.isBanned());

        verify(usersRepository).getReferenceById(userId);
        verify(usersRepository).save(any(Users.class));
    }

    @Test
    void testUnbanToUser_ReturnNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> usersService.unbanTo(userId));
    }

    @Test
    void testUnbanToUser_ReturnUserHasNoRightsException() {
        UserDetails userDetails = User.builder().username(user.getEmail()).password(user.getPassword()).roles("user").build();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, user.getPassword(), userDetails.getAuthorities()));

        when(usersRepository.getReferenceById(userId)).thenReturn(user);
        assertThrows(UserHasNoRightsException.class, () -> usersService.unbanTo(userId));
        verify(usersRepository).getReferenceById(userId);
    }
}