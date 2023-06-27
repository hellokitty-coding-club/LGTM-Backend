package swm.hkcc.LGTM.app.modules.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class SignInResponse {
    private String accessToken;
    private String refreshToken;
}
