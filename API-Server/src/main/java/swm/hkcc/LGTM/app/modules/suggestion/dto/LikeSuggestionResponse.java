package swm.hkcc.LGTM.app.modules.suggestion.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LikeSuggestionResponse {
    private static final String LIKE_BIG_NUMBER = "999+";
    private Boolean isLiked;
    private String likeNum;

    public static LikeSuggestionResponse from(Integer likeNum, Boolean isLiked) {
        return LikeSuggestionResponse.builder()
                .isLiked(isLiked)
                .likeNum(likeNum < 1000 ? likeNum.toString() : LIKE_BIG_NUMBER)
                .build();
    }
}
