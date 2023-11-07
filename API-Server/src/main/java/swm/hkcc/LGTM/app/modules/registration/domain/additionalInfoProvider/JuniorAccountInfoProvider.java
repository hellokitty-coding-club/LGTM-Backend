package swm.hkcc.LGTM.app.modules.registration.domain.additionalInfoProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.mission.domain.Mission;
import swm.hkcc.LGTM.app.modules.registration.dto.registrationJuniorResponse.JuniorAdditionalAccountInfo;
import swm.hkcc.LGTM.app.modules.registration.dto.registrationJuniorResponse.JuniorAdditionalInfo;
import swm.hkcc.LGTM.app.modules.registration.repository.MissionRegistrationRepository;

@Component
@RequiredArgsConstructor
@Slf4j
public class JuniorAccountInfoProvider implements JuniorInfoProvider {
    @Override
    public JuniorAdditionalInfo provide(Member junior, Mission mission) {
        return JuniorAdditionalAccountInfo.builder()
                .accountNumber(mission.getWriter().getSenior().getAccountNumber())
                .bankName(mission.getWriter().getSenior().getBank().getName())
                .price(mission.getPrice())
                .sendTo(mission.getWriter().getSenior().getAccountHolderName())
                .build();
    }
}
