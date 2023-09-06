package swm.hkcc.LGTM.app.modules.mission.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swm.hkcc.LGTM.app.modules.mission.domain.Mission;

import java.util.List;

public interface MissionRepository extends JpaRepository<Mission, Long>, MissionCustomRepository {
    boolean existsByMissionIdAndWriter_MemberId(Long missionId, Long memberId);

    List<Mission> findAllByWriter_MemberId(Long memberId);
}
