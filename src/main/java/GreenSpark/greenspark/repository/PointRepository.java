package GreenSpark.greenspark.repository;

import GreenSpark.greenspark.domain.Point;
import GreenSpark.greenspark.domain.Power;
import GreenSpark.greenspark.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PointRepository extends JpaRepository<Point, Long> {
    List<Point> findByUser(User user);
    List<Point> findByUserOrderByCreatedAtDesc(User user);
}
