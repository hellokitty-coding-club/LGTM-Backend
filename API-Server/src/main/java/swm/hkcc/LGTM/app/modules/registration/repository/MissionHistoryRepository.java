package swm.hkcc.LGTM.app.modules.registration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swm.hkcc.LGTM.app.modules.registration.domain.MissionHistory;

public interface MissionHistoryRepository extends JpaRepository<MissionHistory, Long> {

}
