package swm.hkcc.LGTM.app.modules.mission.repository;

import swm.hkcc.LGTM.app.modules.mission.domain.Mission;

import java.util.List;

public interface MissionCustomRepository {
    List<Mission> getJuniorOnGoingMissions(Long memberId);

    List<Mission> getSeniorOngoingMissions(Long memberId);

    List<Mission> getRecommendedMissions(Long memberId);

    List<Mission> getTotalMissions();
}
