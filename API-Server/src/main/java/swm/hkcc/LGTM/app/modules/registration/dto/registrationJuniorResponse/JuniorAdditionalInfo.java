package swm.hkcc.LGTM.app.modules.registration.dto.registrationJuniorResponse;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
public class JuniorAdditionalInfo {

    public RegistrationJuniorResponse createResponse() {
        return new RegistrationJuniorResponse();
    }
}
