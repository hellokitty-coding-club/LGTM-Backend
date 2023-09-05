package swm.hkcc.LGTM.app.modules.mission.domain.holder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import swm.hkcc.LGTM.app.modules.auth.constants.MemberType;
import swm.hkcc.LGTM.app.modules.mission.constant.MissionContentType;
import swm.hkcc.LGTM.app.modules.mission.domain.MissionContentData;
import swm.hkcc.LGTM.app.modules.mission.dto.MissionDetailsDto;
import swm.hkcc.LGTM.app.modules.mission.dto.MissionDto;
import swm.hkcc.LGTM.app.modules.mission.dto.MissionEmptyViewTypeDto;
import swm.hkcc.LGTM.app.modules.mission.service.MissionService;
import swm.hkcc.LGTM.app.modules.serverDrivenUI.ServerDrivenContent;
import swm.hkcc.LGTM.app.modules.serverDrivenUI.ViewType;

import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class MissionItemHolderTest {

    @Mock
    private MissionService missionService;

    @InjectMocks
    private MissionItemHolder missionItemHolder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("MissionItemHolder는 MissionContentType에 따라 MissionContentData를 반환하는 함수를 반환한다.")
    void getMissionListFunction() {
        // given
        MissionContentType totalMissionContentType = MissionContentType.TOTAL_MISSION_LIST_V1;
        MissionContentType onGoingMissionContentType = MissionContentType.ON_GOING_MISSION_LIST_V1;
        MissionContentType recommendedMissionContentType = MissionContentType.RECOMMENDED_MISSION_LIST_V1;
        MemberType memberType = MemberType.JUNIOR;
        // Simulate the expected behavior of the missionService mock
        MissionContentData<MissionDto> onGoingMissionData = Mockito.mock(MissionContentData.class);
        MissionContentData<MissionDetailsDto> totalMissionData = Mockito.mock(MissionContentData.class);
        MissionContentData<MissionDetailsDto> recommendedMissionData = Mockito.mock(MissionContentData.class);
        when(missionService.getTotalMissions(anyLong())).thenReturn(totalMissionData);
        when(missionService.getJuniorOngoingMissions(anyLong())).thenReturn(onGoingMissionData);
        when(missionService.getRecommendMissions(anyLong())).thenReturn(recommendedMissionData);

        // when
        Function<Long, MissionContentData> totalMissionListFunction = missionItemHolder.getMissionListFunction(totalMissionContentType, memberType);
        Function<Long, MissionContentData> onGoingMissionListFunction = missionItemHolder.getMissionListFunction(onGoingMissionContentType, memberType);
        Function<Long, MissionContentData> recommendedMissionListFunction = missionItemHolder.getMissionListFunction(recommendedMissionContentType, memberType);

        // then
        assertThat(totalMissionListFunction).isNotNull();
        assertThat(onGoingMissionListFunction).isNotNull();
        assertThat(recommendedMissionListFunction).isNotNull();

        // Verify that the correct functions are returned and the methods on the missionService mock are called
        assertThat(totalMissionListFunction.apply(1L)).isEqualTo(totalMissionData);
        assertThat(onGoingMissionListFunction.apply(2L)).isEqualTo(onGoingMissionData);
        assertThat(recommendedMissionListFunction.apply(3L)).isEqualTo(recommendedMissionData);

        // Verify that the missionService methods are called with the correct arguments
        verify(missionService).getTotalMissions(1L);
        verify(missionService).getJuniorOngoingMissions(2L);
        verify(missionService).getRecommendMissions(3L);
    }

    @Test
    @DisplayName("MissionItemHolder는 Empty View를 결정할 때, MissionContentType에 따라 그에 맞는 Empty View를 담은 ServerDrivenContent를 반환한다.")
    void getMissionEmptyView() {
        // given
        MissionContentType totalMissionContentType = MissionContentType.TOTAL_MISSION_LIST_V1;
        MissionContentType onGoingMissionContentType = MissionContentType.ON_GOING_MISSION_LIST_V1;
        MissionContentType recommendedMissionContentType = MissionContentType.RECOMMENDED_MISSION_LIST_V1;

        // when
        ServerDrivenContent totalMissionEmptyView = missionItemHolder.getMissionEmptyView(totalMissionContentType);
        ServerDrivenContent onGoingMissionEmptyView = missionItemHolder.getMissionEmptyView(onGoingMissionContentType);
        ServerDrivenContent recommendedMissionEmptyView = missionItemHolder.getMissionEmptyView(recommendedMissionContentType);

        // then
        assertThat(totalMissionEmptyView.getContent())
                .isEqualTo(MissionEmptyViewTypeDto.builder()
                                        .emptyViewTypeName(MissionItemHolder.TOTAL_MISSION_EMPTY_VIEW)
                                        .build());
        assertThat(onGoingMissionEmptyView.getContent())
                .isEqualTo(MissionEmptyViewTypeDto.builder()
                                        .emptyViewTypeName(MissionItemHolder.ONGOING_MISSION_EMPTY_VIEW)
                                        .build());
        assertThat(recommendedMissionEmptyView.getContent())
                .isEqualTo(MissionEmptyViewTypeDto.builder()
                                        .emptyViewTypeName(MissionItemHolder.RECOMMENDED_MISSION_EMPTY_VIEW)
                                        .build());

        assertThat(totalMissionEmptyView.getViewTypeName()).isEqualTo(ViewType.EMPTY.getName());
        assertThat(onGoingMissionEmptyView.getViewTypeName()).isEqualTo(ViewType.EMPTY.getName());
        assertThat(recommendedMissionEmptyView.getViewTypeName()).isEqualTo(ViewType.EMPTY.getName());
    }
}
