package swm.hkcc.LGTM.app.modules.mission.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.mission.domain.Mission;
import swm.hkcc.LGTM.app.modules.mission.domain.MissionContentData;
import swm.hkcc.LGTM.app.modules.mission.domain.MissionStatus;
import swm.hkcc.LGTM.app.modules.mission.dto.MissionDetailsDto;
import swm.hkcc.LGTM.app.modules.mission.dto.MissionDto;
import swm.hkcc.LGTM.app.modules.mission.repository.*;
import swm.hkcc.LGTM.app.modules.registration.repository.MissionRegistrationRepository;
import swm.hkcc.LGTM.app.modules.tag.repository.TechTagPerMissionRepository;

import java.time.LocalDate;
import java.util.Arrays;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class MissionServiceImplTest {

    @InjectMocks
    private MissionServiceImpl missionService;

    @Mock
    private MissionRepository missionRepository;
    @Mock
    private MissionScrapRepository missionScrapRepository;
    @Mock
    private MissionViewRepository missionViewRepository;
    @Mock
    private MissionRegistrationRepository missionRegistrationRepository;
    @Mock
    private TechTagPerMissionRepository techTagPerMissionRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("진행중인 미션을 가져온다. MissionDto의 리스트를 담은 MissionContentData를 반환한다.")
    public void getOngoingMissions() {
        // Given
        when(missionRepository.getOnGoingMissions(1L))
                .thenReturn(Arrays.asList(createMockMission(1L), createMockMission(2L)));

        // When
        MissionContentData result = missionService.getOngoingMissions(1L);

        // Then
        assertEquals(2, result.getMissionData().size());
        assertAll(
                () -> assertThat(result.getMissionData().get(0)).isInstanceOf(MissionDto.class),
                () -> assertThat(result.getMissionData().get(1)).isInstanceOf(MissionDto.class)
        );
    }

    @Test
    @DisplayName("맞춤 추천 미션을 가져온다. MissionDetailDto의 리스트를 담은 MissionContentData를 반환한다.")
    public void getRecommendMissions() {
        // Given
        when(missionRepository.getTotalMissions())
                .thenReturn(Arrays.asList(createMockMission(1L), createMockMission(2L), createMockMission(3L)));

        // When
        MissionContentData result = missionService.getRecommendMissions(1L);

        // Then
        assertEquals(3, result.getMissionData().size());
        assertAll(
                () -> assertThat(result.getMissionData().get(0)).isInstanceOf(MissionDetailsDto.class),
                () -> assertThat(result.getMissionData().get(1)).isInstanceOf(MissionDetailsDto.class),
                () -> assertThat(result.getMissionData().get(2)).isInstanceOf(MissionDetailsDto.class)
        );
    }

    @Test
    @DisplayName("전체 미션을 가져온다. MissionDetailDto의 리스트를 담은 MissionContentData를 반환한다.")
    public void getTotalMissions() {
        // Given
        when(missionRepository.getTotalMissions())
                .thenReturn(Arrays.asList(createMockMission(1L), createMockMission(2L)));

        // When
        MissionContentData result = missionService.getTotalMissions(1L);

        // Then
        assertEquals(2, result.getMissionData().size());
        assertAll(
                () -> assertThat(result.getMissionData().get(0)).isInstanceOf(MissionDetailsDto.class),
                () -> assertThat(result.getMissionData().get(1)).isInstanceOf(MissionDetailsDto.class)
        );
    }

    private Mission createMockMission(Long id) {
        Member mockMember = Member.builder()
                .memberId(id)
                .githubId("TestGithubId")
                .githubOauthId(9999)
                .nickName("TestNickName")
                .refreshToken("TestRefreshToken")
                .deviceToken("TestDeviceToken")
                .profileImageUrl("TestProfileImageUrl")
                .introduction("TestIntroduction")
                .build();

        return Mission.builder()
                .missionId(id)
                .writer(mockMember)
                .missionRepositoryUrl("TestRepositoryUrl")
                .title("Test Mission ")
                .missionStatus(MissionStatus.MISSION_PROCEEDING)
                .thumbnailImageUrl("TestThumbnailUrl")
                .description("TestDescription")
                .reomnnandTo("TestRecommendTo")
                .notReomnnandTo("TestNotRecommendTo")
                .registrationDueDate(LocalDate.now())
                .assignmentDueDate(LocalDate.now())
                .reviewCompletationDueDate(LocalDate.now())
                .price(1000)
                .maxPeopleNumber(10)
                .build();
    }

}
