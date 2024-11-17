package GreenSpark.greenspark.service;

import GreenSpark.greenspark.domain.User;
import GreenSpark.greenspark.repository.PointRepository;
import GreenSpark.greenspark.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PointService {

    private final PointRepository pointRepository;
    private final UserRepository userRepository;

    public int getTotalPoint(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        return user.getTotalPoint();
    }
}
