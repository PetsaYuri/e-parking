package API.eparking.Repositories;

import API.eparking.Models.PromoCodes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface PromocodesRepository extends JpaRepository<PromoCodes, Long> {

    public PromoCodes findByTitle(String title);

    public List<PromoCodes> findByEndsAfter(Timestamp ends);

    public List<PromoCodes> findByEndsBefore(Timestamp ends);
}
