package GreenSpark.greenspark.repository;

import GreenSpark.greenspark.domain.Power;
import GreenSpark.greenspark.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface PowerRepository extends JpaRepository<Power, Long> {
    Optional<Power> findByYearAndMonthAndUser(int year, int month, User user);
    List<Power> findAllByUserAndYearBetweenAndMonthBetween(User user, int startYear, int endYear, int startMonth, int endMonth);
    List<Power> findByUser(User user);
    Optional<Power> findByUserAndYearAndMonth(User user, int year, int month);
    @Modifying
    @Transactional
    @Query("DELETE FROM Power p WHERE p.year <= :thresholdYear")
    void deleteByYearLessThanEqual(int thresholdYear);
}
