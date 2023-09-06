package swm.hkcc.LGTM.app.modules.registration.service.additionalInfoProvider;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.registration.dto.registrationSeniorDetailResponse.AdditionalInfo;
import swm.hkcc.LGTM.app.modules.registration.dto.registrationSeniorDetailResponse.AdditionalPayInfo;


@Component
@RequiredArgsConstructor
public class PaymentInfoProvider implements AdditionalInfoProvider {
    @Override
    public AdditionalInfo provide(Member junior, Long missionId) {
        return AdditionalPayInfo.builder()
                .realName(junior.getJunior().getRealName())
                .build();
    }

}
