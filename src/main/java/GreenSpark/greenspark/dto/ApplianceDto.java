package GreenSpark.greenspark.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
class ApplianceDto {
    private String modelTerm;
    private String grade;
    private String matchTerm;
    private String manufacturer;
}