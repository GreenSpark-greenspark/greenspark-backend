package GreenSpark.greenspark.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Appliance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appliance_id")
    private Long applianceId;
    @Column(nullable = false)
    private String modelTerm;
    @Column(nullable = false)
    private String grade;
    @Column(nullable = false)
    private String matchTerm;

    @OneToMany(mappedBy = "appliance", cascade = CascadeType.ALL)
    private List<ApplianceHistory> applianceHistoryList = new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
