package swm.hkcc.LGTM.app.modules.registration.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import swm.hkcc.LGTM.app.modules.member.domain.Junior;
import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.member.domain.Senior;
import swm.hkcc.LGTM.app.modules.member.repository.MemberRepository;
import swm.hkcc.LGTM.app.modules.member.service.MemberValidator;
import swm.hkcc.LGTM.app.modules.mission.domain.Mission;
import swm.hkcc.LGTM.app.modules.mission.repository.MissionRepository;
import swm.hkcc.LGTM.app.modules.registration.domain.MissionRegistration;
import swm.hkcc.LGTM.app.modules.registration.domain.ProcessStatus;
import swm.hkcc.LGTM.app.modules.registration.dto.MissionHistoryInfo;
import swm.hkcc.LGTM.app.modules.registration.dto.registrationSeniorDetailResponse.RegistrationSeniorDetailFeedbackResponse;
import swm.hkcc.LGTM.app.modules.registration.dto.registrationSeniorDetailResponse.RegistrationSeniorDetailPayResponse;
import swm.hkcc.LGTM.app.modules.registration.dto.registrationSeniorDetailResponse.RegistrationSeniorDetailPullRequestResponse;
import swm.hkcc.LGTM.app.modules.registration.dto.registrationSeniorDetailResponse.RegistrationSeniorDetailResponse;
import swm.hkcc.LGTM.app.modules.registration.repository.MissionHistoryRepository;
import swm.hkcc.LGTM.app.modules.registration.repository.MissionRegistrationRepository;
import swm.hkcc.LGTM.app.modules.registration.domain.additionalInfoProvider.AdditionalInfoProviderFactory;
import swm.hkcc.LGTM.app.modules.review.domain.Review;
import swm.hkcc.LGTM.app.modules.review.repository.ReviewRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


@SpringBootTest
@Slf4j
@ActiveProfiles("test")
@Transactional
class SeniorEnrollDetailServiceTest {
    @Autowired
    RegistrationService registrationService;
    @Autowired
    AdditionalInfoProviderFactory additionalInfoProviderFactory;

    @MockBean
    MissionRegistrationRepository missionRegistrationRepository;
    @MockBean
    MissionHistoryRepository missionHistoryRepository;
    @MockBean
    RegistrationValidator registrationValidator;
    @MockBean
    MemberValidator memberValidator;
    @MockBean
    ReviewRepository reviewRepository;
    @MockBean
    MemberRepository memberRepository;
    @MockBean
    MissionRepository missionRepository;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // PAYMENT_CONFIRMATION
    @Test
    void PAYMENT_CONFIRMATION() {
        // given
        given(missionRegistrationRepository.getMissionHistoryByMissionAndJunior(any(), any()))
                .willReturn(
                        List.of(
                                new MissionHistoryInfo(ProcessStatus.WAITING_FOR_PAYMENT, LocalDateTime.now()),
                                new MissionHistoryInfo(ProcessStatus.PAYMENT_CONFIRMATION, LocalDateTime.now().plusHours(1))
                        ));
        Member senior = getMockSenior();
        Member junior = getMockJunior();
        Mission mission = getMockMission();

        given(memberRepository.findById(any())).willReturn(Optional.ofNullable(junior));
        given(missionRepository.findById(any())).willReturn(Optional.ofNullable(mission));

        // when
        RegistrationSeniorDetailResponse response = registrationService.getSeniorEnrollDetail(senior, mission, junior);

        // then
        assertThat(response.getStatus()).isEqualTo(ProcessStatus.PAYMENT_CONFIRMATION);
        assertThat(response).isInstanceOf(RegistrationSeniorDetailPayResponse.class);
    }

    // CODE_REVIEW
    @Test
    void CODE_REVIEW() {
        // given
        given(missionRegistrationRepository.getMissionHistoryByMissionAndJunior(any(), any()))
                .willReturn(
                        List.of(
                                new MissionHistoryInfo(ProcessStatus.WAITING_FOR_PAYMENT, LocalDateTime.now()),
                                new MissionHistoryInfo(ProcessStatus.PAYMENT_CONFIRMATION, LocalDateTime.now().plusHours(1)),
                                new MissionHistoryInfo(ProcessStatus.MISSION_PROCEEDING, LocalDateTime.now().plusHours(2)),
                                new MissionHistoryInfo(ProcessStatus.CODE_REVIEW, LocalDateTime.now().plusHours(3))
                        ));
        Member senior = getMockSenior();
        Member junior = getMockJunior();
        Mission mission = getMockMission();
        MissionRegistration missionRegistration = MissionRegistration.builder().registrationId(1L).githubPullRequestUrl("https://github.com/aaa/bb/ccc").build();

        given(memberRepository.findById(any())).willReturn(Optional.ofNullable(junior));
        given(missionRepository.findById(any())).willReturn(Optional.ofNullable(mission));
        given(missionRegistrationRepository.findByMission_MissionIdAndJunior_MemberId(any(), any())).willReturn(Optional.ofNullable(missionRegistration));

        // when
        RegistrationSeniorDetailResponse response = registrationService.getSeniorEnrollDetail(senior, mission, junior);

        // then
        assertThat(response.getStatus()).isEqualTo(ProcessStatus.CODE_REVIEW);
        assertThat(response).isInstanceOf(RegistrationSeniorDetailPullRequestResponse.class);
    }

    // FEEDBACK_REVIEWED
    @Test
    void FEEDBACK_REVIEWED() {
        // given
        given(missionRegistrationRepository.getMissionHistoryByMissionAndJunior(any(), any()))
                .willReturn(
                        List.of(
                                new MissionHistoryInfo(ProcessStatus.WAITING_FOR_PAYMENT, LocalDateTime.now()),
                                new MissionHistoryInfo(ProcessStatus.PAYMENT_CONFIRMATION, LocalDateTime.now().plusHours(1)),
                                new MissionHistoryInfo(ProcessStatus.MISSION_PROCEEDING, LocalDateTime.now().plusHours(2)),
                                new MissionHistoryInfo(ProcessStatus.CODE_REVIEW, LocalDateTime.now().plusHours(3)),
                                new MissionHistoryInfo(ProcessStatus.MISSION_FINISHED, LocalDateTime.now().plusHours(4)),
                                new MissionHistoryInfo(ProcessStatus.FEEDBACK_REVIEWED, LocalDateTime.now().plusHours(5))
                        ));
        Member senior = getMockSenior();
        Member junior = getMockJunior();
        Mission mission = getMockMission();

        given(memberRepository.findById(any())).willReturn(Optional.ofNullable(junior));
        given(missionRepository.findById(any())).willReturn(Optional.ofNullable(mission));
        given(reviewRepository.findByMission_MissionIdAndReviewer_MemberId(any(), any())).willReturn(Optional.ofNullable(Review.builder().reviewId(1L).build()));

        // when
        RegistrationSeniorDetailResponse response = registrationService.getSeniorEnrollDetail(senior,  mission, junior);

        // then
        assertThat(response.getStatus()).isEqualTo(ProcessStatus.FEEDBACK_REVIEWED);
        assertThat(response).isInstanceOf(RegistrationSeniorDetailFeedbackResponse.class);
    }

    // Other ProcessStatuses
    @Test
    void Other_ProcessStatuses() {
        // given
        given(missionRegistrationRepository.getMissionHistoryByMissionAndJunior(any(), any()))
                .willReturn(
                        List.of(
                                new MissionHistoryInfo(ProcessStatus.WAITING_FOR_PAYMENT, LocalDateTime.now())
                        ));
        Member senior = getMockSenior();
        Member junior = getMockJunior();
        Mission mission = getMockMission();

        given(memberRepository.findById(any())).willReturn(Optional.ofNullable(junior));
        given(missionRepository.findById(any())).willReturn(Optional.ofNullable(mission));

        // when
        RegistrationSeniorDetailResponse response = registrationService.getSeniorEnrollDetail(senior, mission, junior);

        // then
        assertThat(response.getStatus()).isEqualTo(ProcessStatus.WAITING_FOR_PAYMENT);
        assertThat(response).isInstanceOf(RegistrationSeniorDetailResponse.class);
    }


    private Member mockJunior;
    private Member mockSenior;
    private Mission mockMission;

    private Member getMockJunior() {
        if (mockJunior == null) {
            mockJunior = Member.builder()
                    .memberId(1L)
                    .nickName("junior")
                    .githubId("junior")
                    .profileImageUrl("junior")
                    .junior(Junior.builder().juniorId(1L).build())
                    .build();
        }
        return mockJunior;
    }

    private Member getMockSenior() {
        if (mockSenior == null) {
            mockSenior = Member.builder()
                    .memberId(1L)
                    .nickName("senior")
                    .githubId("senior")
                    .profileImageUrl("senior")
                    .senior(Senior.builder().seniorId(1L).build())
                    .build();
        }
        return mockSenior;
    }

    private Mission getMockMission() {
        if (mockMission == null) {
            mockMission = Mission.builder()
                    .missionId(1L)
                    .title("title")
                    .description("description")
                    .build();
        }
        return mockMission;
    }
}
