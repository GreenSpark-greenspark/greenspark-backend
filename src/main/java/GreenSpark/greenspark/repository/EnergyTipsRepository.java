package GreenSpark.greenspark.repository;

import GreenSpark.greenspark.domain.Category;
import GreenSpark.greenspark.domain.EnergyTips;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnergyTipsRepository extends JpaRepository<EnergyTips, Long> {
    List<EnergyTips> findByCategory(Category category);
}
