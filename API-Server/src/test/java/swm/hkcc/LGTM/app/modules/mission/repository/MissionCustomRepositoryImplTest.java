package swm.hkcc.LGTM.app.modules.mission.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.member.repository.MemberRepository;
import swm.hkcc.LGTM.app.modules.mission.domain.Mission;
import swm.hkcc.LGTM.app.modules.mission.domain.MissionStatus;
import swm.hkcc.LGTM.app.modules.registration.domain.MissionRegistration;
import swm.hkcc.LGTM.app.modules.registration.domain.PersonalStatus;
import swm.hkcc.LGTM.app.modules.registration.repository.MissionRegistrationRepository;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class MissionCustomRepositoryImplTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MissionRepository missionRepository;

    @Autowired
    private MissionRegistrationRepository missionRegistrationRepository;

    private Member member;
    private Mission mission1, mission2, mission3;

    @BeforeEach
    @Transactional
    void setUp() {
        member = Member.builder()
                .githubId("githubTestUser1")
                .githubOauthId(123456)
                .nickName("testUser")
                .refreshToken("refreshToken")
                .deviceToken("deviceToken")
                .profileImageUrl("https://image/url")
                .introduction("Test user introduction")
                .build();

        member = memberRepository.save(member);

        mission1 = Mission.builder()
                .writer(member)
                .missionRepositoryUrl("https://github.com/test/repo1")
                .title("Test Mission 1")
                .missionStatus(MissionStatus.MISSION_PROCEEDING)
                .description("Test Description 1")
                .recommendTo("To Everyone")
                .notRecommendTo("To No One")
                .registrationDueDate(LocalDate.now())
                .price(100)
                .maxPeopleNumber(10)
                .build();

        mission2 = Mission.builder()
                .writer(member)
                .missionRepositoryUrl("https://github.com/test/repo2")
                .title("Test Mission 2")
                .missionStatus(MissionStatus.MISSION_PROCEEDING)
                .description("Test Description 2")
                .recommendTo("To Everyone")
                .notRecommendTo("To No One")
                .registrationDueDate(LocalDate.now())
                .price(200)
                .maxPeopleNumber(20)
                .build();

        mission3 = Mission.builder()
                .writer(member)
                .missionRepositoryUrl("https://github.com/test/repo3")
                .title("Test Mission 3")
                .missionStatus(MissionStatus.MISSION_PROCEEDING)
                .description("Test Description 3")
                .recommendTo("To Everyone")
                .notRecommendTo("To No One")
                .registrationDueDate(LocalDate.now())
                .price(300)
                .maxPeopleNumber(30)
                .build();

        mission1 = missionRepository.save(mission1);
        mission2 = missionRepository.save(mission2);
        mission3 = missionRepository.save(mission3);
    }

    @AfterEach
    @Transactional
    void tearDown() {
        missionRegistrationRepository.deleteAll();
        missionRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("현재 진행 중인 미션을 가져온다.")
    void getOnGoingMissions() {
        // Given
        MissionRegistration registration1 = MissionRegistration.builder()
                .status(PersonalStatus.WAITING_FOR_PAYMENT)
                .mission(mission1)
                .junior(member)
                .build();

        MissionRegistration registration3 = MissionRegistration.builder()
                .status(PersonalStatus.MISSION_PROCEEDING)
                .mission(mission3)
                .junior(member)
                .build();

        missionRegistrationRepository.save(registration1);
        missionRegistrationRepository.save(registration3);

        Long memberId = member.getMemberId();

        // When
        List<Mission> onGoingMissions = missionRepository.getOnGoingMissions(memberId);

        // Then
        assertThat(onGoingMissions).isNotNull();
        assertThat(onGoingMissions).hasSize(2); // Because we have registered member to 2 missions.
        assertThat(onGoingMissions).extracting("missionId").containsExactlyInAnyOrder(mission1.getMissionId(), mission3.getMissionId());
    }

    @Test
    @DisplayName("전체 미션을 가져온다.")
    void getTotalMissions() {
        // Given
        Long memberId = member.getMemberId();

        // When
        List<Mission> totalMissions = missionRepository.getTotalMissions();

        // Then
        assertThat(totalMissions).isNotNull();
        assertThat(totalMissions).hasSize(3);
        assertThat(totalMissions).extracting("missionId").containsExactlyInAnyOrder(mission1.getMissionId(), mission2.getMissionId(), mission3.getMissionId());
    }
}
