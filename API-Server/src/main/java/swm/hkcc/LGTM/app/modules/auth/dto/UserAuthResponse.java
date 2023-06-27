package swm.hkcc.LGTM.app.modules.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class UserAuthResponse {
    private String accessToken;
    private Integer expiresIn;
    private String refreshToken;
    private Integer refreshTokenExpiresIn;
    private String tokenType;
    private String scope;

}
