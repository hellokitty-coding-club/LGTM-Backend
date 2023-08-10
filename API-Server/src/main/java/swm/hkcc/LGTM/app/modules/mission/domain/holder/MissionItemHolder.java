package swm.hkcc.LGTM.app.modules.mission.domain.holder;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import swm.hkcc.LGTM.app.modules.mission.constant.MissionContentType;
import swm.hkcc.LGTM.app.modules.mission.domain.MissionContentData;
import swm.hkcc.LGTM.app.modules.mission.service.MissionService;
import swm.hkcc.LGTM.app.modules.serverDrivenUI.ServerDrivenContent;
import swm.hkcc.LGTM.app.modules.serverDrivenUI.ViewType;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class MissionItemHolder {

    public static final String TOTAL_MISSION_EMPTY_VIEW = "total_mission_empty_view";
    public static final String ONGOING_MISSION_EMPTY_VIEW = "ongoing_mission_empty_view";
    public static final String RECOMMENDED_MISSION_EMPTY_VIEW = "recommended_mission_empty_view";

    private final MissionService missionService;

    public Function<Long, MissionContentData> getMissionListFunction(MissionContentType missionContentType) {
        return switch (missionContentType) {
            case TOTAL_MISSION_LIST_V1 -> missionService::getTotalMissions;
            case ON_GOING_MISSION_LIST_V1 -> missionService::getOngoingMissions;
            case RECOMMENDED_MISSION_LIST_V1 -> missionService::getRecommendMissions;
            default -> null;
        };
    }

    public ServerDrivenContent getMissionEmptyView(MissionContentType missionContentType) {
        return switch (missionContentType) {
            case TOTAL_MISSION_LIST_V1 -> ServerDrivenContent.from(TOTAL_MISSION_EMPTY_VIEW, missionContentType.getTheme(), ViewType.EMPTY);
            case ON_GOING_MISSION_LIST_V1 -> ServerDrivenContent.from(ONGOING_MISSION_EMPTY_VIEW, missionContentType.getTheme(), ViewType.EMPTY);
            case RECOMMENDED_MISSION_LIST_V1 -> ServerDrivenContent.from(RECOMMENDED_MISSION_EMPTY_VIEW, missionContentType.getTheme(), ViewType.EMPTY);
            default -> null;
        };
    }

}
