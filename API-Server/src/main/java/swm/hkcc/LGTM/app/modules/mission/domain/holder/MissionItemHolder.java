package swm.hkcc.LGTM.app.modules.mission.domain.holder;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import swm.hkcc.LGTM.app.modules.mission.constant.MissionContentType;
import swm.hkcc.LGTM.app.modules.mission.domain.MissionContentData;
import swm.hkcc.LGTM.app.modules.mission.service.MissionService;
import swm.hkcc.LGTM.app.modules.serverDrivenUI.ServerDrivenContents;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class MissionItemHolder {

    private final MissionService missionService;

    public Function<Long, MissionContentData> getMissionListFunction(MissionContentType missionContentType) {
        return switch (missionContentType) {
            case TOTAL_MISSION_LIST_V1 -> missionService::getTotalMissions;
            case ON_GOING_MISSION_LIST_V1 -> missionService::getOngoingMissions;
            case RECOMMENDED_MISSION_LIST_V1 -> missionService::getRecommendMissions;
            default -> null;
        };
    }
}
