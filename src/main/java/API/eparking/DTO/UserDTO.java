package API.eparking.DTO;

import API.eparking.Models.Cars;
import API.eparking.Models.Reviews;

import java.beans.Transient;
import java.util.List;

public record UserDTO(String password, String email, String phone_number, String first_name, String last_name, String image, String role, boolean is_banned, List<Cars> cars, List<Reviews> reviews) {

    public UserDTO withPassword(String newPassword)    {
        return new UserDTO(newPassword, email, phone_number, first_name, last_name, image, role, is_banned, cars, reviews);
    }
}
