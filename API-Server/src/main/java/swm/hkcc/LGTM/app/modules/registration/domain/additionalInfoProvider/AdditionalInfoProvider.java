package swm.hkcc.LGTM.app.modules.registration.domain.additionalInfoProvider;

import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.registration.dto.registrationSeniorDetailResponse.AdditionalInfo;

public interface AdditionalInfoProvider {
    AdditionalInfo provide(Member junior, Long missionId);

}
