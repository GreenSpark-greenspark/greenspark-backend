package GreenSpark.greenspark.domain;

import GreenSpark.greenspark.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Point extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pointId")
    private Long pointId;
    @Column(nullable = false)
    private LocalDate date;
    @Column(nullable = false, name = "after_point")
    private int afterPoint;
    @Column(nullable = false, name = "point_amount")
    private int pointAmount;
    @Column(nullable = false)
    private String event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
