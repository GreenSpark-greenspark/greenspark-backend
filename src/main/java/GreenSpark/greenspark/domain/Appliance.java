package GreenSpark.greenspark.domain;

import GreenSpark.greenspark.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Appliance extends BaseEntity {
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

    private LocalDate updateDate;
    private Boolean isUpdated = false;

    public void updateGrade(String newGrade) {
        this.grade = newGrade;
        this.updateDate = LocalDate.now();
        this.isUpdated = true;
    }
}
