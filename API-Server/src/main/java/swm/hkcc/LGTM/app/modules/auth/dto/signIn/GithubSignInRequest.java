package swm.hkcc.LGTM.app.modules.auth.dto.signIn;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GithubSignInRequest {
    @NotNull
    private String githubAccessToken;
}
