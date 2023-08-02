package swm.hkcc.LGTM.app.modules.mission.service;

import swm.hkcc.LGTM.app.modules.mission.domain.MissionContentData;
import swm.hkcc.LGTM.app.modules.serverDrivenUI.ServerDrivenContents;

public interface MissionService {
    MissionContentData getOngoingMissions(Long memberId);

    MissionContentData getRecommendMissions(Long memberId);

    MissionContentData getTotalMissions(Long memberId);
}
