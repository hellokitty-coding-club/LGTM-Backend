package swm.hkcc.LGTM.app.dto.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GithubOAuthRequest {
    private String clientId;
    private String clientSecret;
    private String code;
}
