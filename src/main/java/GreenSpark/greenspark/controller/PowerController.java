package GreenSpark.greenspark.controller;

import GreenSpark.greenspark.converter.PowerConverter;
import GreenSpark.greenspark.domain.Power;
import GreenSpark.greenspark.dto.PowerRequestDto;
import GreenSpark.greenspark.dto.PowerResponseDto;
import GreenSpark.greenspark.response.DataResponseDto;
import GreenSpark.greenspark.service.PowerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class PowerController {

    private final PowerService powerService;

    @PostMapping(value = "/power/cost/{userId}")
    public DataResponseDto<PowerResponseDto.PowerCreateResponseDto> createCostPower(
            @PathVariable Long userId,
            @RequestBody PowerRequestDto.PowerCreateCostRequestDto powerCreateCostRequestDto){
        Power power = powerService.createCostPower(userId, powerCreateCostRequestDto);
        Long createdUserId = power.getUser().getUserId();
        PowerResponseDto.PowerCreateResponseDto responseDto = PowerConverter.toPowerCreateResponseDto(createdUserId);

        return DataResponseDto.of(responseDto, "전기요금 입력을 완료했습니다.");
    }

    @PostMapping(value = "/power/usage/{userId}")
    public DataResponseDto<PowerResponseDto.PowerCreateResponseDto> createUsagePower(
            @PathVariable Long userId,
            @RequestBody PowerRequestDto.PowerCreateUsageRequestDto powerCreateUsageRequestDto){
        Power power = powerService.createUsagePower(userId, powerCreateUsageRequestDto);
        Long createdUserId = power.getUser().getUserId();
        PowerResponseDto.PowerCreateResponseDto responseDto = PowerConverter.toPowerCreateResponseDto(createdUserId);

        return DataResponseDto.of(responseDto, "전력사용량 입력을 완료했습니다.");
    }

    @GetMapping(value = "/power/{userId}")
    public DataResponseDto<List<PowerResponseDto.PowerGetDataResponseDto>> getPowerData(
            @PathVariable Long userId,
            @RequestParam(name = "display") String display){
        List<PowerResponseDto.PowerGetDataResponseDto> powerDataList = powerService.getPowerData(userId, display);
        return DataResponseDto.of(powerDataList, "해당 유저의 전기요금 또는 전력사용량을 조회했습니다.");
    }

    @GetMapping(value = "/power/history/{userId}")
    public DataResponseDto<List<PowerResponseDto.PowerGetAllResponseDto>> getAllPowers(
            @PathVariable Long userId){
        List<PowerResponseDto.PowerGetAllResponseDto> powerList = powerService.getAllPowers(userId);
        return DataResponseDto.of(powerList, "해당 유저의 모든 파워 정보를 조회했습니다.");
    }

    // 전기요금, 전력사용량 2개 한 번에 입력받는 API(사용하지 않음)
    @PostMapping(value = "/power/{userId}")
    public DataResponseDto<PowerResponseDto.PowerCreateResponseDto> createPower(
            @PathVariable Long userId,
            @RequestBody PowerRequestDto.PowerCreateRequestDto powerCreateRequestDto){
        Power power = powerService.createPower(userId, powerCreateRequestDto);
        Long createdUserId = power.getUser().getUserId();
        PowerResponseDto.PowerCreateResponseDto responseDto = PowerConverter.toPowerCreateResponseDto(createdUserId);

        return DataResponseDto.of(responseDto, "전력 입력을 완료했습니다.");
    }

    @GetMapping(value = "/power/last-month/{userId}")
    public DataResponseDto<PowerResponseDto.PowerGetLastMonthPowerResponseDto> getLastMonthPower(
            @PathVariable Long userId){
        PowerResponseDto.PowerGetLastMonthPowerResponseDto lastMonthList = powerService.getLastMonthPower(userId);
        return DataResponseDto.of(lastMonthList, "해당 유저의 저번달 요금과 저저번달 요금을 조회했습니다.");
    }

    @GetMapping(value = "/power/expect/{userId}")
    public DataResponseDto<PowerResponseDto.PowerGetExpectedCostResponseDto> getExpectedCost(
            @PathVariable Long userId){
        PowerResponseDto.PowerGetExpectedCostResponseDto lastMonthList = powerService.getExpectedCost(userId);
        return DataResponseDto.of(lastMonthList, "해당 유저의 예상 요금과 저번달 요금을 조회했습니다.");
    }
}
