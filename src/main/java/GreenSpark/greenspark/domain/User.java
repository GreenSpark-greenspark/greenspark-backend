package GreenSpark.greenspark.domain;

import GreenSpark.greenspark.common.BaseEntity;
import GreenSpark.greenspark.dto.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String email;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
    @Column(nullable = true)
    private String password;
    @Column( name = "household_members")
    private int householdMembers;
    @Column( name = "electricity_due_date")
    private int electricityDueDate;
    @Column(name = "total_point")
    private int totalPoint;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Appliance> applianceList = new ArrayList<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ApplianceHistory> applianceHistoryList = new ArrayList<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Power> powerList = new ArrayList<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Point> pointList = new ArrayList<>();

}

