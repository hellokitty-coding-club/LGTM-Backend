package swm.hkcc.LGTM.app.modules.registration.dto.registrationJuniorResponse;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@Data
@SuperBuilder
public class JuniorAdditionalFeedbackInfo extends JuniorAdditionalInfo {
    private Long reviewId;

    @Override
    public Map<String, Object> getAdditionalInfo() {
        Map<String, Object> additionalInfo = super.getAdditionalInfo();
        additionalInfo.put("feedbackId", reviewId);
        return additionalInfo;
    }
}
