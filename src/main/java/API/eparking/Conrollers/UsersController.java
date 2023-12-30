package API.eparking.Conrollers;

import API.eparking.DTO.UserDTO;
import API.eparking.Exceptions.Users.EmailAlreadyExistsException;
import API.eparking.Exceptions.Users.PhoneAlreadyExistsException;
import API.eparking.Exceptions.Users.UserHasNoRightsException;
import API.eparking.Exceptions.Users.UserNotFoundException;
import API.eparking.Models.Users;
import API.eparking.Services.UploadsImage;
import API.eparking.Services.UsersService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    private final UsersService usersService;
    private final UploadsImage uploadsImage;

    @Autowired
    public UsersController(UsersService usersService, UploadsImage uploadsImage) {
        this.usersService = usersService;
        this.uploadsImage = uploadsImage;
    }

    @GetMapping("{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable("id") Long id) {
        try {
            Users user = usersService.getById(id);
            return ResponseEntity.ok().body(new UserDTO(user.getPassword(), user.getEmail(), user.getPhoneNumber(), user.getFirst_name(), user.getLast_name(), user.getImage(), user.getRole(), user.isBanned(), user.getCars(), user.getReviews()));
        }   catch (EntityNotFoundException ex)  {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public List<UserDTO> getAll(@RequestParam(required = false) String role, @RequestParam(required = false, name = "is_banned") String banned) {
        return usersService.getAll(role, banned);
    }

    @GetMapping("/search")
    public ResponseEntity<UserDTO> search(@RequestParam(required = false) String email, @RequestParam(required = false) String phone_number)  {
        try {
            Users receivedUser = usersService.getByField(email, phone_number);
            return ResponseEntity.ok(new UserDTO(receivedUser.getPassword(), receivedUser.getEmail(), receivedUser.getPhoneNumber(), receivedUser.getFirst_name(),
                    receivedUser.getLast_name(), receivedUser.getImage(), receivedUser.getRole(), receivedUser.isBanned(), receivedUser.getCars(), receivedUser.getReviews()));
        }   catch (UserNotFoundException | NullPointerException ex)    {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/add")
    public ResponseEntity add(@RequestBody UserDTO dtoUser)  {
        try {
            Users user = usersService.add(dtoUser);
            return ResponseEntity.ok().body(new UserDTO(user.getPassword(), user.getEmail(), user.getPhoneNumber(),
                    user.getFirst_name(), user.getLast_name(), user.getImage(), user.getRole(), user.isBanned(),
                    user.getCars(), user.getReviews()));
        }   catch (EmailAlreadyExistsException ex)   {
            return ResponseEntity.badRequest().body("Email already used");
        }   catch (PhoneAlreadyExistsException ex)   {
            return ResponseEntity.badRequest().body("Phone already used");
        }
    }

    @PostMapping("{id}/upload")
    public ResponseEntity<UserDTO> uploadImage(@PathVariable("id") Long id, @RequestParam("file") MultipartFile file)  {
        try {
            String filename = uploadsImage.createUUIDFilename(file.getOriginalFilename() + " ");
            uploadsImage.saveImage(file, filename);
            Users user = usersService.setAvatar(id, filename);
            return ResponseEntity.ok(new UserDTO(user.getPassword(), user.getEmail(), user.getPhoneNumber(), user.getFirst_name(),
                    user.getLast_name(), user.getImage(), user.getRole(), user.isBanned(), user.getCars(), user.getReviews()));
        }   catch (EntityNotFoundException ex)  {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("{id}")
    public ResponseEntity update(@PathVariable("id") Long id, @RequestBody UserDTO updatedUser)  {
        try {
            Users user = usersService.update(id, updatedUser);
            return ResponseEntity.ok(new UserDTO(user.getPassword(), user.getEmail(), user.getPhoneNumber(), user.getFirst_name(),
                    user.getLast_name(), user.getImage(), user.getRole(), user.isBanned(), user.getCars(), user.getReviews()));
        }   catch (EntityNotFoundException ex)  {
            return ResponseEntity.notFound().build();
        }   catch (NullPointerException ex) {
            return ResponseEntity.badRequest().body("Bad field in the body");
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id) {
        try {
            return usersService.delete(id) ? ResponseEntity.ok().body("Successfully deleted") : ResponseEntity.notFound().build();
        }   catch (EntityNotFoundException ex)  {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/setAdmin/{id}")
    public ResponseEntity<UserDTO> setAdmin(@PathVariable("id") Long id)   {
        try {
            Users receivedUser = usersService.setRoleAdmin(id);
            return ResponseEntity.ok(new UserDTO(receivedUser.getPassword(), receivedUser.getEmail(), receivedUser.getPhoneNumber(),
                    receivedUser.getFirst_name(), receivedUser.getLast_name(), receivedUser.getImage(), receivedUser.getRole(),
                    receivedUser.isBanned(), receivedUser.getCars(), receivedUser.getReviews()));
        }   catch (EntityNotFoundException ex)  {
            return ResponseEntity.notFound().build();
        }   catch (UserHasNoRightsException ex)  {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PostMapping("/removeAdmin/{id}")
    public ResponseEntity<UserDTO> removeAdmin(@PathVariable("id") Long id)   {
        try {
            Users user = usersService.removeAdmin(id);
            return ResponseEntity.ok(new UserDTO(user.getPassword(), user.getEmail(), user.getPhoneNumber(), user.getFirst_name(),
                    user.getLast_name(), user.getImage(), user.getRole(), user.isBanned(), user.getCars(), user.getReviews()));
        } catch (EntityNotFoundException ex)    {
            return ResponseEntity.notFound().build();
        }   catch (UserHasNoRightsException ex)  {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PostMapping("/banTo/{id}")
    public ResponseEntity<UserDTO> banToUser(@PathVariable("id") Long id) {
        try {
            Users user = usersService.banTo(id);
            return ResponseEntity.ok(new UserDTO(user.getPassword(), user.getEmail(), user.getPhoneNumber(), user.getFirst_name(), user.getLast_name(),
                    user.getImage(), user.getRole(), user.isBanned(), user.getCars(), user.getReviews()));
        }   catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }   catch (UserHasNoRightsException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PostMapping("/unbanTo/{id}")
    public ResponseEntity<UserDTO> unbanToUser(@PathVariable("id") Long id)  {
        try {
            Users user = usersService.unbanTo(id);
            return ResponseEntity.ok(new UserDTO(user.getPassword(), user.getEmail(), user.getPhoneNumber(), user.getFirst_name(), user.getLast_name(),
                    user.getImage(), user.getRole(), user.isBanned(), user.getCars(), user.getReviews()));
        }   catch (EntityNotFoundException ex)  {
            return ResponseEntity.notFound().build();
        }   catch (UserHasNoRightsException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}