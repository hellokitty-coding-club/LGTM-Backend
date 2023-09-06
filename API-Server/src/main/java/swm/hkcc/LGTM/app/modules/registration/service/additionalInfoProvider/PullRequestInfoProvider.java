package swm.hkcc.LGTM.app.modules.registration.service.additionalInfoProvider;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.registration.domain.MissionRegistration;
import swm.hkcc.LGTM.app.modules.registration.dto.registrationSeniorDetailResponse.AdditionalInfo;
import swm.hkcc.LGTM.app.modules.registration.dto.registrationSeniorDetailResponse.AdditionalPullRequestInfo;
import swm.hkcc.LGTM.app.modules.registration.repository.MissionRegistrationRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PullRequestInfoProvider implements AdditionalInfoProvider {
    private final MissionRegistrationRepository missionRegistrationRepository;

    @Override
    public AdditionalInfo provide(Member junior, Long missionId) {
        Optional<MissionRegistration> registration = missionRegistrationRepository.findByMission_MissionIdAndJunior_MemberId(missionId, junior.getMemberId());
        if (registration.isPresent()) {
            return AdditionalPullRequestInfo.builder()
                    .githubPullRequestUrl(registration.get().getGithubPullRequestUrl())
                    .build();
        }

        return new AdditionalInfo();
    }
}
