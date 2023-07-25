package swm.hkcc.LGTM.app.modules.mission.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swm.hkcc.LGTM.app.modules.mission.domain.MissionView;

public interface MissionViewRepository extends JpaRepository<MissionView, Long> {
    int countByMission_MissionId(Long missionId);
}
