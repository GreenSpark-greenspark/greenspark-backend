package GreenSpark.greenspark.service;
import GreenSpark.greenspark.domain.Appliance;
import GreenSpark.greenspark.domain.User;
import GreenSpark.greenspark.domain.enums.ApplianceCategory;
import GreenSpark.greenspark.dto.ApplianceDto;
import GreenSpark.greenspark.repository.AppliancesRepository;
import GreenSpark.greenspark.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

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
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);

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
                .user(user)
                .build();
        applianceRepository.save(appliance);

    }
}
