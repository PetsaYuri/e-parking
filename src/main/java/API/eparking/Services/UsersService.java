package API.eparking.Services;

import API.eparking.Exceptions.Users.EmailAlreadyExistsException;
import API.eparking.Exceptions.Users.PhoneAlreadyExistsException;
import API.eparking.Exceptions.Users.UserHasNoRightsException;
import API.eparking.Exceptions.Users.UserNotFoundException;
import API.eparking.Models.Cars;
import API.eparking.Models.Users;
import API.eparking.Repositories.UsersRepository;
import API.eparking.DTO.UserDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UsersService {

    private final UsersRepository usersRepository;
    private final TransactionsService transactionsService;

    @Autowired
    @Lazy
    public UsersService(UsersRepository usersRepository, TransactionsService transactionsService)    {
        this.usersRepository = usersRepository;
        this.transactionsService = transactionsService;
    }

    public UserDTO update(Long id, UserDTO updatedUser) throws EntityNotFoundException, NullPointerException {
        Users currentUser = usersRepository.getReferenceById(id);
        if (!updatedUser.first_name().isEmpty()) {
            currentUser.setFirst_name(updatedUser.first_name());
        }

        if (!updatedUser.last_name().isEmpty()) {
            currentUser.setLast_name(updatedUser.last_name());
        }

        if (!updatedUser.phone_number().isEmpty()) {
            currentUser.setPhoneNumber(updatedUser.phone_number());
        }

        if (updatedUser.cars() != null)   {
            currentUser.setCars(updatedUser.cars());
        }
        usersRepository.save(currentUser);
        return new UserDTO(currentUser.getPassword(), currentUser.getEmail(), currentUser.getPhoneNumber(), currentUser.getFirst_name(), currentUser.getLast_name(),
                currentUser.getImage(), currentUser.getRole(), currentUser.isBanned(), currentUser.getCars(), currentUser.getReviews());
    }

    public List<UserDTO> getAll(String role, String banned) {
           if (role != null)  {
            return usersRepository.findByRole(role).stream().map(currentUser -> new UserDTO(currentUser.getPassword(), currentUser.getEmail(), currentUser.getPhoneNumber(),
                    currentUser.getFirst_name(), currentUser.getLast_name(), currentUser.getImage(), currentUser.getRole(), currentUser.isBanned(), currentUser.getCars(), currentUser.getReviews())).toList();
        }   else if(banned != null) {
            return usersRepository.findByIsBanned(Boolean.getBoolean(banned)).stream().map(currentUser -> new UserDTO(currentUser.getPassword(), currentUser.getEmail(),
                    currentUser.getPhoneNumber(), currentUser.getFirst_name(), currentUser.getLast_name(), currentUser.getImage(), currentUser.getRole(), currentUser.isBanned(), currentUser.getCars(), currentUser.getReviews())).toList();
        }   else {
            return usersRepository.findAll().stream().map(currentUser -> new UserDTO(currentUser.getPassword(), currentUser.getEmail(), currentUser.getPhoneNumber() ,
                    currentUser.getFirst_name(), currentUser.getLast_name(), currentUser.getImage(), currentUser.getRole(), currentUser.isBanned(), currentUser.getCars(), currentUser.getReviews())).toList();
        }
    }

    public UserDTO getByField(String email, String phone_number) throws UserNotFoundException, NullPointerException   {
        Users user;
        if (email != null)  {
            user = usersRepository.findByEmail(email);
            return new UserDTO(user.getPassword(), user.getEmail(), user.getPhoneNumber(), user.getFirst_name(), user.getLast_name(), user.getImage(),
                    user.getRole(), user.isBanned(), user.getCars(), user.getReviews());
        }   else if (phone_number != null) {
            user = usersRepository.findByPhoneNumber(phone_number);
            return new UserDTO(user.getPassword(), user.getEmail(), user.getPhoneNumber(), user.getFirst_name(), user.getLast_name(), user.getImage(),
                    user.getRole(), user.isBanned(), user.getCars(), user.getReviews());
        }
        throw new UserNotFoundException();
    }

    public Users getById(Long id)  {
        return usersRepository.getReferenceById(id);
    }

    public boolean delete(Long id) throws EntityNotFoundException {
        Users user = usersRepository.getReferenceById(id);
        if (user.getPassword() != null) {
            transactionsService.deleteUser(user);
            usersRepository.delete(user);
            return !usersRepository.existsById(user.getId());
        }
        return false;
    }

    public UserDTO add(UserDTO user) throws EmailAlreadyExistsException, PhoneAlreadyExistsException {
        Users existsEmail = usersRepository.findByEmail(user.email());
        Users existsPhone = usersRepository.findByPhoneNumber(user.phone_number());

        if (existsEmail != null)    {
            throw new EmailAlreadyExistsException();
        }

        if (existsPhone != null)    {
            throw new PhoneAlreadyExistsException();
        }

        BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();
        UserDTO newDtoUser = user.withPassword(bCrypt.encode(user.password()));
        Users newUser = new Users(newDtoUser.first_name(), newDtoUser.last_name(), newDtoUser.phone_number(), newDtoUser.email(), newDtoUser.password());
        Users savedUser = usersRepository.save(newUser);

        if (savedUser.getId() == 1)   {
            newUser.setRole("owner");
        }
        return new UserDTO(savedUser.getPassword(), savedUser.getEmail(), savedUser.getPhoneNumber(), savedUser.getFirst_name(), savedUser.getLast_name(), savedUser.getImage(),
                savedUser.getRole(), user.is_banned(), new ArrayList<>(), new ArrayList<>());
    }

    public Users setCarToUser(Users user, Cars car)   {
        List<Cars> cars = user.getCars();
        cars.add(car);
        user.setCars(cars);
        return usersRepository.save(user);
    }

    public Users deleteTheUsersCar(Users user, Cars car)  {
        List<Cars> cars = user.getCars();
        cars.remove(car);
        user.setCars(cars);
        return usersRepository.save(user);
    }

    public Users findById(long id)  {
        return usersRepository.getReferenceById(id);
    }

    public Users findByEmail(String email)  {
        return usersRepository.findByEmail(email);
    }

    public UserDTO setAvatar(Users user, String filename)    {
        user.setImage(filename);
        usersRepository.save(user);
        return new UserDTO(user.getPassword(), user.getEmail(), user.getPhoneNumber(), user.getFirst_name(), user.getLast_name(), user.getImage(), user.getRole(),
                user.isBanned(), user.getCars(), user.getReviews());
    }

    public UserDTO setRoleAdmin(Long id) throws EntityNotFoundException, UserHasNoRightsException {
        Users user = usersRepository.getReferenceById(id);
        if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString().equals("[ROLE_owner]")) {
            user.setRole("admin");
            usersRepository.save(user);
            return new UserDTO(user.getPassword(), user.getEmail(), user.getPhoneNumber(), user.getFirst_name(), user.getLast_name(), user.getImage(),
                    user.getRole(), user.isBanned(), user.getCars(), user.getReviews());
        }
        throw new UserHasNoRightsException();
    }

    public UserDTO removeAdmin(Long id)  throws EntityNotFoundException, UserHasNoRightsException {
        Users user = usersRepository.getReferenceById(id);
        if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString().equals("[ROLE_owner]")) {
            user.setRole("user");
            usersRepository.save(user);
            return new UserDTO(user.getPassword(), user.getEmail(), user.getPhoneNumber(), user.getFirst_name(), user.getLast_name(), user.getImage(),
                    user.getRole(), user.isBanned(), user.getCars(), user.getReviews());
        }
        throw new UserHasNoRightsException();
    }

    public UserDTO banTo(Long id) throws EntityNotFoundException, UserHasNoRightsException {
        Users user = usersRepository.getReferenceById(id);
        if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString().equals("[ROLE_owner]") ||
                SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString().equals("[ROLE_admin]")) {
            user.setBanned(true);
            usersRepository.save(user);
            return new UserDTO(user.getPassword(), user.getEmail(), user.getPhoneNumber(), user.getFirst_name(), user.getLast_name(), user.getImage(),
                    user.getRole(), user.isBanned(), user.getCars(), user.getReviews());
        }
        throw new UserHasNoRightsException();
    }

    public UserDTO unbanTo(Long id) throws EntityNotFoundException, UserHasNoRightsException {
        Users user = usersRepository.getReferenceById(id);
        if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString().equals("[ROLE_owner]") ||
                SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString().equals("[ROLE_admin]")) {
            user.setBanned(false);
            usersRepository.save(user);
            return new UserDTO(user.getPassword(), user.getEmail(), user.getPhoneNumber(), user.getFirst_name(), user.getLast_name(), user.getImage(),
                    user.getRole(), user.isBanned(), user.getCars(), user.getReviews());
        }
        throw new UserHasNoRightsException();
    }
}
