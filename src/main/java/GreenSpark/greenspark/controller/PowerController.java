package GreenSpark.greenspark.controller;

import GreenSpark.greenspark.converter.PowerConverter;
import GreenSpark.greenspark.domain.Power;
import GreenSpark.greenspark.domain.User;
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

    @PostMapping(value = "/power/cost")
    public DataResponseDto<PowerResponseDto.PowerCreateResponseDto> createCostPower(
            @CookieValue("access") String authorization,
            @RequestBody PowerRequestDto.PowerCreateCostRequestDto powerCreateCostRequestDto){
        Power power = powerService.createCostPower(authorization, powerCreateCostRequestDto);
        Long createdUserId = power.getUser().getUserId();
        PowerResponseDto.PowerCreateResponseDto responseDto = PowerConverter.toPowerCreateResponseDto(createdUserId);

        return DataResponseDto.of(responseDto, "전기요금 입력을 완료했습니다.");
    }

    @PostMapping(value = "/power/usage")
    public DataResponseDto<PowerResponseDto.PowerCreateResponseDto> createUsagePower(
            @CookieValue("access") String authorization,
            @RequestBody PowerRequestDto.PowerCreateUsageRequestDto powerCreateUsageRequestDto){
        Power power = powerService.createUsagePower(authorization, powerCreateUsageRequestDto);
        Long createdUserId = power.getUser().getUserId();
        PowerResponseDto.PowerCreateResponseDto responseDto = PowerConverter.toPowerCreateResponseDto(createdUserId);

        return DataResponseDto.of(responseDto, "전력사용량 입력을 완료했습니다.");
    }

    @GetMapping(value = "/power")
    public DataResponseDto<List<PowerResponseDto.PowerGetDataResponseDto>> getPowerData(
            @CookieValue("access") String authorization,
            @RequestParam(name = "display") String display){
        List<PowerResponseDto.PowerGetDataResponseDto> powerDataList = powerService.getPowerData(authorization, display);
        return DataResponseDto.of(powerDataList, "해당 유저의 전기요금 또는 전력사용량을 조회했습니다.");
    }

    @GetMapping(value = "/power/history")
    public DataResponseDto<List<PowerResponseDto.PowerGetAllResponseDto>> getAllPowers(
            @CookieValue("access") String authorization){
        List<PowerResponseDto.PowerGetAllResponseDto> powerList = powerService.getAllPowers(authorization);
        return DataResponseDto.of(powerList, "해당 유저의 모든 파워 정보를 조회했습니다.");
    }

    @GetMapping(value = "/power/expect")
    public DataResponseDto<PowerResponseDto.PowerGetExpectedCostResponseDto> getExpectedCost(
            @CookieValue("access") String authorization){
        PowerResponseDto.PowerGetExpectedCostResponseDto expectedCostResponse = powerService.getExpectedCost(authorization);
        return DataResponseDto.of(expectedCostResponse, "해당 유저의 예상 요금과 저번달 요금을 조회했습니다.");
    }

    @DeleteMapping(value = "/power/reset")
    public DataResponseDto<PowerResponseDto.PowerResetResponseDto> resetPowers(
            @CookieValue("access") String authorization){
        User user = powerService.resetPowers(authorization);
        Long userId = user.getUserId();
        PowerResponseDto.PowerResetResponseDto responseDto = PowerConverter.toPowerResetResponseDto(userId);

        return DataResponseDto.of(responseDto, "해당 유저의 파워 DB를 초기화했습니다.");
    }

    // 저번달 요금, 저저번달 요금 조회하는 API(사용하지 않음)
//    @GetMapping(value = "/power/last-month/{userId}")
//    public DataResponseDto<PowerResponseDto.PowerGetLastMonthPowerResponseDto> getLastMonthPower(
//            @PathVariable Long userId){
//        PowerResponseDto.PowerGetLastMonthPowerResponseDto lastMonthResponse = powerService.getLastMonthPower(userId);
//        return DataResponseDto.of(lastMonthResponse, "해당 유저의 저번달 요금과 저저번달 요금을 조회했습니다.");
//    }
    // 예상요금, 저번달 요금 조회하는 API(사용하지 않음)
//    @GetMapping(value = "/power/expect/{userId}")
//    public DataResponseDto<PowerResponseDto.PowerGetExpectedCostResponseDto> getExpectedCost(
//            @PathVariable Long userId){
//        PowerResponseDto.PowerGetExpectedCostResponseDto expectedCostResponse = powerService.getExpectedCost(userId);
//        return DataResponseDto.of(expectedCostResponse, "해당 유저의 예상 요금과 저번달 요금을 조회했습니다.");
//    }

    // 전기요금, 전력사용량 2개 한 번에 입력받는 API(사용하지 않음)
//    @PostMapping(value = "/power/{userId}")
//    public DataResponseDto<PowerResponseDto.PowerCreateResponseDto> createPower(
//            @PathVariable Long userId,
//            @RequestBody PowerRequestDto.PowerCreateRequestDto powerCreateRequestDto){
//        Power power = powerService.createPower(userId, powerCreateRequestDto);
//        Long createdUserId = power.getUser().getUserId();
//        PowerResponseDto.PowerCreateResponseDto responseDto = PowerConverter.toPowerCreateResponseDto(createdUserId);
//
//        return DataResponseDto.of(responseDto, "전력 입력을 완료했습니다.");
//    }
}
