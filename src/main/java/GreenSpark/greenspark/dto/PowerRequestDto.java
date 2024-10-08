package GreenSpark.greenspark.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.YearMonth;

public class PowerRequestDto {

    @ToString
    @Getter
    @Setter
    public static class PowerCreateRequestDto{
        @NotNull
        private int year;
        @NotNull
        private int month;
        @NotNull
        private int cost;
        @NotNull
        private int usageAmount;

    }
}
