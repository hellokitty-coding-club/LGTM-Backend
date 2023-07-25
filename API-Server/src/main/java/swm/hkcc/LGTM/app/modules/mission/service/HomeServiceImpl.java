package swm.hkcc.LGTM.app.modules.mission.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import swm.hkcc.LGTM.app.modules.mission.domain.MissionContentSequence;
import swm.hkcc.LGTM.app.modules.mission.domain.serverDrivenUI.HomeServerDrivenUISequenceFactory;
import swm.hkcc.LGTM.app.modules.serverDrivenUI.ServerDrivenContents;
import swm.hkcc.LGTM.app.modules.serverDrivenUI.ServerDrivenScreenResponse;

@Slf4j
@RequiredArgsConstructor
@Service
public class HomeServiceImpl implements HomeService{

    private final HomeServerDrivenUISequenceFactory homeServerDrivenUISequenceFactory;
    private final MissionService missionService;

    @Override
    public ServerDrivenScreenResponse getHomeScreen(Long memberId, int version) {
        MissionContentSequence contentSequence = homeServerDrivenUISequenceFactory.createMissionContentSequenceFromVersion(version);

        ServerDrivenContents onGoingMissionList = missionService.getOngoingMissions(memberId);
        ServerDrivenContents recommendMissionList = missionService.getRecommendMissions(memberId);
        ServerDrivenContents totalMissionList = missionService.getTotalMissions(memberId);

        // todo: sequence에 맞춰서 최종적인 ServerDrivenScreenResponse 만들기

        return null;
    }
}
