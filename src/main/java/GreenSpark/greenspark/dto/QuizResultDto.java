package GreenSpark.greenspark.dto;

import GreenSpark.greenspark.domain.Quiz;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QuizResultDto {
    private final String question;
    private final String userAnswer;
    private final String correctAnswer;
    private final String explanation;
    private final boolean isCorrect;

    public QuizResultDto(Quiz quiz, String userAnswer, boolean isCorrect) {
        this.question = quiz.getQuestion();
        this.userAnswer = userAnswer;
        this.correctAnswer = quiz.getAnswer();
        this.explanation = quiz.getExplanation();
        this.isCorrect = isCorrect;
    }
}