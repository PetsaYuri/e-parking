package API.eparking.Conrollers;

import API.eparking.Exceptions.Users.EmailAlreadyExistsException;
import API.eparking.Exceptions.Users.PhoneAlreadyExistsException;
import API.eparking.Exceptions.Users.UserHasNoRightsException;
import API.eparking.Exceptions.Users.UserNotFoundException;
import API.eparking.Models.Users;
import API.eparking.Services.UploadsImage;
import API.eparking.Services.UsersService;
import API.eparking.DTO.UserDTO;
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
    public ResponseEntity getById(@PathVariable("id") Long id) {
        try {
            Users user = usersService.getById(id);
            return ResponseEntity.ok().body(new UserDTO(user.getPassword(), user.getEmail(), user.getPhoneNumber(), user.getFirst_name(), user.getLast_name(), user.getImage(), user.getRole(), user.isBanned(), user.getCars(), user.getReviews()));
        }   catch (EntityNotFoundException ex)  {
         return ResponseEntity.status(404).body("Not Found");
        }
    }

    @GetMapping
    public List<UserDTO> getAll(@RequestParam(required = false) String role, @RequestParam(required = false, name = "is_banned") String banned) {
        return usersService.getAll(role, banned);
    }

    @GetMapping("/search")
    public ResponseEntity search(@RequestParam(required = false) String email, @RequestParam(required = false) String phone_number)  {
        try {
            return ResponseEntity.ok(usersService.getByField(email, phone_number));
        }   catch (UserNotFoundException | NullPointerException ex)    {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    @PostMapping("/add")
    public ResponseEntity add(@RequestBody UserDTO dtoUser)  {
        try {
            return ResponseEntity.ok().body(usersService.add(dtoUser));
        }   catch (EmailAlreadyExistsException ex)   {
            return ResponseEntity.badRequest().body("Email already used");
        }   catch (PhoneAlreadyExistsException ex)   {
            return ResponseEntity.badRequest().body("Phone already used");
        }
    }

    @PutMapping("{id}/upload")
    public UserDTO uploadImage(@PathVariable("id") Users user, @RequestParam("file") MultipartFile file)  {
        String filename = uploadsImage.createUUIDFilename(file.getOriginalFilename());
        uploadsImage.saveImage(file, filename);
        return usersService.setAvatar(user, filename);
    }

    @PutMapping("{id}")
    public ResponseEntity update(@PathVariable("id") Long id, @RequestBody UserDTO updatedUser)  {
        try {
            return ResponseEntity.ok(usersService.update(id, updatedUser));
        }   catch (EntityNotFoundException ex)  {
            return ResponseEntity.status(404).body("Not Found");
        }   catch (NullPointerException ex) {
            return ResponseEntity.badRequest().body("Bad fields in the body");
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id) {
        try {
            return usersService.delete(id) ? ResponseEntity.ok().body("Successfully deleted") : ResponseEntity.status(404).body("Not Found");
        }   catch (EntityNotFoundException ex)  {
            return ResponseEntity.status(404).body("Not Found");
        }
    }

    @PostMapping("/setAdmin/{id}")
    public ResponseEntity setAdmin(@PathVariable("id") Long id)   {
        try {
            return ResponseEntity.ok(usersService.setRoleAdmin(id));
        }   catch (EntityNotFoundException ex)  {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }   catch (UserHasNoRightsException ex)  {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User has no rights");
        }
    }

    @PostMapping("/removeAdmin/{id}")
    public ResponseEntity removeAdmin(@PathVariable("id") Long id)   {
        try {
            return ResponseEntity.ok(usersService.removeAdmin(id));
        } catch (EntityNotFoundException ex)    {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }   catch (UserHasNoRightsException ex)  {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User has no rights");
        }
    }

    @PostMapping("/banTo/{id}")
    public ResponseEntity banToUser(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(usersService.banTo(id));
        }   catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }   catch (UserHasNoRightsException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User has no rights");
        }
    }

    @PostMapping("/unbanTo/{id}")
    public ResponseEntity unbanToUser(@PathVariable("id") Long id)  {
        try {
            return ResponseEntity.ok(usersService.unbanTo(id));
        }   catch (EntityNotFoundException ex)  {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }   catch (UserHasNoRightsException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User has no rights");
        }
    }
}