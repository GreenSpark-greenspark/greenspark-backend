package GreenSpark.greenspark.repository;

import GreenSpark.greenspark.domain.Appliance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppliancesRepository extends JpaRepository<Appliance, Long> {
}
