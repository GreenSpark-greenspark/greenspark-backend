package GreenSpark.greenspark.repository;

import GreenSpark.greenspark.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
