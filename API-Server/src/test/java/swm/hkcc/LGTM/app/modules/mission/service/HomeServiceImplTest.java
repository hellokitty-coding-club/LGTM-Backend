package swm.hkcc.LGTM.app.modules.mission.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import swm.hkcc.LGTM.app.modules.auth.constants.MemberType;
import swm.hkcc.LGTM.app.modules.member.service.MemberService;
import swm.hkcc.LGTM.app.modules.mission.constant.MissionContentType;
import swm.hkcc.LGTM.app.modules.mission.domain.MissionContentData;
import swm.hkcc.LGTM.app.modules.mission.domain.MissionContentSequence;
import swm.hkcc.LGTM.app.modules.mission.domain.holder.MissionItemHolder;
import swm.hkcc.LGTM.app.modules.mission.domain.serverDrivenUI.HomeServerDrivenUISequenceFactory;
import swm.hkcc.LGTM.app.modules.mission.dto.MissionDetailsDto;
import swm.hkcc.LGTM.app.modules.mission.dto.MissionDto;
import swm.hkcc.LGTM.app.modules.serverDrivenUI.ServerDrivenContent;
import swm.hkcc.LGTM.app.modules.serverDrivenUI.ServerDrivenScreenResponse;
import swm.hkcc.LGTM.app.modules.serverDrivenUI.constant.ABTest;
import swm.hkcc.LGTM.app.modules.serverDrivenUI.constant.ViewType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static swm.hkcc.LGTM.app.modules.mission.constant.MissionContentType.*;

class HomeServiceImplTest {

    @Mock
    private HomeServerDrivenUISequenceFactory sequenceFactory;

    @Mock
    private MissionItemHolder missionItemHolder;

    @Mock
    private MissionService missionService;

    @Mock
    private MemberService memberService;

    @InjectMocks
    private HomeServiceImpl homeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getHomeScreen_WithNonEmptyContents() {
        // given
        Long memberId = 1L;
        String ABTestGroupName = ABTest.HOME_SCREEN_SEQUENCE_TEST.getTestName();

        MemberType memberType = MemberType.JUNIOR;


        List<MissionContentType> mockMissionContentTypeList = List.of(
                MissionContentType.ON_GOING_MISSION_TITLE_V1,
                MissionContentType.ON_GOING_MISSION_LIST_V1,
                MissionContentType.SECTION_DARK_CLOSER_V1,
                MissionContentType.RECOMMENDED_MISSION_TITLE_V1,
                MissionContentType.RECOMMENDED_MISSION_LIST_V1,
                MissionContentType.SECTION_LIGHT_CLOSER_V1,
                MissionContentType.TOTAL_MISSION_TITLE_V1,
                MissionContentType.TOTAL_MISSION_LIST_V1,
                MissionContentType.SECTION_LIGHT_CLOSER_V1
        );
        MissionContentSequence mockContentSequence = new MissionContentSequence(mockMissionContentTypeList);
        when(sequenceFactory.getServerDrivenUISequence(ABTestGroupName)).thenReturn(mockContentSequence);
        when(memberService.getMemberType(memberId)).thenReturn(memberType);

        MissionContentData ongoingMissionContent = MissionContentData.of(List.of(createMockMissionDto(), createMockMissionDto()));
        MissionContentData recommendMissionContent = MissionContentData.of(List.of(createMockMissionDetailsDto(), createMockMissionDetailsDto()));
        MissionContentData totalMissionContent = MissionContentData.of(List.of(createMockMissionDetailsDto(), createMockMissionDetailsDto()));
        when(missionService.getJuniorOngoingMissions(anyLong())).thenReturn(ongoingMissionContent);
        when(missionService.getRecommendMissions(anyLong())).thenReturn(recommendMissionContent);
        when(missionService.getTotalMissions(anyLong())).thenReturn(totalMissionContent);

        when(missionItemHolder.getMissionListFunction(ON_GOING_MISSION_LIST_V1, memberType)).thenReturn(missionService::getJuniorOngoingMissions);
        when(missionItemHolder.getMissionListFunction(RECOMMENDED_MISSION_LIST_V1, memberType)).thenReturn(missionService::getRecommendMissions);
        when(missionItemHolder.getMissionListFunction(TOTAL_MISSION_LIST_V1, memberType)).thenReturn(missionService::getTotalMissions);


        // when
        ServerDrivenScreenResponse response = homeService.getHomeScreen(memberId, ABTestGroupName);

        // then
        assertThat(response).isNotNull();

        List<ServerDrivenContent> contents = response.getContents();
        assertThat(contents).isNotEmpty();

        int titleCount = 3;
        int closerCount = 3;
        int itemCount = ongoingMissionContent.getMissionData().size()
                + recommendMissionContent.getMissionData().size()
                + totalMissionContent.getMissionData().size();
        int expectedSize = titleCount + closerCount + itemCount;
        assertThat(contents).hasSize(expectedSize);

        List<ViewType> exoectedViewTypeList = List.of(
                ViewType.TITLE,
                ViewType.ITEM,
                ViewType.ITEM,
                ViewType.CLOSER,
                ViewType.TITLE,
                ViewType.ITEM,
                ViewType.ITEM,
                ViewType.CLOSER,
                ViewType.TITLE,
                ViewType.ITEM,
                ViewType.ITEM,
                ViewType.CLOSER
        );

        for (int i = 0; i < expectedSize; i++) {
            assertThat(contents.get(i).getViewTypeName()).isEqualTo(exoectedViewTypeList.get(i).getName());
        }
    }

    private MissionDto createMockMissionDto() {
        return Mockito.mock(MissionDto.class);
    }

    private MissionDetailsDto createMockMissionDetailsDto() {
        return Mockito.mock(MissionDetailsDto.class);
    }
}
