package swm.hkcc.LGTM.app.modules.registration.domain.additionalInfoProvider;

import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.mission.domain.Mission;
import swm.hkcc.LGTM.app.modules.registration.dto.registrationJuniorResponse.JuniorAdditionalInfo;

public interface JuniorInfoProvider {
    JuniorAdditionalInfo provide(Member junior, Mission mission);
}
