package swm.hkcc.LGTM.app.modules.mission.constant;

import lombok.RequiredArgsConstructor;
import swm.hkcc.LGTM.app.modules.serverDrivenUI.ServerDrivenUISequenceByVersion;
import swm.hkcc.LGTM.app.modules.serverDrivenUI.ViewType;

import java.util.List;

@RequiredArgsConstructor
public enum HomeServerDrivenUISequenceByVersion implements ServerDrivenUISequenceByVersion {
    V1_HOME_SERVER_DRIVEN_UI_SEQUENCE(
            1,
            List.of(ViewType.SECTION_TITLE,
                    ViewType.ON_GOING_MISSION_LIST,
                    ViewType.BLANK,
                    ViewType.SECTION_TITLE,
                    ViewType.RECOMMENDED_MISSION_LIST,
                    ViewType.BLANK,
                    ViewType.SECTION_TITLE,
                    ViewType.TOTAL_MISSION_LIST
                    )),

    V2_HOME_SERVER_DRIVEN_UI_SEQUENCE(
            2,
            List.of(ViewType.SECTION_TITLE,
            ViewType.RECOMMENDED_MISSION_LIST,
            ViewType.BLANK,
            ViewType.SECTION_TITLE,
            ViewType.ON_GOING_MISSION_LIST,
            ViewType.BLANK,
            ViewType.SECTION_TITLE,
            ViewType.TOTAL_MISSION_LIST
            ));

    private final int version;

    private final List<ViewType> components;

}
