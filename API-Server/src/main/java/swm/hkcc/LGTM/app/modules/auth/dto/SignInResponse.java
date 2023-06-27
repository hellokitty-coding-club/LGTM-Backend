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

    private Long userId;
    private String githubId;
    private boolean isRegistered;
    private TokenDto tokenDto;
}
