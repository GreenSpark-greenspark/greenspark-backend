package GreenSpark.greenspark.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public class PointRequestDto {
    @Getter
    @Setter
    public static class PointUpdateRequestDto{
        private int pointAmount;
        private String event;
    }
}
