package GreenSpark.greenspark.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

public class PowerResponseDto {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PowerCreateResponseDto {
        @JsonProperty("user_id")
        Long userId;
    }

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    public static class PowerGetDataResponseDto {
        private int year;
        private int month;
        private int value;
    }

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    public static class PowerGetAllResponseDto {
        private int year;
        private int month;
        private int cost;
        @JsonProperty("usage_amount")
        private int usageAmount;
    }
}
