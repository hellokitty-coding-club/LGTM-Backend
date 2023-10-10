package swm.hkcc.LGTM.app.modules.mission.repository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import swm.hkcc.LGTM.app.modules.mission.domain.MissionView;

public interface MissionViewRepository extends JpaRepository<MissionView, Long> {
    @Cacheable(value = "mission_view_count", key = "#p0")
    int countByMission_MissionId(Long missionId);
}
