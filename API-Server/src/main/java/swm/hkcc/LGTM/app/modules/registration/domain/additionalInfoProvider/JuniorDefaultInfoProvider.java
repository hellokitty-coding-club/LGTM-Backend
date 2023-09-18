package swm.hkcc.LGTM.app.modules.registration.domain.additionalInfoProvider;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.mission.domain.Mission;
import swm.hkcc.LGTM.app.modules.registration.dto.registrationJuniorResponse.JuniorAdditionalInfo;

@Component
@RequiredArgsConstructor
public class JuniorDefaultInfoProvider implements JuniorInfoProvider {
    @Override
    public JuniorAdditionalInfo provide(Member junior, Mission mission) {
        return new JuniorAdditionalInfo();
    }
}
