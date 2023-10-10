package swm.hkcc.LGTM.app.modules.registration.dto.registrationSeniorDetailResponse;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Builder
@Data
public class AdditionalPayInfo extends AdditionalInfo {
    private String realName;

    @Override
    public Map<String, Object> getAdditionalInfo() {
        Map<String, Object> additionalInfo = super.getAdditionalInfo();
        additionalInfo.put("realName", realName);
        return additionalInfo;
    }
}
