package swm.hkcc.LGTM.app.modules.serverDrivenUI;

public enum ViewType {
    SECTION_TITLE("sectionTitle"),

    ON_GOING_MISSION_LIST("onGoingMissionList"),

    RECOMMENDED_MISSION_LIST("recommendedMissionList"),

    TOTAL_MISSION_LIST("totalMissionList"),

    BLANK("blank");

    private final String name;

    ViewType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
