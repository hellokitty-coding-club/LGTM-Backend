package swm.hkcc.LGTM.app.modules.registration.domain.additionalInfoProvider;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.member.exception.NotExistMember;
import swm.hkcc.LGTM.app.modules.mission.domain.Mission;
import swm.hkcc.LGTM.app.modules.registration.dto.registrationJuniorResponse.JuniorAdditionalAccountInfo;
import swm.hkcc.LGTM.app.modules.registration.dto.registrationJuniorResponse.JuniorAdditionalInfo;
import swm.hkcc.LGTM.app.modules.registration.repository.MissionRegistrationRepository;

@Component
@RequiredArgsConstructor
public class JuniorAccountInfoProvider implements JuniorInfoProvider {
    private final MissionRegistrationRepository missionRegistrationRepository;
    @Override
    public JuniorAdditionalInfo provide( Member junior, Mission mission) {
        Member senior = missionRegistrationRepository.getSeniorByMissionAndJunior(mission).orElseThrow(NotExistMember::new);

        return JuniorAdditionalAccountInfo.builder()
                .accountNumber(senior.getSenior().getAccountNumber())
                .bankName(senior.getSenior().getBank().getName())
                .price(mission.getPrice())
                .build();
    }

}
