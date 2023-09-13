package swm.hkcc.LGTM.app.modules.registration.dto.registrationJuniorResponse;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class JuniorAdditionalPullRequestInfo extends JuniorAdditionalInfo {
    private String githubPullRequestUrl;


    public RegistrationJuniorResponse createResponse() {
        RegistrationJuniorPullRequestResponse response = new RegistrationJuniorPullRequestResponse();
        response.setPullReqeustUrl(this.githubPullRequestUrl);
        return response;
    }
}
