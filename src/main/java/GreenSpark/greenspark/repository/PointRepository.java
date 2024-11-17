package GreenSpark.greenspark.repository;

import GreenSpark.greenspark.domain.Point;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointRepository extends JpaRepository<Point, Long> {
}
