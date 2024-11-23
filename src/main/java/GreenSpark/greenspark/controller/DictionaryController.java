package GreenSpark.greenspark.controller;

import GreenSpark.greenspark.dto.DictionaryResponseDto;
import GreenSpark.greenspark.dto.PointResponseDto;
import GreenSpark.greenspark.response.DataResponseDto;
import GreenSpark.greenspark.service.DictionaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class DictionaryController {

    private final DictionaryService dictionaryService;

    @GetMapping(value = "/dictionary/{categoryId}")
    public DataResponseDto<List<DictionaryResponseDto.EnergyTipsGetAllResponseDto>> getAllEnergyTips(
            @PathVariable Long categoryId) {
        List<DictionaryResponseDto.EnergyTipsGetAllResponseDto> tipList = dictionaryService.getAllEnergyTips(categoryId);

        return DataResponseDto.of(tipList, "해당 카테고리의 팁들을 조회했습니다.");
    }
}
