package GreenSpark.greenspark.converter;

import GreenSpark.greenspark.domain.Power;
import GreenSpark.greenspark.domain.User;
import GreenSpark.greenspark.dto.PowerRequestDto;
import GreenSpark.greenspark.dto.PowerResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PowerConverter {

    public static Power toPower(User user, PowerRequestDto.PowerCreateRequestDto powerCreateRequestDto){
        return Power.builder()
                .year(powerCreateRequestDto.getYear())
                .month(powerCreateRequestDto.getMonth())
                .cost(powerCreateRequestDto.getCost())
                .usageAmount(powerCreateRequestDto.getUsageAmount())
                .user(user)
                .build();
    }

    public static PowerResponseDto.PowerCreateResponseDto toPowerCreateResponseDto(Long userId){
        return PowerResponseDto.PowerCreateResponseDto.builder()
                .userId(userId)
                .build();
    }
}
