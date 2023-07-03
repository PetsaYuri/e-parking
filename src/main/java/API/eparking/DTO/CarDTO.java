package API.eparking.DTO;

import API.eparking.Models.Parking;
import API.eparking.Models.Users;

public record CarDTO(String color, String numbers, char type, String image, Parking parking_id, Users user_id) {
}
