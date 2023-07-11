package swm.hkcc.LGTM.app.modules.auth.dto.signIn;

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
    private Integer githubUuid;

    @NotNull
    private boolean isRegistered;

    @Builder.Default
    private String accessToken = "";

    @Builder.Default
    private String refreshToken = "";

}
