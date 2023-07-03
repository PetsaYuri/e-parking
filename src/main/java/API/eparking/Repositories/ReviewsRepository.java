package API.eparking.Repositories;

import API.eparking.Models.Reviews;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewsRepository extends JpaRepository<Reviews, Long> {
    public List<Reviews> findByGrade(int grade);
}
