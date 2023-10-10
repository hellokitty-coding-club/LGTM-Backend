package swm.hkcc.LGTM.app.modules.registration.dto.registrationJuniorResponse;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class JuniorAdditionalFeedbackInfo extends JuniorAdditionalInfo {
    private Long reviewId;


    public RegistrationJuniorResponse createResponse() {
        RegistrationJuniorFeedbackResponse response = new RegistrationJuniorFeedbackResponse();
        response.setFeedbackId(this.getReviewId());
        return response;
    }
}
