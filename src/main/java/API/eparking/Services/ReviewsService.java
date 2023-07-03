package API.eparking.Services;

import API.eparking.Models.Reviews;
import API.eparking.Models.Users;
import API.eparking.Repositories.ReviewsRepository;
import API.eparking.Repositories.UsersRepository;
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

    public List<Reviews> getAll(String grade)   {
        try {
            return Integer.valueOf(grade) == 0 ? reviewsRepository.findAll(Sort.by("id"))
                    : reviewsRepository.findByGrade(Integer.parseInt(grade));
        }   catch (NumberFormatException ex)    {
            return reviewsRepository.findAll(Sort.by("id"));
        }

    }

    public Reviews create(Reviews review, String email)  {
        Users user = usersRepository.findByEmail(email);
        if (user == null) {
            return null;
        }
        Reviews newReview = new Reviews(review.getBody(), review.getGrade(), user);
        return reviewsRepository.save(newReview);
    }

    public Reviews update(Reviews review, Reviews reviewFromDB)   {
        if(review.getBody() != null)    {
            reviewFromDB.setBody(review.getBody());
        }

        if(review.getGrade() != 0)   {
            reviewFromDB.setGrade(review.getGrade());
        }
        return reviewsRepository.save(reviewFromDB);
    }

    public boolean delete(Reviews review)   {
        reviewsRepository.delete(review);
        return true;
    }
}
