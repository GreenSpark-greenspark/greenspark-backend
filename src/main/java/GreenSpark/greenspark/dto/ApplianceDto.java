package GreenSpark.greenspark.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplianceDto {
    private String modelTerm;
    private String grade;
    private String matchTerm;
    private String manufacturer;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ApplianceDataResponseDto {
        private Long applianceId;  // 가전제품 ID 추가
        private String grade;
        private String matchTerm;
    }
}