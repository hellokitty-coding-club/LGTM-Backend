package swm.hkcc.chat.app.modules.auth.dto.signIn;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class SignInResponse {

    @NotNull
    private Long memberId;

    @NotNull
    private String githubId;

    @NotNull
    private Integer githubOauthId;

    @NotNull
    private boolean isRegistered;

    @Builder.Default
    private String accessToken = "";

    @Builder.Default
    private String refreshToken = "";

    @Builder.Default
    private String profileImageUrl = "";

    @Builder.Default
    private String memberType = "";

}
