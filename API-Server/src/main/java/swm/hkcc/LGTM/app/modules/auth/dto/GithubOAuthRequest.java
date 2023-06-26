package swm.hkcc.LGTM.app.modules.auth.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GithubOAuthRequest {
    private String clientId;
    private String clientSecret;
    private String code;
}
