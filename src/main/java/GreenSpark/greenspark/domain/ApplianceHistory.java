package GreenSpark.greenspark.domain;

import GreenSpark.greenspark.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ApplianceHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "applianceHistory_id")
    private Long applianceHistoryId;
    @Column(nullable = false)
    private String message;
    @Column(nullable = false)
    private Date chageDate;
    @Column(nullable = false)
    private String applianceName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appliance_id")
    private Appliance appliance;
}
