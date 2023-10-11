package swm.hkcc.LGTM.app.modules.mission.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swm.hkcc.LGTM.app.modules.mission.domain.MissionRecommendation;

import java.util.List;

public interface MissionRecommendationRepository extends JpaRepository<MissionRecommendation, MissionRecommendation.MissionRecommendationId> {

    List<MissionRecommendation> findByIdMemberId(Long memberId);

}
