package swm.hkcc.LGTM.app.modules.tag.repository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import swm.hkcc.LGTM.app.modules.mission.domain.Mission;
import swm.hkcc.LGTM.app.modules.tag.domain.TechTag;
import swm.hkcc.LGTM.app.modules.tag.domain.TechTagPerMission;

import java.util.List;

public interface TechTagPerMissionRepository extends JpaRepository<TechTagPerMission, Long> {
    @Cacheable(value = "tech_tag_per_mission", key = "#p0")
    @Query("select tpm.techTag from TechTagPerMission tpm where tpm.mission.missionId = :missionId")
    List<TechTag> findTechTagsByMissionId(@Param("missionId") Long missionId);

    List<TechTagPerMission> findByMission(Mission mission);
}
