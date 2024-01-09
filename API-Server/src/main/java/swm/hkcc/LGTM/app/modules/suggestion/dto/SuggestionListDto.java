package swm.hkcc.LGTM.app.modules.suggestion.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SuggestionListDto {
    private final String infoTitle = "\uD83D\uDC4B 어떤 미션이 필요한지 알려주세요!";
    private final String infoDescription = "여러분을 위한 미션이 무엇인지 의견을 남겨주세요.\n리뷰어들이 미션을 제작하는데 큰 도움이 됩니다.";
    private List<SuggestionDto> suggestions;

    public static SuggestionListDto from(List<SuggestionDto> suggestions) {
        return SuggestionListDto.builder()
                .suggestions(suggestions)
                .build();
    }
}
