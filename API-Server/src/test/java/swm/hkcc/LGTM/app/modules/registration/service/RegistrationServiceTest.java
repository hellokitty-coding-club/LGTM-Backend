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

    @InjectMocks
    private RegistrationService registrationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void Register_junior가_아닌_경우() {
        // given
        Member notJunior = Member.builder()
                .memberId(1L)
                .build();
        // when

        // then
        assertThatThrownBy(() -> registrationService.registerJunior(notJunior, 1L))
                .isInstanceOf(NotJuniorMember.class);
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

    // 미션 마감일이 지난 경우
    @Test
    void Register_미션_마감일이_지난_경우() {
        // given
        Member junior = Member.builder()
                .memberId(1L)
                .junior(Junior.builder()
                        .juniorId(1L)
                        .build())
                .build();
        Mission mission = Mission.builder()
                .missionId(1L)
                .registrationDueDate(LocalDate.now().minusDays(1))
                .build();
        given(missionRepository.findById(1L)).willReturn(Optional.of(mission));
        given(missionRegistrationRepository.countByMission_MissionId(1L)).willReturn(10);

        // when

        // then
        assertThatThrownBy(() -> registrationService.registerJunior(junior, mission.getMissionId()))
                .isInstanceOf(MissRegisterDeadline.class);
    }

    @Test
    void Register_미션_등록인원이_넘치는_경우() {
        // given
        Member junior = Member.builder()
                .memberId(1L)
                .junior(Junior.builder()
                        .juniorId(1L)
                        .build())
                .build();
        Mission mission = Mission.builder()
                .missionId(1L)
                .registrationDueDate(LocalDate.now().plusDays(1))
                .maxPeopleNumber(10)
                .build();
        given(missionRepository.findById(1L)).willReturn(Optional.of(mission));
        given(missionRegistrationRepository.countByMission_MissionId(1L)).willReturn(10);


        // when

        // then
        assertThatThrownBy(() -> registrationService.registerJunior(junior, mission.getMissionId()))
                .isInstanceOf(FullRegisterMembers.class);
    }

    @Test
    void Senior_Info_Senior가_아닌_경우() {
        // given
        Member notSenior = Member.builder()
                .memberId(1L)
                .build();
        // when

        // then
        assertThatThrownBy(() -> registrationService.getSeniorEnrollInfo(notSenior, 1L))
                .isInstanceOf(NotSeniorMember.class);
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

    @Test
    void Senior_Info_자신의_미션이_아닌_경우() {
        // given
        Member senior = Member.builder()
                .memberId(1L)
                .senior(Senior.builder()
                        .seniorId(1L)
                        .build())
                .build();
        Mission mission = Mission.builder()
                .missionId(1L)
                .writer(Member.builder()
                        .memberId(2L)
                        .build())
                .registrationDueDate(LocalDate.now().plusDays(1))
                .maxPeopleNumber(10)
                .build();

        given(missionRepository.findById(1L)).willReturn(Optional.ofNullable(mission));


        assertThatThrownBy(() -> registrationService.getSeniorEnrollInfo(senior, 1L))
                .isInstanceOf(NotMyMission.class);
    }
}
