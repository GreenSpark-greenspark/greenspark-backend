package GreenSpark.greenspark.domain;

import GreenSpark.greenspark.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Power extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usage_id")
    private Long usageId;
    @Column(nullable = false)
    private int usageAmount;
    @Column(nullable = false)
    private int cost;
    @Column(nullable = false)
    private int year;
    @Column(nullable = false)
    private int month;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;
}
