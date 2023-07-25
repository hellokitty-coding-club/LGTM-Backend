package swm.hkcc.LGTM.app.modules.mission.repository;

import swm.hkcc.LGTM.app.modules.mission.dto.MissionDetailsDto;
import swm.hkcc.LGTM.app.modules.mission.dto.MissionDto;

import java.util.List;

public interface MissionCustomRepository {
    List<MissionDto> getOnGoingMissions(Long memberId);

    List<MissionDetailsDto> getRecommendedMissions(Long memberId);

    List<MissionDetailsDto> getTotalMissions(Long memberId);
}
