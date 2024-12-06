package GreenSpark.greenspark.repository;

import GreenSpark.greenspark.domain.UserDailyQuiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface UserDailyQuizRepository extends JpaRepository<UserDailyQuiz, Long> {
    List<UserDailyQuiz> findByUserIdAndProvidedDate(Long userId, LocalDate providedDate);


    @Query("SELECT DISTINCT u.userId FROM UserDailyQuiz u")
    List<Long> findAllUserIds();

    void deleteByUserId(Long userId);
}