package GreenSpark.greenspark.repository;

import GreenSpark.greenspark.domain.Appliance;
import GreenSpark.greenspark.domain.Memo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemoRepository extends JpaRepository<Memo, Long> {
    List<Memo> findByAppliance(Appliance appliance);
}
