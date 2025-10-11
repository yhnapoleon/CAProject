package sg.nusiss.t6.caproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sg.nusiss.t6.caproject.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUserName(String userName);
    Boolean existsByUserName(String userName);
    Boolean existsByUserEmail(String userEmail);
    Boolean existsByUserPhone(String userPhone);
}
