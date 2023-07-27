package swm.hkcc.LGTM.app.modules.mission.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import swm.hkcc.LGTM.app.global.constant.ResponseCode;
import swm.hkcc.LGTM.app.global.exception.GeneralException;
import swm.hkcc.LGTM.app.modules.mission.domain.MissionContentSequence;
import swm.hkcc.LGTM.app.modules.mission.domain.serverDrivenUI.HomeServerDrivenUISequenceFactory;
import swm.hkcc.LGTM.app.modules.mission.dto.MissionTitleDto;
import swm.hkcc.LGTM.app.modules.serverDrivenUI.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class HomeServiceImpl implements HomeService{

    private final HomeServerDrivenUISequenceFactory sequenceFactory;
    private final MissionService missionService;

    private static final String TOTAL_MISSION_TITLE_TEXT = "더 많은 미션 찾아보기";
    private static final String ONGOING_MISSION_TITLE_TEXT = "진행중인 미션";
    private static final String RECOMMENDED_MISSION_TITLE_TEXT = "맞춤 추천 미션 목록이에요";
    private static final String RESPONSE_SCREEN_NAME = "Home";

    @Override
    public ServerDrivenScreenResponse getHomeScreen(Long memberId, int version) {
        MissionContentSequence contentSequence = sequenceFactory.getServerDrivenUISequenceByVersion(version);
        List<ServerDrivenContent> serverDrivenContentList = new ArrayList<>();
        contentSequence.getMissionContents()
                .forEach(missionContentType -> {
                    switch (missionContentType) {
                        case TOTAL_MISSION_TITLE -> serverDrivenContentList.add(getMissionTitle(TOTAL_MISSION_TITLE_TEXT, Theme.LIGHT));
                        case ON_GOING_MISSION_TITLE -> serverDrivenContentList.add(getMissionTitle(ONGOING_MISSION_TITLE_TEXT, Theme.DARK));
                        case RECOMMENDED_MISSION_TITLE -> serverDrivenContentList.add(getMissionTitle(RECOMMENDED_MISSION_TITLE_TEXT, Theme.LIGHT));
                        case TOTAL_MISSION_LIST -> addMissionList(missionService.getTotalMissions(memberId), serverDrivenContentList);
                        case ON_GOING_MISSION_LIST -> addMissionList(missionService.getOngoingMissions(memberId), serverDrivenContentList);
                        case RECOMMENDED_MISSION_LIST -> addMissionList(missionService.getRecommendMissions(memberId), serverDrivenContentList);
                        case SECTION_CLOSER -> serverDrivenContentList.add(ServerDrivenContent.from(Theme.LIGHT, ViewType.CLOSER));
                        default -> throw new GeneralException(ResponseCode.DATA_ACCESS_ERROR, missionContentType.getClass().getName());
                    }
                });

        return new ServerDrivenScreenResponse(RESPONSE_SCREEN_NAME, serverDrivenContentList);
    }

    private ServerDrivenContent getMissionTitle(String title, Theme theme) {
        MissionTitleDto missionTitleDto = MissionTitleDto.builder()
                .title(title)
                .build();
        return ServerDrivenContent.from(missionTitleDto, theme, ViewType.TITLE);
    }

    private void addMissionList(ServerDrivenContents missionContents, List<ServerDrivenContent> contentList) {
        contentList.addAll(missionContents.getContents());
    }
}
