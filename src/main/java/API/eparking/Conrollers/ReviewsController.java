package API.eparking.Conrollers;

import API.eparking.DTO.ReviewDTO;
import API.eparking.Models.Reviews;
import API.eparking.Models.Users;
import API.eparking.Services.ReviewsService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewsController {

    private final ReviewsService reviewsService;

    @Autowired
    public ReviewsController(ReviewsService reviewsService) {
        this.reviewsService = reviewsService;
    }

    @GetMapping("{id}")
    public ResponseEntity<ReviewDTO> get(@PathVariable("id") Long id)    {
        try {
            Reviews review = reviewsService.getById(id);
            return ResponseEntity.ok(new ReviewDTO(review.getBody(), review.getGrade(), review.getUser()));
        }   catch (EntityNotFoundException ex)  {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity getAll(@RequestParam(required = false, defaultValue = "0") String grade)   {
        try {
            List<Reviews> reviews = reviewsService.getAll(grade);
            return ResponseEntity.ok(reviews.stream().map(review -> new ReviewDTO(review.getBody(), review.getGrade(), review.getUser())).toList());
        }   catch (NumberFormatException ex)    {
            return ResponseEntity.badRequest().body("Invalid value grade");
        }
    }

    @PostMapping
    public ResponseEntity add(@RequestBody ReviewDTO reviewDTO) {
        try {
            Reviews review = reviewsService.create(reviewDTO, SecurityContextHolder.getContext().getAuthentication().getName());
            return ResponseEntity.ok(new ReviewDTO(review.getBody(), review.getGrade(), review.getUser()));
        }   catch (DataIntegrityViolationException ex) {
            return ResponseEntity.badRequest().body("Not required field in the body");
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<ReviewDTO> update(@RequestBody ReviewDTO reviewDTO, @PathVariable("id") Long id) {
        try {
            Reviews review = reviewsService.update(reviewDTO, id);
            return ResponseEntity.ok(new ReviewDTO(review.getBody(), review.getGrade(), review.getUser()));
        }   catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id)   {
        try {
            boolean result = reviewsService.delete(id);
            return result ? ResponseEntity.ok("Successfully deleted") : ResponseEntity.internalServerError().body("An error occurred while deleting");
        }   catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}