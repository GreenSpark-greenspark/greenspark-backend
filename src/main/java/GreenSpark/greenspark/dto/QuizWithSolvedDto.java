package GreenSpark.greenspark.dto;

import GreenSpark.greenspark.domain.Quiz;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QuizWithSolvedDto {
    private Quiz quiz;
    private boolean solved;
}