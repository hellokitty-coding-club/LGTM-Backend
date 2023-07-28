package swm.hkcc.LGTM.app.modules.mission.domain.holder;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import swm.hkcc.LGTM.app.modules.mission.constant.MissionContentType;
import swm.hkcc.LGTM.app.modules.mission.service.MissionService;
import swm.hkcc.LGTM.app.modules.serverDrivenUI.ServerDrivenContents;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class MissionItemHolder {

    private final MissionService missionService;

    public Function<Long, ServerDrivenContents> getMissionListFunction(MissionContentType missionContentType) {
        switch (missionContentType) {
            case TOTAL_MISSION_LIST:
                return missionService::getTotalMissions;
            case ON_GOING_MISSION_LIST:
                return missionService::getOngoingMissions;
            case RECOMMENDED_MISSION_LIST:
                return missionService::getRecommendMissions;
            default:
                return null;
        }
    }
}
