package swm.hkcc.LGTM.app.modules.registration.domain.additionalInfoProvider;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.mission.domain.Mission;
import swm.hkcc.LGTM.app.modules.registration.domain.MissionRegistration;
import swm.hkcc.LGTM.app.modules.registration.dto.registrationJuniorResponse.JuniorAdditionalInfo;
import swm.hkcc.LGTM.app.modules.registration.dto.registrationJuniorResponse.JuniorAdditionalPullRequestInfo;
import swm.hkcc.LGTM.app.modules.registration.exception.NotRegisteredMissionInternal;
import swm.hkcc.LGTM.app.modules.registration.repository.MissionRegistrationRepository;

@Component
@RequiredArgsConstructor
public class JuniorPullRequestInfoProvider implements JuniorInfoProvider {
    private final MissionRegistrationRepository missionRegistrationRepository;

    @Override
    public JuniorAdditionalInfo provide(Member junior, Mission mission) {
        MissionRegistration registration = missionRegistrationRepository.findByMission_MissionIdAndJunior_MemberId(mission.getMissionId(), junior.getMemberId())
                .orElseThrow(NotRegisteredMissionInternal::new);

        return JuniorAdditionalPullRequestInfo.builder()
                .githubPullRequestUrl(registration.getGithubPullRequestUrl())
                .build();
    }
}
