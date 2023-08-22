package swm.hkcc.LGTM.app.modules.registration.repository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import swm.hkcc.LGTM.app.modules.registration.domain.MissionRegistration;

import java.util.List;
import java.util.Optional;

public interface MissionRegistrationRepository extends JpaRepository<MissionRegistration, Long> {
    @Cacheable(value = "mission_participant_count", key = "#missionId")
    int countByMission_MissionId(Long missionId);

    int countByMission_MissionIdAndJunior_MemberId(Long missionId, Long juniorId);
}
