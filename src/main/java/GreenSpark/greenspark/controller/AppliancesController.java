package GreenSpark.greenspark.controller;


import GreenSpark.greenspark.service.AppliancesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AppliancesController {
    private final AppliancesService appliancesService;
    //가전제품 검색 api
    @GetMapping("/appliances/search")
    public String searchAppliances(@RequestParam(value = "modelName") String modelName,
                                   @RequestParam(value = "equipmentName") String equipmentName) {

        return appliancesService.Search_appliances_OpenAPI(modelName,equipmentName);
    }
}