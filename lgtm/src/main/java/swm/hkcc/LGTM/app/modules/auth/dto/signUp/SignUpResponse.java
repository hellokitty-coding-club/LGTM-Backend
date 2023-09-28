package swm.hkcc.LGTM.app.modules.auth.dto.signUp;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignUpResponse {
    @NotNull
    private Long memberId;

    @NotNull
    private String githubId;

    @NotNull
    private String accessToken;

    @NotNull
    private String refreshToken;

    @NotNull
    private String memberType = "";

}
