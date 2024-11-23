package GreenSpark.greenspark.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public class PointResponseDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PointUpdateResponseDto {
        @JsonProperty("user_id")
        private Long userId;
        private LocalDate date;
        @JsonProperty("after_point")
        private int afterPoint;
        @JsonProperty("point_amount")
        private int pointAmount;
        private String event;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PointGetAllResponseDto {
        private LocalDate date;
        @JsonProperty("after_point")
        private int afterPoint;
        @JsonProperty("point_amount")
        private int pointAmount;
        private String event;
    }
}
