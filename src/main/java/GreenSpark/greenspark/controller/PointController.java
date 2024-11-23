package GreenSpark.greenspark.controller;

import GreenSpark.greenspark.dto.PointRequestDto;
import GreenSpark.greenspark.dto.PointResponseDto;
import GreenSpark.greenspark.response.DataResponseDto;
import GreenSpark.greenspark.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class PointController {

    private final PointService pointService;

    @GetMapping(value = "/point")
    public DataResponseDto<Integer> getTotalPoint(
            @CookieValue("access") String authorization){
        int totalPoint = pointService.getTotalPoint(authorization);

        return DataResponseDto.of(totalPoint, "사용 가능 포인트 조회를 완료했습니다.");
    }

    @PostMapping(value = "/point/update")
    public DataResponseDto<PointResponseDto.PointUpdateResponseDto> updatePoint(
            @CookieValue("access") String authorization,
            @RequestBody PointRequestDto.PointUpdateRequestDto pointUpdateRequestDto){
        PointResponseDto.PointUpdateResponseDto responseDto = pointService.updatePoint(authorization, pointUpdateRequestDto);

        return DataResponseDto.of(responseDto, "포인트 갱신을 완료했습니다.");
    }

    @GetMapping(value = "/point/history")
    public DataResponseDto<List<PointResponseDto.PointGetAllResponseDto>> getAllPoint(
            @CookieValue("access") String authorization){
        List<PointResponseDto.PointGetAllResponseDto> pointList = pointService.getAllPoint(authorization);

        return DataResponseDto.of(pointList, "포인트 내역을 조회했습니다.");
    }
}
