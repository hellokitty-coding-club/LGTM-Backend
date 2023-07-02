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
    private boolean isRegistered;

    @NotNull
    private String accessToken = "";

    @NotNull
    private String refreshToken = "";
}
