package swm.hkcc.LGTM.app.modules.suggestion.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.suggestion.domain.Suggestion;
import swm.hkcc.LGTM.app.modules.suggestion.dto.CreateSuggestionRequest;
import swm.hkcc.LGTM.app.modules.suggestion.dto.CreateSuggestionResponse;
import swm.hkcc.LGTM.app.modules.suggestion.repository.SuggestionLikeRepository;
import swm.hkcc.LGTM.app.modules.suggestion.repository.SuggestionRepository;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class SuggestionService {
    private final SuggestionRepository suggestionRepository;
    private final SuggestionLikeRepository suggestionLikeRepository;

    public CreateSuggestionResponse createSuggestion(CreateSuggestionRequest requestBody, Member member) {
        Suggestion suggestion = Suggestion.from(requestBody, member);
        suggestionRepository.save(suggestion);
        return CreateSuggestionResponse.from(suggestion);
    }

}
