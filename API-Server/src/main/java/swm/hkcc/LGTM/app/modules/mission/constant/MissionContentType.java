package swm.hkcc.LGTM.app.modules.mission.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import swm.hkcc.LGTM.app.modules.serverDrivenUI.ViewType;

@Getter
@RequiredArgsConstructor
public enum MissionContentType {
    ON_GOING_MISSION_TITLE(ViewType.TITLE),

    ON_GOING_MISSION_LIST(ViewType.ITEM),

    RECOMMENDED_MISSION_TITLE(ViewType.TITLE),

    RECOMMENDED_MISSION_LIST(ViewType.ITEM),

    TOTAL_MISSION_TITLE(ViewType.TITLE),

    TOTAL_MISSION_LIST(ViewType.ITEM),

    SECTION_DARK_CLOSER(ViewType.CLOSER),

    SECTION_LIGHT_CLOSER(ViewType.CLOSER);

    private final ViewType viewType;

}
