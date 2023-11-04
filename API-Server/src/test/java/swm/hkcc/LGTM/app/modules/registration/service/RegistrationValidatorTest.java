package swm.hkcc.LGTM.app.modules.registration.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import swm.hkcc.LGTM.app.modules.member.domain.Junior;
import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.member.domain.Senior;
import swm.hkcc.LGTM.app.modules.mission.domain.Mission;
import swm.hkcc.LGTM.app.modules.registration.exception.FullRegisterMembers;
import swm.hkcc.LGTM.app.modules.registration.exception.MissRegisterDeadline;
import swm.hkcc.LGTM.app.modules.registration.exception.NotMyMission;
import swm.hkcc.LGTM.app.modules.registration.repository.MissionRegistrationRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

class RegistrationValidatorTest {
    @Mock
    private MissionRegistrationRepository missionRegistrationRepository;


    @InjectMocks
    private RegistrationValidator registrationValidator;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
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
        given(missionRegistrationRepository.countByMission_MissionId(1L)).willReturn(10);
        // when

        // then
        assertThatThrownBy(() -> registrationValidator.validateToRegisterMission(mission, junior.getMemberId()))
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
        given(missionRegistrationRepository.countByMission(mission)).willReturn(10);


        // when

        // then
        assertThatThrownBy(() -> registrationValidator.validateToRegisterMission(mission, junior.getMemberId()))
                .isInstanceOf(FullRegisterMembers.class);
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



        assertThatThrownBy(() -> registrationValidator.validateMissionForSenior(mission, senior.getMemberId()))
                .isInstanceOf(NotMyMission.class);
    }

}