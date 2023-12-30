package API.eparking.Services;

import API.eparking.Exceptions.Users.*;
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

    public Users update(Long id, UserDTO updatedUser) throws EntityNotFoundException, NullPointerException {
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
        return usersRepository.save(currentUser);
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

    public List<Users> getAll() {
        return usersRepository.findAll();
    }

    public Users getByField(String email, String phone_number) throws UserNotFoundException {
        if (email != null)  {
            return usersRepository.findByEmail(email);
        }   else if (phone_number != null) {
           return usersRepository.findByPhoneNumber(phone_number);
        }
        throw new UserNotFoundException();
    }

    public Users getById(Long id)  {
        return usersRepository.getReferenceById(id);
    }

    public boolean delete(Long id) throws EntityNotFoundException {
        Users user = usersRepository.getReferenceById(id);

        if (!user.getTransactions().isEmpty()) {
            transactionsService.deleteUser(user);
        }

        usersRepository.delete(user);
        return !usersRepository.existsById(user.getId());
    }

    public Users add(UserDTO user) throws EmailAlreadyExistsException, PhoneAlreadyExistsException {
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

        /*if (savedUser.getId() == 1)   {
            newUser.setRole("owner");
        }*/
        return savedUser;
    }

    public Users setCarToUser(Users user, Cars car) throws EntityNotFoundException {
        if (usersRepository.existsById(user.getId())) {
            List<Cars> cars = user.getCars();
            cars.add(car);
            user.setCars(cars);
            return usersRepository.save(user);
        }   else {
            throw new EntityNotFoundException();
        }
    }

    public Users deleteUserCar(Users user, Cars car) throws UserNotHaveThisCarException, EntityNotFoundException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        String role = findByEmail(email).getRole();
        if (usersRepository.existsById(user.getId())) {
            if (user.getCars().contains(car) || role.equals("admin") || role.equals("owner")) {
                List<Cars> cars = user.getCars();
                cars.remove(car);
                user.setCars(cars);

                return usersRepository.save(user);
            }
            throw new UserNotHaveThisCarException();
        }   else {
            throw new EntityNotFoundException();
        }
    }

    public Users findByEmail(String email) throws EntityNotFoundException {
        Users user = usersRepository.findByEmail(email);
        if (user == null)  {
            throw new EntityNotFoundException();
        }
        return user;
    }

    public Users setAvatar(Long id, String filename) throws EntityNotFoundException   {
        Users user = usersRepository.getReferenceById(id);
        if (user == null)   {
            throw new EntityNotFoundException();
        }

        user.setImage(filename);
        return usersRepository.save(user);
    }

    public Users setRoleAdmin(Long id) throws EntityNotFoundException, UserHasNoRightsException {
        Users user = usersRepository.getReferenceById(id);

        if (user == null)   {
            throw new EntityNotFoundException();
        }

        if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString().equals("[ROLE_owner]")) {
            user.setRole("admin");
            return usersRepository.save(user);
        }
        throw new UserHasNoRightsException();
    }

    public Users removeAdmin(Long id)  throws EntityNotFoundException, UserHasNoRightsException {
        Users user = usersRepository.getReferenceById(id);

        if (user == null)   {
            throw new EntityNotFoundException();
        }

        if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString().equals("[ROLE_owner]")) {
            user.setRole("user");
            return usersRepository.save(user);
        }
        throw new UserHasNoRightsException();
    }

    public Users banTo(Long id) throws EntityNotFoundException, UserHasNoRightsException {
        Users user = usersRepository.getReferenceById(id);
        if (user == null)   {
            throw new EntityNotFoundException();
        }

        if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString().equals("[ROLE_owner]") ||
                SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString().equals("[ROLE_admin]")) {
            user.setBanned(true);
            return usersRepository.save(user);
        }
        throw new UserHasNoRightsException();
    }

    public Users unbanTo(Long id) throws EntityNotFoundException, UserHasNoRightsException {
        Users user = usersRepository.getReferenceById(id);
        if (user == null) {
            throw new EntityNotFoundException();
        }

        if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString().equals("[ROLE_owner]") ||
                SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString().equals("[ROLE_admin]")) {
            user.setBanned(false);
            return usersRepository.save(user);
        }
        throw new UserHasNoRightsException();
    }
}
