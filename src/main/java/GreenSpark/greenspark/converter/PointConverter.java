package GreenSpark.greenspark.converter;

import GreenSpark.greenspark.domain.Power;
import GreenSpark.greenspark.domain.User;
import GreenSpark.greenspark.dto.PointResponseDto;
import GreenSpark.greenspark.dto.PowerRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PointConverter {

    public static PointResponseDto.PointGetResponseDto toPointGetResponseDto(Long userId, int totalPoint){
        return PointResponseDto.PointGetResponseDto.builder()
                .userId(userId)
                .totalPoint(totalPoint)
                .build();
    }
}
