package GreenSpark.greenspark.controller;

import GreenSpark.greenspark.converter.PointConverter;
import GreenSpark.greenspark.dto.PointResponseDto;
import GreenSpark.greenspark.response.DataResponseDto;
import GreenSpark.greenspark.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class PointController {

    private final PointService pointService;

    @GetMapping(value = "/point/{userId}")
    public DataResponseDto<PointResponseDto.PointGetResponseDto> getTotalPoint(
            @PathVariable Long userId){
        int totalPoint = pointService.getTotalPoint(userId);
        PointResponseDto.PointGetResponseDto responseDto = PointConverter.toPointGetResponseDto(userId, totalPoint);

        return DataResponseDto.of(responseDto, "해당 유저의 사용 가능 포인트 조회를 완료했습니다.");
    }
}
