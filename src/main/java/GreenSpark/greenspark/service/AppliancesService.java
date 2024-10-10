package GreenSpark.greenspark.service;
import GreenSpark.greenspark.domain.enums.ApplianceCategory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class AppliancesService {

    public String Search_appliances_OpenAPI(String modelName, String equipmentName, String companyName) {
        String category = ApplianceCategory.getCode(equipmentName);
        StringBuilder result = new StringBuilder();
        String baseURL = "https://apis.data.go.kr/B553530/eep/";
        String serviceKey = "eAJwVNZRoCSnmJha4K8km53UJWoFBcif8T%2BxkvwxqAYgJp3u8UuGp4bEFP5pafKJYVbw%2FuMYQV71NxEcOTUZbg%3D%3D";

        try {
            StringBuilder queryParams = new StringBuilder("serviceKey=" + serviceKey +
                    "&pageNo=1" +
                    "&numOfRows=10" +
                    "&apiType=json");

            if (modelName != null && !modelName.isEmpty()) {
                queryParams.append("&q2=").append(URLEncoder.encode(modelName, "UTF-8"));
            }
            if (companyName != null && !companyName.isEmpty()) {
                queryParams.append("&q3=").append(URLEncoder.encode(companyName, "UTF-8"));
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
}