package GreenSpark.greenspark.repository;

import GreenSpark.greenspark.domain.Power;
import GreenSpark.greenspark.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PowerRepository extends JpaRepository<Power, Long> {
    Optional<Power> findByYearAndMonthAndUser(int year, int month, User user);
    List<Power> findAllByUserAndYearBetweenAndMonthBetween(User user, int startYear, int endYear, int startMonth, int endMonth);
    List<Power> findByUser(User user);
    Optional<Power> findByUserAndYearAndMonth(User user, int year, int month);
}
