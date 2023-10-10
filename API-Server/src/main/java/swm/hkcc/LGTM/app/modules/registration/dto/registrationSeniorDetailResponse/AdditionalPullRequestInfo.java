package swm.hkcc.LGTM.app.modules.registration.dto.registrationSeniorDetailResponse;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdditionalPullRequestInfo extends AdditionalInfo {
    private String githubPullRequestUrl;

    @Override
    public RegistrationSeniorDetailResponse createResponse() {
        RegistrationSeniorDetailPullRequestResponse response = new RegistrationSeniorDetailPullRequestResponse();
        response.setGithubPullRequestUrl(githubPullRequestUrl);
        return response;
    }
}
