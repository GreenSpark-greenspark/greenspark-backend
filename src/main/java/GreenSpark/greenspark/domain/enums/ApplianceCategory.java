package GreenSpark.greenspark.domain.enums;

public enum ApplianceCategory {
    WASHING_MACHINE("EEP_01_LIST", "전기세탁기(일반)"),
    VACUUM_CLEANER("EEP_05_LIST", "전기진공청소기"),
    DRUM_WASHING_MACHINE("EEP_06_LIST", "전기세탁기(드럼)"),
    FAN("EEP_07_LIST", "선풍기"),
    AIR_PURIFIER("EEP_08_LIST", "공기청정기"),
    RICE_COOKER("EEP_11_LIST", "전기밥솥"),
    KIMCHI_FRIDGE("EEP_13_LIST", "김치냉장고"),
    HEATER("EEP_14_LIST", "전기온풍기"),
    ELECTRIC_STOVE("EEP_15_LIST", "전기스토브"),
    WATER_PURIFIER("EEP_16_LIST", "전기냉온수기");

    private final String code;
    private final String equipmentName;

    ApplianceCategory(String code, String equipmentName) {
        this.code = code;
        this.equipmentName =equipmentName;
    }

    public String getCode() {
        return code;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public static String getCode(String equipmentName) {
        for (ApplianceCategory applianceCategory : ApplianceCategory.values()) {
            if (applianceCategory.getEquipmentName().equals(equipmentName)) {
                return applianceCategory.code;
            }
        }
        throw new IllegalArgumentException("기자재 명칭과 맞는 code를 찾을 수 없습니다.");
    }

    public static ApplianceCategory fromCode(String code) {
        for (ApplianceCategory category : values()) {
            if (category.getCode().equalsIgnoreCase(code)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Unknown category code: " + code);
    }
}