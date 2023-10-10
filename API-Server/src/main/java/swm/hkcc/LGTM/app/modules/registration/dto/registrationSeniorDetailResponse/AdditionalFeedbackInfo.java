package swm.hkcc.LGTM.app.modules.registration.dto.registrationSeniorDetailResponse;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class AdditionalFeedbackInfo extends AdditionalInfo {
    private Long reviewId;

    @Override
    public Map<String, Object> getAdditionalInfo() {
        Map<String, Object> additionalInfo = super.getAdditionalInfo();
        additionalInfo.put("feedbackId", reviewId);
        return additionalInfo;
    }
}
