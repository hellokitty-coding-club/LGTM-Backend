package swm.hkcc.LGTM.app.modules.mission.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import swm.hkcc.LGTM.app.modules.serverDrivenUI.ServerDrivenUISequenceByVersion;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static swm.hkcc.LGTM.app.modules.mission.constant.MissionContentType.*;

@Getter
public enum HomeServerDrivenUISequenceByVersion implements ServerDrivenUISequenceByVersion {
    A_V2_HOME_SERVER_DRIVEN_UI_SEQUENCE(
            "A",
            2,
            List.of(ON_GOING_MISSION_TITLE_V1,
                    ON_GOING_MISSION_LIST_V1,
                    SECTION_LIGHT_CLOSER_V1,
                    MISSION_SUGGESTION_TITLE_V1,
                    MISSION_SUGGESTION_BODY_V1,
                    SECTION_DARK_CLOSER_V1,
                    RECOMMENDED_MISSION_TITLE_V1,
                    RECOMMENDED_MISSION_LIST_V1,
                    SECTION_DARK_CLOSER_V1,
                    HOT_MISSION_TITLE_V1,
                    HOT_MISSION_LIST_V1,
                    SECTION_DARK_CLOSER_V1,
                    TOTAL_MISSION_TITLE_V1,
                    TOTAL_MISSION_LIST_V1,
                    SECTION_DARK_CLOSER_V1
            )),

    B_V2_HOME_SERVER_DRIVEN_UI_SEQUENCE(
            "B",
            2,
            List.of(ON_GOING_MISSION_TITLE_V1,
                    ON_GOING_MISSION_LIST_V1,
                    SECTION_LIGHT_CLOSER_V1,
                    MISSION_SUGGESTION_TITLE_V1,
                    MISSION_SUGGESTION_BODY_V1,
                    SECTION_DARK_CLOSER_V1,
                    HOT_MISSION_TITLE_V1,
                    HOT_MISSION_LIST_V1,
                    SECTION_DARK_CLOSER_V1,
                    RECOMMENDED_MISSION_TITLE_V1,
                    RECOMMENDED_MISSION_LIST_V1,
                    SECTION_DARK_CLOSER_V1,
                    TOTAL_MISSION_TITLE_V1,
                    TOTAL_MISSION_LIST_V1,
                    SECTION_DARK_CLOSER_V1
            )),

    A_HOME_SERVER_DRIVEN_UI_SEQUENCE(
            "A",
            List.of(ON_GOING_MISSION_TITLE_V1,
                    ON_GOING_MISSION_LIST_V1,
                    SECTION_LIGHT_CLOSER_V1,
                    RECOMMENDED_MISSION_TITLE_V1,
                    RECOMMENDED_MISSION_LIST_V1,
                    SECTION_DARK_CLOSER_V1,
                    HOT_MISSION_TITLE_V1,
                    HOT_MISSION_LIST_V1,
                    SECTION_DARK_CLOSER_V1,
                    TOTAL_MISSION_TITLE_V1,
                    TOTAL_MISSION_LIST_V1,
                    SECTION_DARK_CLOSER_V1
            )),

    B_HOME_SERVER_DRIVEN_UI_SEQUENCE(
            "B",
            List.of(ON_GOING_MISSION_TITLE_V1,
                    ON_GOING_MISSION_LIST_V1,
                    SECTION_LIGHT_CLOSER_V1,
                    HOT_MISSION_TITLE_V1,
                    HOT_MISSION_LIST_V1,
                    SECTION_DARK_CLOSER_V1,
                    RECOMMENDED_MISSION_TITLE_V1,
                    RECOMMENDED_MISSION_LIST_V1,
                    SECTION_DARK_CLOSER_V1,
                    TOTAL_MISSION_TITLE_V1,
                    TOTAL_MISSION_LIST_V1,
                    SECTION_DARK_CLOSER_V1
            ));

    private final String ABTestGroupName;
    private final int version;

    private final List<MissionContentType> contents;

    public static Optional<HomeServerDrivenUISequenceByVersion> find(String ABTestGroupName) {
        return Arrays.stream(HomeServerDrivenUISequenceByVersion.values())
                .filter(value -> Objects.equals(value.ABTestGroupName, ABTestGroupName))
                .filter(value -> value.version == 1)
                .findFirst();
    }

    public static Optional<HomeServerDrivenUISequenceByVersion> find(String ABTestGroupName, int version) {
        return Arrays.stream(HomeServerDrivenUISequenceByVersion.values())
                .filter(value -> Objects.equals(value.ABTestGroupName, ABTestGroupName))
                .filter(value -> value.version == version)
                .findFirst();
    }

    HomeServerDrivenUISequenceByVersion(String ABTestGroupName, int version, List<MissionContentType> contents) {
        this.ABTestGroupName = ABTestGroupName;
        this.version = version;
        this.contents = contents;
    }

    HomeServerDrivenUISequenceByVersion(String ABTestGroupName, List<MissionContentType> contents) {
        this.version = 1;
        this.ABTestGroupName = ABTestGroupName;
        this.contents = contents;
    }
}
