package swm.hkcc.LGTM.app.modules.suggestion.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import swm.hkcc.LGTM.app.modules.suggestion.domain.Suggestion;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class CreateSuggestionResponse {
    @NotNull
    private Long writerId;
    @NotNull
    private Long suggestionId;

    public static CreateSuggestionResponse from(Suggestion suggestion) {
        return CreateSuggestionResponse.builder()
                .writerId(suggestion.getWriter().getMemberId())
                .suggestionId(suggestion.getSuggestionId())
                .build();
    }
}
