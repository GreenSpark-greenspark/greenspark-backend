package GreenSpark.greenspark.domain.enums;

import lombok.Getter;

@Getter
public enum ApplianceCategory {
    WASHING_MACHINE("EEP_01_LIST", "전기세탁기(일반)"),
    VACUUM_CLEANER("EEP_05_LIST", "전기진공청소기"),
    DRUM_WASHING_MACHINE("EEP_06_LIST", "전기세탁기(드럼)"),
    FAN("EEP_07_LIST", "선풍기"),
    AIR_PURIFIER("EEP_08_LIST", "공기청정기 (~24.12.31)"),
    KIMCHI_FRIDGE("EEP_13_LIST", "김치냉장고"),
    WATER_PURIFIER("EEP_16_LIST", "전기냉온수기"),
    TELEVISION("EEP_17_LIST","텔레비전수상기"),
    DEHUMIDIFIER("EEP_19_LIST","제습기"),
    ELECTRIC_REFREIGERATOR("EEP_20_LIST","전기냉장고"),
    ELECTRIC_AIR_CONDITIONER_1("EEP_28_LIST","전기냉난방기(~2018.10.01 이전)"),
    ELECTRIC_AIR_CONDITIONER_2("EEP_28_LIST","전기냉난방기");




    private final String code;
    private final String equipmentName;

    ApplianceCategory(String code, String equipmentName) {
        this.code = code;
        this.equipmentName =equipmentName;
    }


    public static String getCode(String equipmentName) {
        for (ApplianceCategory applianceCategory : ApplianceCategory.values()) {
            if (applianceCategory.getEquipmentName().equals(equipmentName)) {
                return applianceCategory.code;
            }
        }
        throw new IllegalArgumentException("기자재 명칭과 맞는 code를 찾을 수 없습니다.");
    }
}