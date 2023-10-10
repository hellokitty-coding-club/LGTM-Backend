package swm.hkcc.LGTM.app.modules.registration.domain.additionalInfoProvider;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.mission.domain.Mission;
import swm.hkcc.LGTM.app.modules.registration.dto.registrationJuniorResponse.JuniorAdditionalFeedbackInfo;
import swm.hkcc.LGTM.app.modules.registration.dto.registrationJuniorResponse.JuniorAdditionalInfo;
import swm.hkcc.LGTM.app.modules.review.domain.Review;
import swm.hkcc.LGTM.app.modules.review.exception.NotExistReviewInternal;
import swm.hkcc.LGTM.app.modules.review.repository.ReviewRepository;

@Component
@RequiredArgsConstructor
public class JuniorFeedbackInfoProvider implements JuniorInfoProvider {
    private final ReviewRepository reviewRepository;

    @Override
    public JuniorAdditionalInfo provide(Member junior, Mission mission) {
        Review review = reviewRepository.findByMission_MissionIdAndReviewer_MemberId(mission.getMissionId(), junior.getMemberId())
                .orElseThrow(NotExistReviewInternal::new);

        return JuniorAdditionalFeedbackInfo.builder()
                .reviewId(review.getReviewId())
                .build();
    }
}
