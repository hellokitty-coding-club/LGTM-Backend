package swm.hkcc.LGTM.app.modules.tag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swm.hkcc.LGTM.app.modules.tag.domain.TechTagPerMission;

public interface TechTagPerMissionRepository extends JpaRepository<TechTagPerMission, Long> {
}
