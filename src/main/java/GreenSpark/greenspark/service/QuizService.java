package GreenSpark.greenspark.service;

import GreenSpark.greenspark.domain.Quiz;
import GreenSpark.greenspark.domain.UserDailyQuiz;
import GreenSpark.greenspark.domain.UserQuizRecord;
import GreenSpark.greenspark.dto.QuizResultDto;
import GreenSpark.greenspark.dto.QuizWithSolvedDto;
import GreenSpark.greenspark.repository.QuizRepository;
import GreenSpark.greenspark.repository.UserDailyQuizRepository;
import GreenSpark.greenspark.repository.UserQuizRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;
    private final UserQuizRecordRepository userQuizRecordRepository;
    private final UserDailyQuizRepository userDailyQuizRepository;

    @Scheduled(cron = "0 0 0 * * *") // 매일 정각 실행
    @Transactional
    public void refreshDailyQuizzes() {
        List<Long> allUserIds = userDailyQuizRepository.findAllUserIds(); // 모든 사용자 ID 조회
        allUserIds.forEach(userId -> refreshQuizzesForUser(userId));
}
    @Transactional
    public void refreshQuizzesForUser(Long userId) {
        // 사용자별로 퀴즈 초기화
        userDailyQuizRepository.deleteByUserId(userId);

        // 새로운 퀴즈 선택
        List<Quiz> newQuizzes = quizRepository.findQuizzesExcludingSolvedByUser(userId, Pageable.ofSize(2)).getContent();
        newQuizzes.forEach(quiz -> userDailyQuizRepository.save(
                UserDailyQuiz.builder()
                        .userId(userId)
                        .quiz(quiz)
                        .providedDate(LocalDate.now())
                        .build()
        ));
    }

    public List<QuizWithSolvedDto> getDailyQuizzes(Long userId) {
        // 오늘 제공된 퀴즈 가져오기
        List<UserDailyQuiz> todayQuizzes = userDailyQuizRepository.findByUserIdAndProvidedDate(userId, LocalDate.now());

        if (todayQuizzes.isEmpty()) {
            // 새로운 퀴즈 선택
            List<Quiz> newQuizzes = quizRepository.findQuizzesExcludingSolvedByUser(userId, Pageable.ofSize(2)).getContent();
            if (newQuizzes.isEmpty()) {
                throw new IllegalStateException("더 이상 제공할 퀴즈가 없습니다.");
            }

            // 선택된 퀴즈 기록
            newQuizzes.forEach(quiz -> userDailyQuizRepository.save(
                    UserDailyQuiz.builder()
                            .userId(userId)
                            .quiz(quiz)
                            .providedDate(LocalDate.now())
                            .build()
            ));

            todayQuizzes = newQuizzes.stream()
                    .map(quiz -> UserDailyQuiz.builder()
                            .userId(userId)
                            .quiz(quiz)
                            .providedDate(LocalDate.now())
                            .build())
                    .collect(Collectors.toList());
        }

        // 퀴즈와 유저 풀이 상태 정보 반환
        return todayQuizzes.stream()
                .map(userDailyQuiz -> {
                    Quiz quiz = userDailyQuiz.getQuiz();
                    boolean isSolved = userQuizRecordRepository.existsByUserIdAndQuiz(userId, quiz);
                    return new QuizWithSolvedDto(quiz, isSolved);
                })
                .toList();
    }

    public QuizResultDto submitQuiz(Long userId, Long quizId, String userAnswer) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("해당 퀴즈가 존재하지 않습니다."));

        boolean isCorrect = quiz.getAnswer().equals(userAnswer);

        // 퀴즈 풀이 기록 저장
        saveQuizResult(userId, quiz, userAnswer, isCorrect);

        return new QuizResultDto(quiz, userAnswer, isCorrect);
    }

    public Quiz getQuizById(Long quizId) {
        return quizRepository.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("해당 퀴즈가 존재하지 않습니다."));
    }


    private void saveQuizResult(Long userId, Quiz quiz, String userAnswer, boolean isCorrect) {
        userQuizRecordRepository.save(
                UserQuizRecord.builder()
                        .userId(userId)
                        .quiz(quiz)
                        .userAnswer(userAnswer)
                        .isCorrect(isCorrect)
                        .solved(true)
                        .build()
        );
    }
    public boolean hasUserAlreadySubmitted(Long userId, Long quizId) {
        return userQuizRecordRepository.existsByUserIdAndQuiz_QuizId(userId, quizId);
    }
}