package swm.hkcc.LGTM.app.modules.suggestion.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import swm.hkcc.LGTM.app.modules.suggestion.domain.Suggestion;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class SuggestionDto {
    private static final String LIKE_BIG_NUMBER = "999+";
    @NotNull
    private Long suggestionId;
    @NotNull
    private String title;
    @NotNull
    private String description;
    @NotNull
    private LocalDateTime date;
    @NotNull
    private String likeNum;
    @NotNull
    private Boolean isLiked;
    @NotNull
    private Boolean isMyPost;

    public static SuggestionDto from(Suggestion suggestion, Integer likeNum, Boolean isLiked, Boolean isMyPost) {
        return SuggestionDto.builder()
                .suggestionId(suggestion.getSuggestionId())
                .title(suggestion.getTitle())
                .description(suggestion.getDescription())
                .date(suggestion.getCreatedAt())
                .likeNum(likeNum < 1000 ? likeNum.toString() : LIKE_BIG_NUMBER)
                .isLiked(isLiked)
                .isMyPost(isMyPost)
                .build();
    }
}
