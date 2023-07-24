package swm.hkcc.LGTM.app.modules.mission.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swm.hkcc.LGTM.app.modules.mission.domain.Mission;

public interface MissionRepository extends JpaRepository<Mission, Long> {
}
