package GreenSpark.greenspark.repository;

import GreenSpark.greenspark.domain.Appliance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppliancesRepository extends JpaRepository<Appliance, Long> {
    List<Appliance> findByUser_UserId(Long userId);
    List<Appliance> findTop3ByUser_UserIdAndIsUpdatedOrderByUpdateDateDesc(Long userId, Boolean isUpdated);
}
