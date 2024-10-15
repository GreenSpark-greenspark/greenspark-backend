package GreenSpark.greenspark.controller;


import GreenSpark.greenspark.domain.Appliance;
import GreenSpark.greenspark.dto.ApplianceDto;
import GreenSpark.greenspark.response.DataResponseDto;
import GreenSpark.greenspark.service.AppliancesService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    public DataResponseDto<?> searchAppliances(@RequestParam(value = "modelName", required = false) String modelName,
                                               @RequestParam(value = "equipmentName", required = false) String equipmentName) {
        String jsonResponse = appliancesService.Search_appliances_OpenAPI(modelName, equipmentName);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            int totalCount = rootNode.path("response").path("body").path("totalCount").asInt();
            if (totalCount == 0) {
                return DataResponseDto.of(null, "검색 결과가 없습니다.");
            }
            return DataResponseDto.of(jsonResponse, "가전제품 검색 결과를 성공적으로 조회했습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return DataResponseDto.of(null, "데이터 처리 중 오류가 발생했습니다.");
        }
    }

    //내 가전제품 추가하기 api
// 내 가전제품 추가하기 api
    @PostMapping("/appliances/{userId}")
    public DataResponseDto<?> addAppliance(@PathVariable Long userId, @RequestBody ApplianceDto applianceDto) {
        try {
            appliancesService.Add_Appliances(userId, applianceDto);
            return DataResponseDto.of(null, "가전제품이 성공적으로 추가되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return DataResponseDto.of(null, "가전제품 추가 중 오류가 발생했습니다.");
        }
    }

}