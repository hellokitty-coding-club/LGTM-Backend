package swm.hkcc.LGTM.app.modules.mission.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import swm.hkcc.LGTM.app.modules.mission.constant.MissionContentType;
import swm.hkcc.LGTM.app.modules.mission.domain.MissionContentSequence;
import swm.hkcc.LGTM.app.modules.mission.domain.holder.MissionItemHolder;
import swm.hkcc.LGTM.app.modules.mission.domain.holder.MissionTitleHolder;
import swm.hkcc.LGTM.app.modules.mission.domain.serverDrivenUI.HomeServerDrivenUISequenceFactory;
import swm.hkcc.LGTM.app.modules.serverDrivenUI.ServerDrivenContent;
import swm.hkcc.LGTM.app.modules.serverDrivenUI.ServerDrivenContents;
import swm.hkcc.LGTM.app.modules.serverDrivenUI.ServerDrivenScreenResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
@Service
public class HomeServiceImpl implements HomeService{

    private final HomeServerDrivenUISequenceFactory sequenceFactory;
    private final MissionItemHolder missionItemHolder;
    private final MissionTitleHolder missionTitleHolder;

    private static final String RESPONSE_SCREEN_NAME = "Home";

    @Override
    public ServerDrivenScreenResponse getHomeScreen(Long memberId, int version) {
        MissionContentSequence contentSequence = sequenceFactory.getServerDrivenUISequenceByVersion(version);
        List<ServerDrivenContent> serverDrivenContentList = new ArrayList<>();

        contentSequence.getMissionContents()
                .forEach(missionContentType -> processMissionContentType(memberId, missionContentType, serverDrivenContentList));

        return new ServerDrivenScreenResponse(RESPONSE_SCREEN_NAME, serverDrivenContentList);
    }

    private void processMissionContentType(Long memberId, MissionContentType missionContentType, List<ServerDrivenContent> serverDrivenContentList) {
        Function<Long, ServerDrivenContents> missionListFunction = missionItemHolder.getMissionListFunction(missionContentType);

        if (missionListFunction != null) {
            addMissionList(missionListFunction.apply(memberId), serverDrivenContentList);
            return;
        }
        serverDrivenContentList.add(missionTitleHolder.getMissionTitle(missionContentType));
    }

    private void addMissionList(ServerDrivenContents missionContents, List<ServerDrivenContent> contentList) {
        contentList.addAll(missionContents.getContents());
    }
}
