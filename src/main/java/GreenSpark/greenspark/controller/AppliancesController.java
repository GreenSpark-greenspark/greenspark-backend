package GreenSpark.greenspark.controller;


import GreenSpark.greenspark.response.DataResponseDto;
import GreenSpark.greenspark.service.AppliancesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AppliancesController {
    private final AppliancesService appliancesService;
    //가전제품 검색 api
    @GetMapping("/appliances/search")
    public DataResponseDto<String> searchAppliances(@RequestParam(value = "modelName") String modelName,
                                            @RequestParam(value = "equipmentName") String equipmentName) {

        String jsonResponse=appliancesService.Search_appliances_OpenAPI(modelName,equipmentName);
        return DataResponseDto.of(jsonResponse, "가전제품 검색 결과를 성공적으로 조회했습니다.");

    }

    //내 가전제품 추가하기 api
//    @PostMapping("api/appliances/{userId}/{applianceId}")
//    public ResponseEntity<String> addAppliance(@PathVariable Long userId, @PathVariable String applianceId) {
//
//    }

}