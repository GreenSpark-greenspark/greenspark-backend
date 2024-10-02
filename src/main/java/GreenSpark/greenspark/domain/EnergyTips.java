package GreenSpark.greenspark.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class EnergyTips {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "energyTips_id")
    private Long energyTipsId;
    @Column(nullable = false)
    private String tipContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
}
