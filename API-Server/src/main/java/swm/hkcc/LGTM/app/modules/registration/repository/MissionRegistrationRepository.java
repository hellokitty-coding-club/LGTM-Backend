package swm.hkcc.LGTM.app.modules.registration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swm.hkcc.LGTM.app.modules.registration.domain.MissionRegistration;

public interface MissionRegistrationRepository extends JpaRepository<MissionRegistration, Long> {
    int countByMission_MissionId(Long missionId);
}
