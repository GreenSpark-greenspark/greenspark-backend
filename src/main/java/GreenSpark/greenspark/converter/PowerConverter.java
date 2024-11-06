package GreenSpark.greenspark.converter;

import GreenSpark.greenspark.domain.Power;
import GreenSpark.greenspark.domain.User;
import GreenSpark.greenspark.dto.PowerRequestDto;
import GreenSpark.greenspark.dto.PowerResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

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
    public static Power CostRequestDtotoPower(User user, PowerRequestDto.PowerCreateCostRequestDto powerCreateCostRequestDto){
        return Power.builder()
                .year(powerCreateCostRequestDto.getYear())
                .month(powerCreateCostRequestDto.getMonth())
                .cost(powerCreateCostRequestDto.getCost())
                .user(user)
                .build();
    }
    public static Power UsageRequestDtotoPower(User user, PowerRequestDto.PowerCreateUsageRequestDto powerCreateUsageRequestDto){
        return Power.builder()
                .year(powerCreateUsageRequestDto.getYear())
                .month(powerCreateUsageRequestDto.getMonth())
                .usageAmount(powerCreateUsageRequestDto.getUsageAmount())
                .user(user)
                .build();
    }
    public static PowerResponseDto.PowerGetLastMonthPowerResponseDto toPowerGetLastMonthPowerResponseDto(int lastMonthCost, int monthBeforeLastCost){
        return PowerResponseDto.PowerGetLastMonthPowerResponseDto.builder()
                .lastMonthCost(lastMonthCost)
                .monthBeforeLastCost(monthBeforeLastCost)
                .build();
    }

    public static PowerResponseDto.PowerGetExpectedCostResponseDto toGetExpectedCostResponseDto(int expectedCost, int lastMonthCost){
        return PowerResponseDto.PowerGetExpectedCostResponseDto.builder()
                .expectedCost(expectedCost)
                .lastMonthCost(lastMonthCost)
                .build();
    }
}
