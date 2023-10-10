package swm.hkcc.LGTM.app.modules.registration.dto.registrationJuniorResponse;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.HashMap;
import java.util.Map;

@SuperBuilder
@NoArgsConstructor
public class JuniorAdditionalInfo {
    public Map<String, Object> getAdditionalInfo() {
        return new HashMap<>();
    }
}
