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

    @PostMapping(value = "/power/{userId}")
    public DataResponseDto<PowerResponseDto.PowerCreateResponseDto> createPower(
            @PathVariable Long userId,
            @RequestBody PowerRequestDto.PowerCreateRequestDto powerCreateRequestDto){
        Power power = powerService.createPower(userId, powerCreateRequestDto);
        Long createdUserId = power.getUser().getUserId();
        PowerResponseDto.PowerCreateResponseDto responseDto = PowerConverter.toPowerCreateResponseDto(createdUserId);

        return DataResponseDto.of(responseDto, "전력 입력을 완료했습니다.");
    }

    @GetMapping(value = "/power/{userId}")
    public DataResponseDto<List<PowerResponseDto.PowerGetDataResponseDto>> getPowerData(
            @PathVariable Long userId,
            @RequestParam(name = "display") String display){
        List<PowerResponseDto.PowerGetDataResponseDto> powerDataList = powerService.getPowerData(userId, display);
        return DataResponseDto.of(powerDataList, "해당 유저의 전기요금 또는 전력사용량을 조회했습니다.");
    }
}
