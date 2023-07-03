package API.eparking.Conrollers;

import API.eparking.Models.Reviews;
import API.eparking.Models.Users;
import API.eparking.Services.ReviewsService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Reviews get(@PathVariable("id") Reviews review)    {
        return review;
    }

    @GetMapping
    public List<Reviews> getAll(@RequestParam(required = false, defaultValue = "0") String grade)   {
        return reviewsService.getAll(grade);
    }

    @PostMapping
    public Reviews add(@RequestBody Reviews review) {
        return reviewsService.create(review, SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @PutMapping("{id}")
    public Reviews update(@RequestBody Reviews review, @PathVariable("id") Reviews reviewFromDB) {
        return reviewsService.update(review, reviewFromDB);
    }

    @DeleteMapping("{id}")
    public boolean delete(@PathVariable("id") Reviews review)   {
        return reviewsService.delete(review);
    }
}
