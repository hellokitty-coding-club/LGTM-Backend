package swm.hkcc.LGTM.app.modules.registration.repository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import swm.hkcc.LGTM.app.modules.registration.domain.MissionRegistration;

public interface MissionRegistrationRepository extends JpaRepository<MissionRegistration, Long> {
    @Cacheable(value = "mission_participant_count", key = "#missionId")
    int countByMission_MissionId(Long missionId);

    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END FROM MissionRegistration m WHERE m.mission.missionId = :missionId AND m.junior.memberId = :memberId")
    boolean existsByMissionIdAndMemberId(Long missionId, Long memberId);
}
