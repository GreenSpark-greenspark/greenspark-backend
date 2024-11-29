package GreenSpark.greenspark.controller;

import GreenSpark.greenspark.domain.Quiz;
import GreenSpark.greenspark.domain.User;
import GreenSpark.greenspark.dto.QuizResultDto;
import GreenSpark.greenspark.dto.QuizWithSolvedDto;
import GreenSpark.greenspark.jwt.JWTUtil;
import GreenSpark.greenspark.repository.UserRepository;
import GreenSpark.greenspark.response.DataResponseDto;
import GreenSpark.greenspark.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class QuizController {

    private final QuizService quizService;
    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;

    // 매일 퀴즈 가져오기
    @GetMapping("/quiz")
    public DataResponseDto<?> getDailyQuizzes(@CookieValue("access") String authorization) {
        try {
            long userId = getUserId(authorization);
            List<QuizWithSolvedDto> quizzes = quizService.getDailyQuizzes(userId);
            return DataResponseDto.of(quizzes, "매일 퀴즈를 성공적으로 가져왔습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return DataResponseDto.of(null, "퀴즈 데이터를 가져오는 중 오류가 발생했습니다.");
        }
    }

    // 특정 퀴즈 보기
    @GetMapping("/quiz/{quizId}")
    public DataResponseDto<?> getQuiz(@PathVariable Long quizId) {
        try {
            Quiz quiz = quizService.getQuizById(quizId);
            return DataResponseDto.of(quiz, "퀴즈 세부 정보를 성공적으로 가져왔습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return DataResponseDto.of(null, "퀴즈 가져오는 중 오류가 발생니다.");
        }
    }

    // 퀴즈 제출
    @PostMapping("/submit")
    public DataResponseDto<?> submitQuiz(
            @CookieValue("access") String authorization,
            @RequestParam Long quizId,
            @RequestParam String userAnswer) {
        try {
            long userId = getUserId(authorization);
            QuizResultDto result = quizService.submitQuiz(userId, quizId, userAnswer);
            return DataResponseDto.of(result, "퀴즈가 성공적으로 제출되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return DataResponseDto.of(null, "퀴즈 제출 중 오류가 발생했습니다.");
        }
    }

    private long getUserId(String authorization) {
        String token = authorization.replace("Bearer ", "");
        String username = jwtUtil.getUsername(token);
        User user = userRepository.findByUsername(username);
        return user.getUserId();
    }
}