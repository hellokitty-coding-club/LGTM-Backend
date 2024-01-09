package swm.hkcc.LGTM.app.modules.suggestion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swm.hkcc.LGTM.app.modules.suggestion.domain.SuggestionLike;

public interface SuggestionLikeRepository extends JpaRepository<SuggestionLike, Long> {
    Integer countBySuggestion_SuggestionId(Long suggestionId);
    Boolean existsBySuggestion_SuggestionIdAndMember_MemberId(Long suggestionId, Long memberId);
}
