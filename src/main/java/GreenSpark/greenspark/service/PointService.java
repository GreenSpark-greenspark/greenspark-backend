package GreenSpark.greenspark.service;

import GreenSpark.greenspark.domain.User;
import GreenSpark.greenspark.jwt.JWTUtil;
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
    private final JWTUtil jwtUtil;

    public int getTotalPoint(String authorization) {
        Long userId = getUserId(authorization);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        return user.getTotalPoint();
    }

    private long getUserId(String authorization){
        String token=authorization.replace("Bearer ","");
        String username = jwtUtil.getUsername(token);
        User user = userRepository.findByUsername(username);
        return user.getUserId();
    }
}
