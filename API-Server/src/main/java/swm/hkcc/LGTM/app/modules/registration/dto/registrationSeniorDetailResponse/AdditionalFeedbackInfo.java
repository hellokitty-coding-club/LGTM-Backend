package swm.hkcc.LGTM.app.modules.registration.dto.registrationSeniorDetailResponse;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdditionalFeedbackInfo extends AdditionalInfo {
    private Long reviewId;

    @Override
    public RegistrationSeniorDetailResponse createResponse() {
        RegistrationSeniorDetailFeedbackResponse response = new RegistrationSeniorDetailFeedbackResponse();
        response.setFeedbackId(reviewId);
        return response;
    }
}
