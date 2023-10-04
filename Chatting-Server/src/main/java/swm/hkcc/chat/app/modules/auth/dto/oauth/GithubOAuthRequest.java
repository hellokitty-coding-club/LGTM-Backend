package swm.hkcc.chat.app.modules.auth.dto.oauth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GithubOAuthRequest {
    private String clientId;
    private String clientSecret;
    private String code;
}
