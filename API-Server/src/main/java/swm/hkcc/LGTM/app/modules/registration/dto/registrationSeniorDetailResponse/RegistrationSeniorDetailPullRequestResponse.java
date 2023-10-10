package swm.hkcc.LGTM.app.modules.registration.dto.registrationSeniorDetailResponse;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegistrationSeniorDetailPullRequestResponse extends RegistrationSeniorDetailResponse {
    private String githubPullRequestUrl;
}
