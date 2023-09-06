package swm.hkcc.LGTM.app.modules.registration.repository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import swm.hkcc.LGTM.app.modules.mission.domain.Mission;
import swm.hkcc.LGTM.app.modules.registration.domain.MissionRegistration;

import java.util.List;
import java.util.Optional;

public interface MissionRegistrationRepository extends JpaRepository<MissionRegistration, Long>, MissionRegistrationCustomRepository {
    @Cacheable(value = "mission_participant_count", key = "#p0")
    int countByMission_MissionId(Long missionId);

    int countByMission_MissionIdAndJunior_MemberId(Long missionId, Long juniorId);

    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END FROM MissionRegistration m WHERE m.mission.missionId = :missionId AND m.junior.memberId = :memberId")
    boolean existsByMissionIdAndMemberId(Long missionId, Long memberId);

    @Query("SELECT mr FROM MissionRegistration mr JOIN FETCH mr.mission WHERE mr.junior.memberId = :memberId")
    List<MissionRegistration> findAllByJuniorMemberIdWithMission(@Param("memberId") Long memberId);
}
