package swm.hkcc.LGTM.app.modules.auth.dto;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class TokenDto {
    private String githubAccessToken;
    private String githubRefreshToken;

}
