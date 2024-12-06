package GreenSpark.greenspark.repository;

import GreenSpark.greenspark.domain.Quiz;
import GreenSpark.greenspark.domain.UserQuizRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserQuizRecordRepository extends JpaRepository<UserQuizRecord, Long> {

    boolean existsByUserIdAndQuiz(Long userId, Quiz quiz);

    boolean existsByUserIdAndQuiz_QuizId(Long userId, Long quizId);
}
