package swm.hkcc.LGTM.app.modules.registration.dto.registrationJuniorResponse;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@Data
@SuperBuilder
public class JuniorAdditionalPullRequestInfo extends JuniorAdditionalInfo {
    private String githubPullRequestUrl;

    @Override
    public Map<String, Object> getAdditionalInfo() {
        Map<String, Object> additionalInfo = super.getAdditionalInfo();
        additionalInfo.put("githubPullRequestUrl", githubPullRequestUrl);
        return additionalInfo;
    }
}
