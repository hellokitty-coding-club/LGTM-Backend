package swm.hkcc.LGTM.app.modules.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swm.hkcc.LGTM.app.modules.review.domain.Review;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByMission_MissionIdAndReviewer_MemberId(Long missionId, Long memberId);
}
