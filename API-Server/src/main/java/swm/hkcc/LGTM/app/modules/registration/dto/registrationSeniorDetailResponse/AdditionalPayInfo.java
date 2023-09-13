package swm.hkcc.LGTM.app.modules.registration.dto.registrationSeniorDetailResponse;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AdditionalPayInfo extends AdditionalInfo {
    private String realName;

    @Override
    public RegistrationSeniorDetailResponse createResponse() {
        RegistrationSeniorDetailPayResponse response = new RegistrationSeniorDetailPayResponse();
        response.setRealName(realName);
        return response;
    }
}
