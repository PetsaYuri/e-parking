package API.eparking.Repositories;

import API.eparking.Models.Cars;
import API.eparking.Models.PromoCodes;
import API.eparking.Models.Transactions;
import API.eparking.Models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionsRepository extends JpaRepository<Transactions, Long> {

    public List<Transactions> findByPromoCode(PromoCodes promoCode);

    public List<Transactions> findByCar(Cars car);

    public List<Transactions> findByUser(Users user);
}
