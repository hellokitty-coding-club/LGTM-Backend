package swm.hkcc.LGTM.app.modules.suggestion.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.suggestion.domain.Suggestion;
import swm.hkcc.LGTM.app.modules.suggestion.dto.CreateSuggestionRequest;
import swm.hkcc.LGTM.app.modules.suggestion.dto.CreateSuggestionResponse;
import swm.hkcc.LGTM.app.modules.suggestion.dto.SuggestionDto;
import swm.hkcc.LGTM.app.modules.suggestion.exception.NotExistSuggestion;
import swm.hkcc.LGTM.app.modules.suggestion.exception.NotMySuggestion;
import swm.hkcc.LGTM.app.modules.suggestion.repository.SuggestionLikeRepository;
import swm.hkcc.LGTM.app.modules.suggestion.repository.SuggestionRepository;

import java.util.List;
import java.util.Objects;

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

    public List<SuggestionDto> getSuggestionList(Member member) {
        List<Suggestion> suggestionList = suggestionRepository.findAll(
                Sort.by(Sort.Direction.DESC, "createdAt"));
        return suggestionList.stream()
                .map(suggestion -> getSuggestionDetail(suggestion, member))
                .toList();
    }

    public SuggestionDto getSuggestion(Long suggestionId, Member member) {
        Suggestion suggestion = suggestionRepository.findById(suggestionId).orElseThrow(NotExistSuggestion::new);
        return getSuggestionDetail(suggestion, member);
    }

    private SuggestionDto getSuggestionDetail(Suggestion suggestion, Member member) {
        return SuggestionDto.from(suggestion,
                suggestionLikeRepository.countBySuggestion_SuggestionId(suggestion.getSuggestionId()),
                suggestionLikeRepository.existsBySuggestion_SuggestionIdAndMember_MemberId(suggestion.getSuggestionId(), member.getMemberId()),
                Objects.equals(member.getMemberId(), suggestion.getWriter().getMemberId()));
    }

    public boolean deleteSuggestion(Long suggestionId, Member member) {
        Suggestion suggestion = suggestionRepository.findById(suggestionId).orElseThrow(NotExistSuggestion::new);
        validateMySuggestion(suggestion, member);
        suggestionRepository.delete(suggestion);
        return true;
    }

    private void validateMySuggestion(Suggestion suggestion, Member member) {
        if (!Objects.equals(member.getMemberId(), suggestion.getWriter().getMemberId())) {
            throw new NotMySuggestion();
        }
    }

}
