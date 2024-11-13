package GreenSpark.greenspark.controller;


import GreenSpark.greenspark.domain.Appliance;
import GreenSpark.greenspark.domain.User;
import GreenSpark.greenspark.dto.ApplianceDto;
import GreenSpark.greenspark.repository.AppliancesRepository;
import GreenSpark.greenspark.response.DataResponseDto;
import GreenSpark.greenspark.service.AppliancesService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AppliancesController {
    private final AppliancesService appliancesService;
    private final AppliancesRepository appliancesRepository;
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
    @PostMapping("/appliances/{userId}")
    public DataResponseDto<?> addAppliance(@PathVariable Long userId, @RequestBody ApplianceDto applianceDto) {
        try {
            appliancesService.Add_Appliances(userId, applianceDto);
            return DataResponseDto.of(null, "가전제품이 성공적으로 추가되었습니다.");
        } catch (IllegalArgumentException e) {
            if (e.getMessage().equals("이미 존재하는 가전제품입니다.")) {
                return DataResponseDto.of(null, "이미 존재하는 가전제품입니다.");
            }
            return DataResponseDto.of(null, "잘못된 사용자 ID입니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return DataResponseDto.of(null, "가전제품 추가 중 오류가 발생했습니다.");
        }
    }
    //내 가전제품 상세보기 api
    @GetMapping("/appliances/detail/{applianceId}")
    public DataResponseDto<?> getApplianceDetail(@PathVariable Long applianceId) {
        Appliance appliance = appliancesRepository.findById(applianceId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid applianceId"));
        String modelName = appliance.getModelTerm();
        String equipmentName = appliance.getMatchTerm();
        String jsonResponse = appliancesService.Search_appliances_OpenAPI(modelName, equipmentName);
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            int totalCount = rootNode.path("response").path("body").path("totalCount").asInt();

            if (totalCount == 0) {
                return DataResponseDto.of(null, "검색 결과가 없습니다.");
            }
            JsonNode itemsNode = rootNode.path("response").path("body").path("items").path("item");
            ArrayNode filteredItems = objectMapper.createArrayNode();

            for (JsonNode itemNode : itemsNode) {
                String itemModelName = itemNode.path("MODEL_TERM").asText();
                if (modelName.equals(itemModelName)) {
                    filteredItems.add(itemNode);
                }
            }
            ObjectNode responseNode = objectMapper.createObjectNode();
            responseNode.set("items", filteredItems);

            if (filteredItems.isEmpty()) {
                return DataResponseDto.of(null, "검색 결과가 없습니다.");
            }

            return DataResponseDto.of(responseNode.toString(), "가전제품 상세보기를 조회했습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return DataResponseDto.of(null, "데이터 처리 중 오류가 발생했습니다.");
        }
    }
    //내 가전제품 목록보기 api
    @GetMapping("/appliances/{userId}")
    public DataResponseDto<?> getAllAppliances(@PathVariable Long userId) {
        List<ApplianceDto.ApplianceDataResponseDto> userappliances=appliancesService.getUserAppliances(userId);
        return DataResponseDto.of(userappliances,"가전제품 목록을 조회했습니다.");
    }

    @GetMapping("/appliances/history/{userId}")
    public DataResponseDto<?> getApplianceHistory(@PathVariable Long userId) {
        LocalDate today=LocalDate.now();
        if(today.getDayOfMonth()==1){
            List<ApplianceDto.AppliancesHistoryResponseDto> history=appliancesService.get_Grade_Upgrade_Appliances(userId);
            return DataResponseDto.of(new ApplianceDto.AppliancesHistoryResponse(history, today),"가전제품 히스토리 목록을 조회했습니다.");
        }
        return DataResponseDto.of(null,"효율등급이 변경된 히스토리 목록이 없습니다.");
    }

@GetMapping("/appliances/preview/{userId}")
public DataResponseDto<?> getRecentlyUpdatedAppliances(@PathVariable Long userId) {
    List<Appliance> updatedAppliances = appliancesRepository.findTop3ByUser_UserIdAndIsUpdatedOrderByUpdateDateDesc(userId, true);
    List<Appliance> allAppliances = appliancesRepository.findByUser_UserId(userId);
    List<Appliance> nonUpdatedAppliances = allAppliances.stream()
            .filter(appliance -> !updatedAppliances.contains(appliance))
            .collect(Collectors.toList());

    List<ApplianceDto.ApplianceDataResponseDto> resultDtos = new ArrayList<>();

    if (!updatedAppliances.isEmpty()) {
        resultDtos.addAll(updatedAppliances.stream()
                .map(appliance -> ApplianceDto.ApplianceDataResponseDto.builder()
                        .applianceId(appliance.getApplianceId())
                        .grade(appliance.getGrade())
                        .matchTerm(appliance.getMatchTerm())
                        .isUpdated(appliance.getIsUpdated())
                        .build())
                .collect(Collectors.toList()));
        resultDtos.addAll(nonUpdatedAppliances.stream()
                .sorted(Comparator.comparing(Appliance::getApplianceId))
                .limit(3 - updatedAppliances.size())
                .map(appliance -> ApplianceDto.ApplianceDataResponseDto.builder()
                        .applianceId(appliance.getApplianceId())
                        .grade(appliance.getGrade())
                        .matchTerm(appliance.getMatchTerm())
                        .isUpdated(appliance.getIsUpdated())
                        .build())
                .collect(Collectors.toList()));
    } else {
        resultDtos.addAll(nonUpdatedAppliances.stream()
                .sorted(Comparator.comparing(Appliance::getApplianceId))
                .limit(3)
                .map(appliance -> ApplianceDto.ApplianceDataResponseDto.builder()
                        .applianceId(appliance.getApplianceId())
                        .grade(appliance.getGrade())
                        .matchTerm(appliance.getMatchTerm())
                        .isUpdated(appliance.getIsUpdated())
                        .build())
                .collect(Collectors.toList()));
    }
    return DataResponseDto.of(resultDtos, "가전제품 미리보기가 조회되었습니다.");
}
}