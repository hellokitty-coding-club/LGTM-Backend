package swm.hkcc.LGTM.app.modules.mission.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swm.hkcc.LGTM.app.modules.auth.constants.MemberType;
import swm.hkcc.LGTM.app.modules.member.service.MemberService;
import swm.hkcc.LGTM.app.modules.mission.constant.MissionContentType;
import swm.hkcc.LGTM.app.modules.mission.domain.MissionContentData;
import swm.hkcc.LGTM.app.modules.mission.domain.MissionContentSequence;
import swm.hkcc.LGTM.app.modules.mission.domain.holder.MissionItemHolder;
import swm.hkcc.LGTM.app.modules.mission.domain.serverDrivenUI.HomeServerDrivenUISequenceFactory;
import swm.hkcc.LGTM.app.modules.mission.dto.MissionCloserDto;
import swm.hkcc.LGTM.app.modules.mission.dto.MissionTitleDto;
import swm.hkcc.LGTM.app.modules.mission.dto.SubItemDto;
import swm.hkcc.LGTM.app.modules.serverDrivenUI.ServerDrivenContent;
import swm.hkcc.LGTM.app.modules.serverDrivenUI.ServerDrivenContents;
import swm.hkcc.LGTM.app.modules.serverDrivenUI.ServerDrivenScreenResponse;
import swm.hkcc.LGTM.app.modules.serverDrivenUI.constant.ViewType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
@Service
public class HomeServiceImpl implements HomeService{
    private final HomeServerDrivenUISequenceFactory sequenceFactory;
    private final MissionItemHolder missionItemHolder;
    private final MemberService memberService;

    private static final String RESPONSE_SCREEN_NAME = "Home";

    @Override
    @Transactional(readOnly = true)
    public ServerDrivenScreenResponse getHomeScreen(Long memberId, String ABTestGroupName) {
        MissionContentSequence contentSequence = sequenceFactory.getServerDrivenUISequence(ABTestGroupName);

        List<ServerDrivenContent> serverDrivenContentList = new ArrayList<>();
        contentSequence.getMissionContents()
                .forEach(missionContentType -> processMissionContentType(memberId, missionContentType, serverDrivenContentList));

        return new ServerDrivenScreenResponse(RESPONSE_SCREEN_NAME, serverDrivenContentList);
    }

    @Override
    @Transactional(readOnly = true)
    public ServerDrivenScreenResponse getHomeScreenV2(Long memberId, String ABTestGroupName) {
        MissionContentSequence contentSequence = sequenceFactory.getServerDrivenUISequence(ABTestGroupName, 2);

        List<ServerDrivenContent> serverDrivenContentList = new ArrayList<>();
        contentSequence.getMissionContents()
                .forEach(missionContentType -> processMissionContentType(memberId, missionContentType, serverDrivenContentList));

        return new ServerDrivenScreenResponse(RESPONSE_SCREEN_NAME, serverDrivenContentList);
    }

    private void processMissionContentType(Long memberId, MissionContentType missionContentType, List<ServerDrivenContent> serverDrivenContentList) {
        if (missionContentType.getViewType() == ViewType.ITEM) {
            MemberType memberType = memberService.getMemberType(memberId);
            Function<Long, MissionContentData> missionListFunction = missionItemHolder.getMissionListFunction(missionContentType, memberType);
            MissionContentData missionContentData = missionListFunction.apply(memberId);

            ServerDrivenContents missionContents = ServerDrivenContents.of(
                    missionContentData.getMissionData().stream()
                            .map(missionData -> ServerDrivenContent.from(missionData, missionContentType.getTheme(), missionContentType.getViewType()))
                            .toList());
            addMissionContentsOrEmptyView(serverDrivenContentList, missionContents, missionContentType);
            return;
        }
        if (missionContentType.getViewType() == ViewType.TITLE) {
            MemberType memberType = memberService.getMemberType(memberId);
            serverDrivenContentList.add(getMissionTitle(missionContentType, memberType));
            return;
        }
        if (missionContentType.getViewType() == ViewType.SUB_ITEM) {
            MemberType memberType = memberService.getMemberType(memberId);
            serverDrivenContentList.add(getSubItem(missionContentType, memberType));
            return;
        }
        serverDrivenContentList.add(getMissionCloser(missionContentType));
    }

    private void addMissionContentsOrEmptyView(List<ServerDrivenContent> serverDrivenContentList, ServerDrivenContents missionContents, MissionContentType missionContentType) {
        if (missionContents.getContents().isEmpty()) {
            addEmptyView(serverDrivenContentList, missionContentType);
            return;
        }
        addMissionList(missionContents, serverDrivenContentList);
    }

    private void addEmptyView(List<ServerDrivenContent> serverDrivenContentList, MissionContentType missionContentType) {
            serverDrivenContentList.add(missionItemHolder.getMissionEmptyView(missionContentType));
    }

    private void addMissionList(ServerDrivenContents missionContents, List<ServerDrivenContent> contentList) {
        contentList.addAll(missionContents.getContents());
    }

    private ServerDrivenContent getMissionTitle(MissionContentType missionContentType, MemberType memberType) {
        return ServerDrivenContent.from(
                MissionTitleDto.builder()
                        .title(missionContentType.getTitleName(memberType))
                        .build(),
                missionContentType.getTheme(),
                missionContentType.getViewType()
        );
    }

    private ServerDrivenContent getMissionCloser(MissionContentType missionContentType) {
        return ServerDrivenContent.from(
                MissionCloserDto.builder()
                        .closer(missionContentType.getViewType().getName())
                        .build(),
                missionContentType.getTheme(),
                missionContentType.getViewType()
        );
    }

    private ServerDrivenContent getSubItem(MissionContentType missionContentType, MemberType memberType) {
        return ServerDrivenContent.from(
                    SubItemDto.builder()
                        .text(missionContentType.getTitleName(memberType))
                        .build(),
                missionContentType.getTheme(),
                missionContentType.getViewType()
        );
    }
}
