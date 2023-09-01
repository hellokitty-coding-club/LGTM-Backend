package swm.hkcc.LGTM.app.modules.registration.service.additionalInfoProvider;

import org.springframework.stereotype.Component;
import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.registration.dto.registrationSeniorDetailResponse.AdditionalInfo;

@Component
public class DefaultInfoProvider implements AdditionalInfoProvider {
    @Override
    public AdditionalInfo provide(Member junior, Long missionId) {
        return new AdditionalInfo();
    }
}
