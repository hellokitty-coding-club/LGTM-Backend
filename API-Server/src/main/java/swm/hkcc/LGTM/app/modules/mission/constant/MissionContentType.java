package swm.hkcc.LGTM.app.modules.mission.constant;

import lombok.Getter;

import swm.hkcc.LGTM.app.modules.serverDrivenUI.Theme;
import swm.hkcc.LGTM.app.modules.serverDrivenUI.ViewType;

@Getter
public enum MissionContentType {

    ON_GOING_MISSION_TITLE_V1(ViewType.TITLE, Theme.DARK, "진행 중인 미션"),

    ON_GOING_MISSION_LIST_V1(ViewType.ITEM, Theme.DARK, 3),

    RECOMMENDED_MISSION_TITLE_V1(ViewType.TITLE, Theme.LIGHT, "맞춤 추천 미션 목록이에요"),

    RECOMMENDED_MISSION_LIST_V1(ViewType.ITEM, Theme.LIGHT, 3),

    TOTAL_MISSION_TITLE_V1(ViewType.TITLE, Theme.LIGHT, "더 많은 미션 찾아보기"),

    TOTAL_MISSION_LIST_V1(ViewType.ITEM, Theme.LIGHT, 5),

    SECTION_DARK_CLOSER_V1(ViewType.CLOSER, Theme.DARK),

    SECTION_LIGHT_CLOSER_V1(ViewType.CLOSER, Theme.LIGHT);

    private final ViewType viewType;
    private final Theme theme;
    private String titleName;
    private int maxItemCount;

    MissionContentType(ViewType viewType, Theme theme) {
        this.viewType = viewType;
        this.theme = theme;
    }

    MissionContentType(ViewType viewType, Theme theme, String titleName) {
        this(viewType, theme);
        this.titleName = titleName;
    }

    MissionContentType(ViewType viewType, Theme theme, int maxItemCount) {
        this(viewType, theme);
        this.maxItemCount = maxItemCount;
    }
}
