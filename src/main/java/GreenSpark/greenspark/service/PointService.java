package GreenSpark.greenspark.service;

import GreenSpark.greenspark.converter.PointConverter;
import GreenSpark.greenspark.domain.Point;
import GreenSpark.greenspark.domain.User;
import GreenSpark.greenspark.dto.PointRequestDto;
import GreenSpark.greenspark.dto.PointResponseDto;
import GreenSpark.greenspark.jwt.JWTUtil;
import GreenSpark.greenspark.repository.PointRepository;
import GreenSpark.greenspark.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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

    public PointResponseDto.PointUpdateResponseDto updatePoint(String authorization, PointRequestDto.PointUpdateRequestDto pointUpdateRequestDto) {
        Long userId = getUserId(authorization);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        int pointAmount = pointUpdateRequestDto.getPointAmount();
        int afterPoint = user.getTotalPoint() + pointAmount;
        LocalDate now = LocalDate.now();

        // 현재 유저의 totalPoint에 업데이트된 포인트를 합한 후 저장
        user.setTotalPoint(afterPoint);
        userRepository.save(user);

        // requestDto에 현재 날짜와 afterPoint를 합한 point 객체 생성 후 db에 저장
        Point point = PointConverter.pointUpdateRequestDtotoPoint(user, now, afterPoint, pointUpdateRequestDto);
        pointRepository.save(point);

        return PointConverter.pointtoPointUpdateResponseDto(user, point);
    }

    public List<PointResponseDto.PointGetAllResponseDto> getAllPoint(String authorization) {
        Long userId = getUserId(authorization);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        // createdAt이 가장 최근인 point 객체부터 내림차순 정렬
        List<Point> points = pointRepository.findByUserOrderByCreatedAtDesc(user);
        return points.stream()
                .map(point -> new PointResponseDto.PointGetAllResponseDto(point.getDate(), point.getAfterPoint(), point.getPointAmount(), point.getEvent()))
                .collect(Collectors.toList());
    }

    private long getUserId(String authorization){
        String token=authorization.replace("Bearer ","");
        String username = jwtUtil.getUsername(token);
        User user = userRepository.findByUsername(username);
        return user.getUserId();
    }
}
