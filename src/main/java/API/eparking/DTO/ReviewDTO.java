package API.eparking.DTO;

import API.eparking.Models.Users;

public record ReviewDTO(String body, int grade, Users user) {
}
