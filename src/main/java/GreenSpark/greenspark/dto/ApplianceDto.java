package GreenSpark.greenspark.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

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

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AppliancesHistoryResponseDto{
        private Long applianceId;
        private String previousGrade;
        private String nextGrade;
        private String matchTerm;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AppliancesHistoryResponse{
        private List<AppliancesHistoryResponseDto> history;
        private LocalDate callDate;
    }
}