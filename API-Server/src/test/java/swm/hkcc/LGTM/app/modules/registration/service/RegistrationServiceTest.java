package swm.hkcc.LGTM.app.modules.registration.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import swm.hkcc.LGTM.app.modules.member.domain.Junior;
import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.member.domain.Senior;
import swm.hkcc.LGTM.app.modules.member.exception.NotJuniorMember;
import swm.hkcc.LGTM.app.modules.member.exception.NotSeniorMember;
import swm.hkcc.LGTM.app.modules.member.service.MemberValidator;
import swm.hkcc.LGTM.app.modules.mission.domain.Mission;
import swm.hkcc.LGTM.app.modules.mission.exception.NotExistMission;
import swm.hkcc.LGTM.app.modules.mission.repository.MissionRepository;
import swm.hkcc.LGTM.app.modules.registration.exception.FullRegisterMembers;
import swm.hkcc.LGTM.app.modules.registration.exception.MissRegisterDeadline;
import swm.hkcc.LGTM.app.modules.registration.exception.NotMyMission;
import swm.hkcc.LGTM.app.modules.registration.repository.MissionRegistrationRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;


class RegistrationServiceTest {
    @Mock
    private MissionRepository missionRepository;

    @Mock
    private MissionRegistrationRepository missionRegistrationRepository;

    @Mock
    private MemberValidator memberValidator;

    @Mock
    private RegistrationValidator registrationValidator;

    @InjectMocks
    private RegistrationService registrationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void Register_mission_이_없는_경우() {
        // given
        Member junior = Member.builder()
                .memberId(1L)
                .junior(Junior.builder()
                        .juniorId(1L)
                        .build())
                .build();
        given(missionRepository.findById(1L)).willReturn(Optional.ofNullable(null));
        // when

        // then
        assertThatThrownBy(() -> registrationService.registerJunior(junior, 0L))
                .isInstanceOf(NotExistMission.class);
    }

    @Test
    void Senior_Info_미션_없는_경우() {
        // given
        Member senior = Member.builder()
                .memberId(1L)
                .senior(Senior.builder()
                        .seniorId(1L)
                        .build())
                .build();
        given(missionRepository.findById(1L)).willReturn(Optional.ofNullable(null));
        // when

        // then
        assertThatThrownBy(() -> registrationService.getSeniorEnrollInfo(senior, 0L))
                .isInstanceOf(NotExistMission.class);
    }

}
