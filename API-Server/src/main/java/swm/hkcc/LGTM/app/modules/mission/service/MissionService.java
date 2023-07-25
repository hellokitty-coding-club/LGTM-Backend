package swm.hkcc.LGTM.app.modules.mission.service;

import swm.hkcc.LGTM.app.modules.serverDrivenUI.ServerDrivenContents;

public interface MissionService {
    ServerDrivenContents getOngoingMissions(Long memberId);

    ServerDrivenContents getRecommendMissions(Long memberId);

    ServerDrivenContents getTotalMissions(Long memberId);
}
