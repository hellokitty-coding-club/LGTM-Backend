package swm.hkcc.LGTM.app.modules.mission.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import swm.hkcc.LGTM.app.modules.serverDrivenUI.ServerDrivenUISequenceByVersion;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Getter
@RequiredArgsConstructor
public enum HomeServerDrivenUISequenceByVersion implements ServerDrivenUISequenceByVersion {
    V1_HOME_SERVER_DRIVEN_UI_SEQUENCE(
            1,
            List.of(MissionContentType.ON_GOING_MISSION_TITLE,
                    MissionContentType.ON_GOING_MISSION_LIST,
                    MissionContentType.SECTION_CLOSER,
                    MissionContentType.RECOMMENDED_MISSION_TITLE,
                    MissionContentType.RECOMMENDED_MISSION_LIST,
                    MissionContentType.SECTION_CLOSER,
                    MissionContentType.TOTAL_MISSION_TITLE,
                    MissionContentType.TOTAL_MISSION_LIST
                    )),

    V2_HOME_SERVER_DRIVEN_UI_SEQUENCE(
            2,
            List.of(MissionContentType.RECOMMENDED_MISSION_TITLE,
            MissionContentType.RECOMMENDED_MISSION_LIST,
            MissionContentType.SECTION_CLOSER,
            MissionContentType.ON_GOING_MISSION_TITLE,
            MissionContentType.ON_GOING_MISSION_LIST,
            MissionContentType.SECTION_CLOSER,
            MissionContentType.TOTAL_MISSION_TITLE,
            MissionContentType.TOTAL_MISSION_LIST
            ));

    private final int version;

    private final List<MissionContentType> components;

    public static Optional<HomeServerDrivenUISequenceByVersion> findByVersion(int version) {
        return Arrays.stream(HomeServerDrivenUISequenceByVersion.values())
                .filter(value -> value.version == version)
                .findFirst();
    }
}
