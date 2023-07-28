package swm.hkcc.LGTM.app.modules.mission.constant;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import swm.hkcc.LGTM.app.modules.serverDrivenUI.ServerDrivenUISequenceByVersion;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static swm.hkcc.LGTM.app.modules.mission.constant.MissionContentType.*;

@Getter
@RequiredArgsConstructor
public enum HomeServerDrivenUISequenceByVersion implements ServerDrivenUISequenceByVersion {
    V1_HOME_SERVER_DRIVEN_UI_SEQUENCE(
            1,
            List.of(ON_GOING_MISSION_TITLE,
                    ON_GOING_MISSION_LIST,
                    SECTION_DARK_CLOSER,
                    RECOMMENDED_MISSION_TITLE,
                    RECOMMENDED_MISSION_LIST,
                    SECTION_LIGHT_CLOSER,
                    TOTAL_MISSION_TITLE,
                    TOTAL_MISSION_LIST,
                    SECTION_LIGHT_CLOSER
            )),

    V2_HOME_SERVER_DRIVEN_UI_SEQUENCE(
            2,
            List.of(RECOMMENDED_MISSION_TITLE,
                    RECOMMENDED_MISSION_LIST,
                    SECTION_DARK_CLOSER,
                    ON_GOING_MISSION_TITLE,
                    ON_GOING_MISSION_LIST,
                    SECTION_LIGHT_CLOSER,
                    TOTAL_MISSION_TITLE,
                    TOTAL_MISSION_LIST,
                    SECTION_LIGHT_CLOSER
            ));

    private final int version;

    private final List<MissionContentType> contents;

    public static Optional<HomeServerDrivenUISequenceByVersion> findByVersion(int version) {
        return Arrays.stream(HomeServerDrivenUISequenceByVersion.values())
                .filter(value -> value.version == version)
                .findFirst();
    }
}
