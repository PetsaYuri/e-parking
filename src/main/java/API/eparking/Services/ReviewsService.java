package API.eparking.Services;

import API.eparking.DTO.ReviewDTO;
import API.eparking.Models.Reviews;
import API.eparking.Models.Users;
import API.eparking.Repositories.ReviewsRepository;
import API.eparking.Repositories.UsersRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewsService {

    private final ReviewsRepository reviewsRepository;

    private final UsersRepository usersRepository;

    @Autowired
    public ReviewsService(ReviewsRepository reviewsRepository, UsersRepository usersRepository)  {
        this.reviewsRepository = reviewsRepository;
        this.usersRepository = usersRepository;
    }

    public Reviews getById(Long id) {
        return reviewsRepository.getReferenceById(id);
    }

    public List<Reviews> getAll(String grade)   {
            return Integer.parseInt(grade) == 0 ? reviewsRepository.findAll(Sort.by("id"))
                    : reviewsRepository.findByGrade(Integer.parseInt(grade));
    }

    public Reviews create(ReviewDTO reviewDTO, String email)  {
        Users user = usersRepository.findByEmail(email);
        if (user == null) {
            return null;
        }
        Reviews newReview = new Reviews(reviewDTO.body(), reviewDTO.grade(), user);
        return reviewsRepository.save(newReview);
    }

    public Reviews update(ReviewDTO reviewDTO, Long id)   {
        Reviews review = reviewsRepository.getReferenceById(id);
        if(reviewDTO.body() != null)    {
            review.setBody(reviewDTO.body());
        }

        if(reviewDTO.grade() != 0)   {
            review.setGrade(reviewDTO.grade());
        }
        return reviewsRepository.save(review);
    }

    public boolean delete(Long id) {
        if (reviewsRepository.existsById(id)) {
            Reviews review = reviewsRepository.getReferenceById(id);
            reviewsRepository.delete(review);
            return !reviewsRepository.existsById(id);
        }   else {
            throw new EntityNotFoundException();
        }
    }
}
