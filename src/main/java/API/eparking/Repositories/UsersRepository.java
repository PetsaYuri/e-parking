package API.eparking.Repositories;

import API.eparking.Models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

    public Users findByEmail(String email);

    public Users findByPhoneNumber(String phone_number);

    public List<Users> findByRole(String role);

    public List<Users> findByIsBanned(boolean banned);
}
