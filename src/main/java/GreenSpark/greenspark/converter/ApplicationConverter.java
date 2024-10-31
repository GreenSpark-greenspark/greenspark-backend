package GreenSpark.greenspark.converter;

import GreenSpark.greenspark.domain.Appliance;
import GreenSpark.greenspark.dto.ApplianceDto;

public class ApplicationConverter {
    public static ApplianceDto.ApplianceDataResponseDto toApplianceDataResponseDto(Appliance appliance) {
        return new ApplianceDto.ApplianceDataResponseDto(
                appliance.getApplianceId(),
                appliance.getGrade(),
                appliance.getMatchTerm(),
                appliance.getIsUpdated()
        );
    }
}
