package GreenSpark.greenspark.repository;

import GreenSpark.greenspark.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    User findByUsername(String username);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.attendance = false")
    void updateAllAttendanceToFalse();
}
