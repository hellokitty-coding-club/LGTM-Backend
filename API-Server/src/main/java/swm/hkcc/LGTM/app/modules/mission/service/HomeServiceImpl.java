package swm.hkcc.LGTM.app.modules.mission.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swm.hkcc.LGTM.app.modules.mission.constant.MissionContentType;
import swm.hkcc.LGTM.app.modules.mission.domain.MissionContentSequence;
import swm.hkcc.LGTM.app.modules.mission.domain.holder.MissionCloserHolder;
import swm.hkcc.LGTM.app.modules.mission.domain.holder.MissionItemHolder;
import swm.hkcc.LGTM.app.modules.mission.domain.holder.MissionTitleHolder;
import swm.hkcc.LGTM.app.modules.mission.domain.serverDrivenUI.HomeServerDrivenUISequenceFactory;
import swm.hkcc.LGTM.app.modules.serverDrivenUI.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
@Service
public class HomeServiceImpl implements HomeService{

    public static final MissionContentType DARK_MISSION_CONTENT_TYPE = MissionContentType.ON_GOING_MISSION_LIST;
    private final HomeServerDrivenUISequenceFactory sequenceFactory;
    private final MissionItemHolder missionItemHolder;
    private final MissionTitleHolder missionTitleHolder;
    private final MissionCloserHolder missionCloserHolder;

    private static final String RESPONSE_SCREEN_NAME = "Home";

    @Override
    @Transactional(readOnly = true)
    public ServerDrivenScreenResponse getHomeScreen(Long memberId, int version) {
        MissionContentSequence contentSequence = sequenceFactory.getServerDrivenUISequenceByVersion(version);
        List<ServerDrivenContent> serverDrivenContentList = new ArrayList<>();

        contentSequence.getMissionContents()
                .forEach(missionContentType -> processMissionContentType(memberId, missionContentType, serverDrivenContentList));

        return new ServerDrivenScreenResponse(RESPONSE_SCREEN_NAME, serverDrivenContentList);
    }

    private void processMissionContentType(Long memberId, MissionContentType missionContentType, List<ServerDrivenContent> serverDrivenContentList) {
        if (missionContentType.getViewType() == ViewType.ITEM) {
            Function<Long, ServerDrivenContents> missionListFunction = missionItemHolder.getMissionListFunction(missionContentType);
            ServerDrivenContents missionContents = missionListFunction.apply(memberId);
            addMissionContentsOrEmptyView(serverDrivenContentList, missionContents, missionContentType);
            return;
        }
        if (missionContentType.getViewType() == ViewType.TITLE) {
            serverDrivenContentList.add(missionTitleHolder.getMissionTitle(missionContentType));
            return;
        }
        serverDrivenContentList.add(missionCloserHolder.getMissionCloser(missionContentType));
    }

    private void addMissionContentsOrEmptyView(List<ServerDrivenContent> serverDrivenContentList, ServerDrivenContents missionContents, MissionContentType missionContentType) {
        if (missionContents.getContents().isEmpty()) {
            addEmptyView(serverDrivenContentList, missionContentType);
            return;
        }
        addMissionList(missionContents, serverDrivenContentList);
    }

    private static void addEmptyView(List<ServerDrivenContent> serverDrivenContentList, MissionContentType missionContentType) {
        if (missionContentType == DARK_MISSION_CONTENT_TYPE)
            serverDrivenContentList.add(ServerDrivenContent.from(Theme.DARK, ViewType.EMPTY));
        else
            serverDrivenContentList.add(ServerDrivenContent.from(Theme.LIGHT, ViewType.EMPTY));
    }

    private void addMissionList(ServerDrivenContents missionContents, List<ServerDrivenContent> contentList) {
        contentList.addAll(missionContents.getContents());
    }
}
