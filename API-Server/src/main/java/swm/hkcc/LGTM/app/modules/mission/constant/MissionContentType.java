package swm.hkcc.LGTM.app.modules.mission.constant;

import lombok.Getter;

import swm.hkcc.LGTM.app.modules.auth.constants.MemberType;
import swm.hkcc.LGTM.app.modules.auth.exception.UnspecifiedMemberType;
import swm.hkcc.LGTM.app.modules.serverDrivenUI.constant.Theme;
import swm.hkcc.LGTM.app.modules.serverDrivenUI.constant.ViewType;

@Getter
public enum MissionContentType {

    ON_GOING_MISSION_TITLE_V1(ViewType.TITLE, Theme.WHITE, "진행 중인 미션"),
    ON_GOING_MISSION_LIST_V1(ViewType.ITEM, Theme.WHITE, 3),

    RECOMMENDED_MISSION_TITLE_V1(ViewType.TITLE, Theme.GRAY, "맞춤 추천 미션"),
    RECOMMENDED_MISSION_LIST_V1(ViewType.ITEM, Theme.GRAY, 3),

    HOT_MISSION_TITLE_V1(ViewType.TITLE, Theme.GRAY, "핫한 미션"),
    HOT_MISSION_LIST_V1(ViewType.ITEM, Theme.GRAY, 3),

    TOTAL_MISSION_TITLE_V1(ViewType.TITLE, Theme.GRAY, "전체 미션"),
    TOTAL_MISSION_LIST_V1(ViewType.ITEM, Theme.GRAY, 5),

    SECTION_DARK_CLOSER_V1(ViewType.CLOSER, Theme.GRAY),
    SECTION_LIGHT_CLOSER_V1(ViewType.CLOSER, Theme.WHITE),

    MISSION_SUGGESTION_TITLE_V1(ViewType.TITLE, Theme.GRAY, "하고 싶은 미션이 없다면?", "어떤 미션을 만들지 고민이세요?"),
    MISSION_SUGGESTION_BODY_V1(ViewType.SUB_ITEM, Theme.GRAY, "리뷰어님, 이런 미션 만들어주세요!", "리뷰이들의 \uD83D\uDD25HOT한 주제 살펴보기"),
    ;

    private final ViewType viewType;
    private final Theme theme;
    private String titleName;
    private String titleNameForSenior;
    private String titleNameForJunior;
    private int maxItemCount;

    MissionContentType(ViewType viewType, Theme theme) {
        this.viewType = viewType;
        this.theme = theme;
    }

    MissionContentType(ViewType viewType, Theme theme, String titleName) {
        this(viewType, theme);
        this.titleName = titleName;
    }

    MissionContentType(ViewType viewType, Theme theme, String titleNameForJunior, String titleNameForSenior) {
        this(viewType, theme);
        this.titleNameForSenior = titleNameForSenior;
        this.titleNameForJunior = titleNameForJunior;
    }

    MissionContentType(ViewType viewType, Theme theme, int maxItemCount) {
        this(viewType, theme);
        this.maxItemCount = maxItemCount;
    }

    public String getTitleName(MemberType memberType) {
        if (titleName != null)
            return titleName;

        if (memberType == MemberType.JUNIOR)
            return titleNameForJunior;
        return titleNameForSenior;
    }
}
