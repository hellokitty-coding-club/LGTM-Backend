package swm.hkcc.LGTM.app.modules.mission.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import swm.hkcc.LGTM.app.modules.mission.domain.Mission;

public interface MissionRepository extends JpaRepository<Mission, Long>, MissionCustomRepository {
//    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END FROM Mission m WHERE m.missionId = :missionId AND m.writer.memberId = :memberId")
//    boolean existsByMissionIdAndMemberId(Long missionId, Long memberId);
    boolean existsByMissionIdAndWriter_MemberId(Long missionId, Long memberId);
}
