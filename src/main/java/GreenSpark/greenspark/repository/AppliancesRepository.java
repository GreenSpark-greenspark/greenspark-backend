package GreenSpark.greenspark.repository;

import GreenSpark.greenspark.domain.Appliance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface AppliancesRepository extends JpaRepository<Appliance, Long> {
    List<Appliance> findByUser_UserId(Long userId);
    List<Appliance> findTop3ByUser_UserIdAndIsUpdatedOrderByUpdateDateDesc(Long userId, Boolean isUpdated);

    Collection<Object> findTop3ByUser_UserIdAndIsUpdatedFalseOrderByApplianceIdDesc(Long userId);

    boolean existsByUser_UserIdAndModelTerm(Long userId, String modelTerm);
}
