package GreenSpark.greenspark.controller;


import GreenSpark.greenspark.service.AppliancesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AppliancesController {
    private final AppliancesService appliancesService;
    //가전제품 검색 api
    @GetMapping("/appliances/search")
    public String searchAppliances(@RequestParam(value = "modelName", required = false) String modelName,
                                   @RequestParam(value = "equipmentName", required = false) String equipmentName,
                                   @RequestParam(value = "companyName", required = false) String companyName) {
//        if (modelName == null && equipmentName == null && companyName == null) {
//            //셋 중 하나라도 입력 안받았으면 에러처리
//        }
        return appliancesService.Search_appliances_OpenAPI(modelName,equipmentName,companyName);
    }
}
