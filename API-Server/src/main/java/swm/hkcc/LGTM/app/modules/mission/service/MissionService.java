package swm.hkcc.LGTM.app.modules.mission.service;

import swm.hkcc.LGTM.app.modules.mission.domain.Mission;
import swm.hkcc.LGTM.app.modules.mission.domain.MissionContentData;
import swm.hkcc.LGTM.app.modules.mission.dto.MissionDetailViewResponse;

public interface MissionService {
    MissionContentData getJuniorOngoingMissions(Long memberId);

    MissionContentData getSeniorOngoingMissions(Long memberId);

    MissionContentData getRecommendMissions(Long memberId);

    MissionContentData getMostViewedMissions(Long memberId);

    MissionContentData getTotalMissions(Long memberId);

    MissionDetailViewResponse getMissionDetail(Long memberId, Long missionId);

    Mission getMission(Long missionId);
}
