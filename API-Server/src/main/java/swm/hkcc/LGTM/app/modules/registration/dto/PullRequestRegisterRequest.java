package swm.hkcc.LGTM.app.modules.registration.dto;

import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
public class PullRequestRegisterRequest {
    @URL
    private String githubPrUrl;

    public String getGithubPrUrl() {
        // valid

        return githubPrUrl;
    }
}
