package GreenSpark.greenspark.domain;

import GreenSpark.greenspark.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserQuizRecord extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private Long recordId;

    @Column(nullable = false)
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @Column(nullable = false)
    private String userAnswer;

    @Column(nullable = false)
    private boolean isCorrect;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean solved;

    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean isActive;
}