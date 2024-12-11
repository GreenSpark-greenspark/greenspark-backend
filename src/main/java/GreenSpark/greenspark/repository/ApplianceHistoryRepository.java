package GreenSpark.greenspark.repository;

import GreenSpark.greenspark.domain.ApplianceHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplianceHistoryRepository extends JpaRepository<ApplianceHistory, Long> {
    List<ApplianceHistory> findByUser_UserId(long userId);
}
