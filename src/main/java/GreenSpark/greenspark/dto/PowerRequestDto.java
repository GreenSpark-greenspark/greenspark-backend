package GreenSpark.greenspark.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.YearMonth;

public class PowerRequestDto {

    @Getter
    @Setter
    public static class PowerCreateRequestDto{
        @NotNull
        private int year;
        @NotNull
        private int month;
        private int cost;
        private int usageAmount;
    }

    @Getter
    @Setter
    public static class PowerCreateCostRequestDto{
        @NotNull
        private int year;
        @NotNull
        private int month;
        @NotNull
        private int cost;
    }

    @Getter
    @Setter
    public static class PowerCreateUsageRequestDto{
        @NotNull
        private int year;
        @NotNull
        private int month;
        @NotNull
        private int usageAmount;
    }
}
