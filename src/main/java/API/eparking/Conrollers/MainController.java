package API.eparking.Conrollers;

import API.eparking.Exceptions.Users.EmailAlreadyExistsException;
import API.eparking.Exceptions.Users.PhoneAlreadyExistsException;
import API.eparking.Models.Users;
import API.eparking.Services.UsersService;
import API.eparking.DTO.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
public class MainController {

    private final UsersService usersService;

    @Autowired
    @Lazy
    public MainController(UsersService usersService)  {
        this.usersService = usersService;
    }

    @GetMapping("/profile")
    public Users profile()  {
       return usersService.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @PostMapping("/signup")
    public ResponseEntity<HashMap> signUp(@RequestBody UserDTO user)   {
        try {
            HashMap<String, UserDTO> hashMap = new HashMap<>();
            hashMap.put("user", usersService.add(user));
            return ResponseEntity.ok().body(hashMap);
        }   catch (EmailAlreadyExistsException ex)   {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("error_message", "Email already used");
            return ResponseEntity.badRequest().body(hashMap);
        }   catch (PhoneAlreadyExistsException ex)   {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("error_message", "Phone already used");
            return ResponseEntity.badRequest().body(hashMap);
        }
    }
}
