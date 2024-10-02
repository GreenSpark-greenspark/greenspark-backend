package GreenSpark.greenspark.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_id")
    private Long quizId;
    @Column(nullable = false)
    private String question;
    @Column(nullable = false)
    private String choice1;
    @Column(nullable = false)
    private String choice2;
    @Column(nullable = false)
    private String choice3;
    @Column(nullable = false)
    private String choice4;
    @Column(nullable = false)
    private String answer;

}
