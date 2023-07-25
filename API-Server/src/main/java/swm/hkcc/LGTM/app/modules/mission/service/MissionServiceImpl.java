package swm.hkcc.LGTM.app.modules.mission.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import swm.hkcc.LGTM.app.modules.mission.dto.MissionDto;
import swm.hkcc.LGTM.app.modules.mission.repository.MissionCustomRepository;
import swm.hkcc.LGTM.app.modules.serverDrivenUI.ServerDrivenContent;
import swm.hkcc.LGTM.app.modules.serverDrivenUI.ServerDrivenContents;
import swm.hkcc.LGTM.app.modules.serverDrivenUI.Theme;
import swm.hkcc.LGTM.app.modules.serverDrivenUI.ViewType;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class MissionServiceImpl implements MissionService {

    private final MissionCustomRepository missionCustomRepository;

    @Override
    public ServerDrivenContents getOngoingMissions(Long memberId) {
        List<MissionDto> missions = missionCustomRepository.getOnGoingMissions(memberId);

        return ServerDrivenContents.of(
                missions.stream()
                        .map(missionDto -> ServerDrivenContent.from(missionDto, Theme.DARK, ViewType.ITEM))
                        .toList());
    }

    @Override
    public ServerDrivenContents getRecommendMissions(Long memberId) {
        return null;
    }

    @Override
    public ServerDrivenContents getTotalMissions(Long memberId) {
        return null;
    }
}
