package GreenSpark.greenspark.repository;

import GreenSpark.greenspark.domain.Refresh;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshRepository extends JpaRepository<Refresh,Long> {
    Boolean existsByRefresh(String refresh);

    Optional<Refresh> findByRefresh(String refresh);
    Optional<Refresh> findByUsername(String username);

    void deleteByRefresh(String refresh);

    void deleteByUsername(String username);

}
