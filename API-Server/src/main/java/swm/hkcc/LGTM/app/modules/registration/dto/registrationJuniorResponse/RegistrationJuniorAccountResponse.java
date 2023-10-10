package swm.hkcc.LGTM.app.modules.registration.dto.registrationJuniorResponse;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@NoArgsConstructor
public class RegistrationJuniorAccountResponse extends RegistrationJuniorResponse {
    private JuniorAdditionalAccountInfo accountInfo;
}
