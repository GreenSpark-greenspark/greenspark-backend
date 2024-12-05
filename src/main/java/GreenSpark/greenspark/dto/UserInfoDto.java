package GreenSpark.greenspark.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfoDto {
    private String username;
    private int householdMembers;
    private int electricityDueDate;
}
