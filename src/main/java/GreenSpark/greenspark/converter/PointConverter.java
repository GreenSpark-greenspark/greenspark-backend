package GreenSpark.greenspark.converter;

import GreenSpark.greenspark.domain.Point;
import GreenSpark.greenspark.domain.Power;
import GreenSpark.greenspark.domain.User;
import GreenSpark.greenspark.dto.PointRequestDto;
import GreenSpark.greenspark.dto.PointResponseDto;
import GreenSpark.greenspark.dto.PowerRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class PointConverter {

    public static Point pointUpdateRequestDtotoPoint(User user, LocalDate now, int afterPoint, PointRequestDto.PointUpdateRequestDto pointUpdateRequestDto){
        return Point.builder()
                .user(user)
                .date(now)
                .afterPoint(afterPoint)
                .pointAmount(pointUpdateRequestDto.getPointAmount())
                .event(pointUpdateRequestDto.getEvent())
                .build();
    }

    public static PointResponseDto.PointUpdateResponseDto pointtoPointUpdateResponseDto(User user, Point point){
        return PointResponseDto.PointUpdateResponseDto.builder()
                .userId(user.getUserId())
                .date(point.getDate())
                .afterPoint(point.getAfterPoint())
                .pointAmount(point.getPointAmount())
                .event(point.getEvent())
                .build();
    }
}
