package swm.hkcc.LGTM.app.modules.mission.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import swm.hkcc.LGTM.app.modules.mission.domain.MissionContentData;
import swm.hkcc.LGTM.app.modules.mission.dto.MissionDetailsDto;
import swm.hkcc.LGTM.app.modules.mission.dto.MissionDto;
import swm.hkcc.LGTM.app.modules.mission.repository.MissionCustomRepository;
import swm.hkcc.LGTM.app.modules.mission.repository.MissionRepository;
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
    public MissionContentData getOngoingMissions(Long memberId) {
        List<MissionDto> missions = missionCustomRepository.getOnGoingMissions(memberId);
        return MissionContentData.of(missions);
    }

    @Override // todo: 추천 미션 가져오기 구현 미완료 -> 전체 미션 가져오기로 임시 대체
    public MissionContentData getRecommendMissions(Long memberId) {
        List<MissionDetailsDto> missions = missionCustomRepository.getTotalMissions(memberId);
        return MissionContentData.of(missions);
    }

    @Override
    public MissionContentData getTotalMissions(Long memberId) {
        List<MissionDetailsDto> missions = missionCustomRepository.getTotalMissions(memberId);
        return MissionContentData.of(missions);
    }
}
