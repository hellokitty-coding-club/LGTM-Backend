package swm.hkcc.LGTM.app.modules.registration.dto.registrationJuniorResponse;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class JuniorAdditionalPullRequestInfo extends JuniorAdditionalInfo {
    private String githubPullRequestUrl;


    public RegistrationJuniorResponse createResponse() {
        RegistrationJuniorPullRequestResponse response = new RegistrationJuniorPullRequestResponse();
        response.setPullRequestUrl(this.githubPullRequestUrl);
        return response;
    }
}
