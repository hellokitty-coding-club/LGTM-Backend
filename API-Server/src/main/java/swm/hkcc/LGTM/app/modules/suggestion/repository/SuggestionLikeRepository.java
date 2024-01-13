package swm.hkcc.LGTM.app.modules.suggestion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swm.hkcc.LGTM.app.modules.suggestion.domain.SuggestionLike;

import java.util.Optional;

public interface SuggestionLikeRepository extends JpaRepository<SuggestionLike, Long> {
    Integer countBySuggestion_SuggestionId(Long suggestionId);

    Boolean existsBySuggestion_SuggestionIdAndMember_MemberId(Long suggestionId, Long memberId);

    Optional<SuggestionLike> findBySuggestion_SuggestionIdAndMember_MemberId(Long suggestionId, Long memberId);
}
