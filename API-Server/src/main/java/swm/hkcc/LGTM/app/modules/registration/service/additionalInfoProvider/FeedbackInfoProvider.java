package swm.hkcc.LGTM.app.modules.registration.service.additionalInfoProvider;

import lombok.RequiredArgsConstructor;
import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.registration.dto.registrationSeniorDetailResponse.AdditionalFeedbackInfo;
import swm.hkcc.LGTM.app.modules.registration.dto.registrationSeniorDetailResponse.AdditionalInfo;
import swm.hkcc.LGTM.app.modules.review.domain.Review;
import swm.hkcc.LGTM.app.modules.review.repository.ReviewRepository;

import java.util.Optional;

@RequiredArgsConstructor
public class FeedbackInfoProvider implements AdditionalInfoProvider {
    private final ReviewRepository reviewRepository;

    @Override
    public AdditionalInfo provide(Member junior, Long missionId) {
        Optional<Review> review = reviewRepository.findByMission_MissionIdAndReviewer_MemberId(missionId, junior.getMemberId());

        if (review.isPresent()) {
            return AdditionalFeedbackInfo.builder()
                    .reviewId(review.get().getReviewId())
                    .build();
        }

        return new AdditionalInfo();
    }
}
