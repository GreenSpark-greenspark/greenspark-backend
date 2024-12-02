package GreenSpark.greenspark.repository;

import GreenSpark.greenspark.domain.Quiz;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface QuizRepository extends JpaRepository<Quiz, Long> {
    @Query("SELECT q FROM Quiz q WHERE q.quizId NOT IN " +
            "(SELECT uq.quiz.quizId FROM UserQuizRecord uq WHERE uq.userId = :userId)")
    Page<Quiz> findQuizzesExcludingSolvedByUser(@Param("userId") Long userId, Pageable pageable);
}
