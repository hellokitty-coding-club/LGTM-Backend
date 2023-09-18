package swm.hkcc.LGTM.app.modules.registration.dto.registrationJuniorResponse;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@NoArgsConstructor
public class RegistrationJuniorPullRequestResponse extends RegistrationJuniorResponse {
    private String pullReqeustUrl;
}
