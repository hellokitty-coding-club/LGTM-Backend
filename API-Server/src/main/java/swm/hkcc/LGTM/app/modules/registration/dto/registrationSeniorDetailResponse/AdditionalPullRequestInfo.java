package swm.hkcc.LGTM.app.modules.registration.dto.registrationSeniorDetailResponse;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class AdditionalPullRequestInfo extends AdditionalInfo {
    private String githubPullRequestUrl;

    @Override
    public Map<String, Object> getAdditionalInfo() {
        Map<String, Object> additionalInfo = super.getAdditionalInfo();
        additionalInfo.put("githubPullRequestUrl", githubPullRequestUrl);
        return additionalInfo;
    }
}
