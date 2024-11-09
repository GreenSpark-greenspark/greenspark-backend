package GreenSpark.greenspark.service;
import GreenSpark.greenspark.converter.ApplicationConverter;
import GreenSpark.greenspark.domain.Appliance;
import GreenSpark.greenspark.domain.User;
import GreenSpark.greenspark.domain.enums.ApplianceCategory;
import GreenSpark.greenspark.dto.ApplianceDto;
import GreenSpark.greenspark.repository.AppliancesRepository;
import GreenSpark.greenspark.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AppliancesService {
    private final UserRepository userRepository;
    private final AppliancesRepository applianceRepository;


    @Value("${api.service-key}") String serviceKey;

    public String Search_appliances_OpenAPI(String modelName, String equipmentName) {
        String category = ApplianceCategory.getCode(equipmentName);
        StringBuilder result = new StringBuilder();
        String baseURL = "https://apis.data.go.kr/B553530/eep/";
        try {
            StringBuilder queryParams = new StringBuilder("serviceKey=" + serviceKey +
                    "&pageNo=1" +
                    "&numOfRows=10" +
                    "&apiType=json");

            if (modelName != null && !modelName.isEmpty()) {
                queryParams.append("&q2=").append(URLEncoder.encode(modelName, "UTF-8"));
            }

            String fullUrl = baseURL + category + "?" + queryParams;
            System.out.println("API 요청 URL: " + fullUrl);

            URL url = new URL(fullUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(50000);
            urlConnection.setReadTimeout(50000);

            int responseCode = urlConnection.getResponseCode();
            System.out.println("API 응답 코드: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8));
                String returnLine;
                while ((returnLine = bufferedReader.readLine()) != null) {
                    result.append(returnLine).append("\n");
                }
                bufferedReader.close();
            } else {
                System.out.println("Error: API 응답이 실패했습니다. 코드: " + responseCode);
            }
            urlConnection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    public void Add_Appliances(Long userId, ApplianceDto applianceDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

        Appliance appliance = Appliance.builder()
                .modelTerm(applianceDto.getModelTerm())
                .grade(applianceDto.getGrade())
                .matchTerm(applianceDto.getMatchTerm())
                .isUpdated(false)
                .user(user)
                .build();
        applianceRepository.save(appliance);

    }

    public List<ApplianceDto.ApplianceDataResponseDto> getUserAppliances(Long userId) {
        List<Appliance> appliances = applianceRepository.findByUser_UserId(userId);
        return appliances.stream()
                .map(appliance -> ApplianceDto.ApplianceDataResponseDto.builder()
                        .applianceId(appliance.getApplianceId()) // 가전제품 ID
                        .grade(appliance.getGrade()) // 효율 등급
                        .matchTerm(appliance.getMatchTerm()) // 기자재 명칭
                        .build())
                .collect(Collectors.toList());
    }

    public List<ApplianceDto.AppliancesHistoryResponseDto> get_Grade_Upgrade_Appliances(Long userId) {
        List<Appliance> appliances = applianceRepository.findByUser_UserId(userId);
        List<ApplianceDto.AppliancesHistoryResponseDto> updatedAppliances = appliances.stream()
                .map(appliance -> {
                    String apiResponse = Search_appliances_OpenAPI(appliance.getModelTerm(), appliance.getMatchTerm());
                    String updatedGrade = parseGradeFromApiResponse(apiResponse);
                    if (updatedGrade != null && !appliance.getGrade().equals(updatedGrade)) {
                        String previousGrade=appliance.getGrade();
                        appliance.updateGrade(updatedGrade);
                        applianceRepository.save(appliance);
                        return new ApplianceDto.AppliancesHistoryResponseDto(appliance.getApplianceId(), previousGrade, updatedGrade, appliance.getMatchTerm());
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return updatedAppliances;
    }
    private String parseGradeFromApiResponse(String apiResponse) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(apiResponse);
            JsonNode itemsNode = rootNode.path("response").path("body").path("items").path("item");

            if (itemsNode.isArray() && itemsNode.size() > 0) {
                return itemsNode.get(0).path("GRADE").asText(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
