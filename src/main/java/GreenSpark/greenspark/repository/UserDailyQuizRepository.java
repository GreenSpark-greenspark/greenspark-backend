package GreenSpark.greenspark.repository;

import GreenSpark.greenspark.domain.UserDailyQuiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface UserDailyQuizRepository extends JpaRepository<UserDailyQuiz, Long> {
    List<UserDailyQuiz> findByUserIdAndProvidedDate(Long userId, LocalDate providedDate);
}