package GreenSpark.greenspark.domain;

import GreenSpark.greenspark.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

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

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;
}
