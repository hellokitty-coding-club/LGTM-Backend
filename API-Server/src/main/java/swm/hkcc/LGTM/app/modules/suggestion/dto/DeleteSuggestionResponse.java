package swm.hkcc.LGTM.app.modules.suggestion.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeleteSuggestionResponse {
    private Long suggestionId;
    private Boolean success;
}
