package swm.hkcc.LGTM.app.modules.suggestion.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.suggestion.exception.NotExistSuggestion;
import swm.hkcc.LGTM.app.modules.suggestion.repository.SuggestionLikeRepository;
import swm.hkcc.LGTM.app.modules.suggestion.repository.SuggestionRepository;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


class SuggestionServiceTest {
    @Mock
    private SuggestionRepository suggestionRepository;
    @Mock
    private SuggestionLikeRepository suggestionLikeRepository;
    @InjectMocks
    private SuggestionService suggestionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("존재하지 않는 suggestionId로 getSuggestion을 호출하면 예외가 발생한다.")
    void getSuggestion_withNonExistingSuggestionId_thenThrowException() {
        // given
        Long nonExistingSuggestionId = 1L;
        Member member = Member.builder().memberId(1L).build();
        given(suggestionRepository.findById(any())).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> suggestionService.getSuggestion(nonExistingSuggestionId, member))
                .isInstanceOf(NotExistSuggestion.class);
    }

}