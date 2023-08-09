package swm.hkcc.LGTM.app.modules.mission.repository;

import swm.hkcc.LGTM.app.modules.mission.domain.Mission;

import java.util.List;

public interface MissionCustomRepository {
    List<Mission> getOnGoingMissions(Long memberId);

    List<Mission> getRecommendedMissions();

    List<Mission> getTotalMissions();
}
