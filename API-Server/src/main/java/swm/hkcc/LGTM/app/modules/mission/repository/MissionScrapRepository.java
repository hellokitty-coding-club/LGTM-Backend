package swm.hkcc.LGTM.app.modules.mission.repository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import swm.hkcc.LGTM.app.modules.mission.domain.MissionScrap;

public interface MissionScrapRepository extends JpaRepository<MissionScrap, Long> {
    @Cacheable(value = "mission_scrap_count", key = "#missionId")
    int countByMission_MissionId(Long missionId);
    boolean existsByScrapper_MemberIdAndMission_MissionId(Long memberId, Long missionId);
}
