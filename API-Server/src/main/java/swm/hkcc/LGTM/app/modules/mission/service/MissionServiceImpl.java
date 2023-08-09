package swm.hkcc.LGTM.app.modules.mission.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import swm.hkcc.LGTM.app.modules.mission.domain.Mission;
import swm.hkcc.LGTM.app.modules.mission.domain.MissionContentData;
import swm.hkcc.LGTM.app.modules.mission.domain.mapper.MissionMapper;
import swm.hkcc.LGTM.app.modules.mission.dto.MissionDetailsDto;
import swm.hkcc.LGTM.app.modules.mission.repository.MissionRepository;
import swm.hkcc.LGTM.app.modules.mission.repository.MissionScrapRepository;
import swm.hkcc.LGTM.app.modules.mission.repository.MissionViewRepository;
import swm.hkcc.LGTM.app.modules.registration.domain.repository.MissionRegistrationRepository;
import swm.hkcc.LGTM.app.modules.tag.domain.TechTag;
import swm.hkcc.LGTM.app.modules.tag.repository.TechTagPerMissionRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class MissionServiceImpl implements MissionService {

    private final MissionRepository missionRepository;
    private final MissionScrapRepository missionScrapRepository;
    private final MissionViewRepository missionViewRepository;
    private final MissionRegistrationRepository missionRegistrationRepository;
    private final TechTagPerMissionRepository techTagPerMissionRepository;

    @Override
    public MissionContentData getOngoingMissions(Long memberId) {
        List<Mission> missions = missionRepository.getOnGoingMissions(memberId);

        return MissionContentData.of(
                missions.stream()
                        .map(mission -> MissionMapper.missionToMissionDto(
                                mission,
                                techTagPerMissionRepository.findTechTagsByMissionId(mission.getMissionId())
                        ))
                        .toList()
        );
    }

    @Override
    public MissionContentData getTotalMissions(Long memberId) {
        List<Mission> missions = missionRepository.getTotalMissions(memberId);

        List<MissionDetailsDto> missionDetailsDtos = missions.stream()
                .map(mission -> {
                    List<TechTag> techTags = techTagPerMissionRepository.findTechTagsByMissionId(mission.getMissionId());
                    int viewCount = missionViewRepository.countByMission_MissionId(mission.getMissionId());
                    int currentPeopleNumber = missionRegistrationRepository.countByMission_MissionId(mission.getMissionId());
                    boolean isScraped = missionScrapRepository.existsByScrapper_MemberIdAndMission_MissionId(memberId, mission.getMissionId());
                    int scrapCount = missionScrapRepository.countByMission_MissionId(mission.getMissionId());

                    return MissionMapper.missionToMissionDetailDto(
                            mission,
                            techTags,
                            viewCount,
                            currentPeopleNumber,
                            isScraped,
                            scrapCount
                    );
                })
                .collect(Collectors.toList());

        return MissionContentData.of(missionDetailsDtos);
    }

    // 미완성
    @Override // todo: 추천 미션 가져오기 구현 미완료 -> 전체 미션 가져오기로 임시 대체
    public MissionContentData getRecommendMissions(Long memberId) {
        List<Mission> missions = missionRepository.getTotalMissions(memberId);

        List<MissionDetailsDto> missionDetailsDtos = missions.stream()
                .map(mission -> {
                    List<TechTag> techTags = techTagPerMissionRepository.findTechTagsByMissionId(mission.getMissionId());
                    int viewCount = missionViewRepository.countByMission_MissionId(mission.getMissionId());
                    int currentPeopleNumber = missionRegistrationRepository.countByMission_MissionId(mission.getMissionId());
                    boolean isScraped = missionScrapRepository.existsByScrapper_MemberIdAndMission_MissionId(memberId, mission.getMissionId());
                    int scrapCount = missionScrapRepository.countByMission_MissionId(mission.getMissionId());

                    return MissionMapper.missionToMissionDetailDto(
                            mission,
                            techTags,
                            viewCount,
                            currentPeopleNumber,
                            isScraped,
                            scrapCount
                    );
                })
                .collect(Collectors.toList());

        return MissionContentData.of(missionDetailsDtos);
    }

}
