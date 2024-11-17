package GreenSpark.greenspark.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PointResponseDto {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PointGetResponseDto {
        @JsonProperty("user_id")
        Long userId;
        @JsonProperty("total_point")
        int totalPoint;
    }
}
