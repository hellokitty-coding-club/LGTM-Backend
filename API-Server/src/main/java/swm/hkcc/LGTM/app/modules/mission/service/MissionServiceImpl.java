package swm.hkcc.LGTM.app.modules.mission.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import swm.hkcc.LGTM.app.modules.mission.domain.Mission;
import swm.hkcc.LGTM.app.modules.mission.domain.MissionContentData;
import swm.hkcc.LGTM.app.modules.mission.dto.MissionDetailsDto;
import swm.hkcc.LGTM.app.modules.mission.dto.MissionDto;
import swm.hkcc.LGTM.app.modules.mission.repository.MissionCustomRepository;
import swm.hkcc.LGTM.app.modules.mission.repository.MissionScrapRepository;
import swm.hkcc.LGTM.app.modules.mission.repository.MissionViewRepository;
import swm.hkcc.LGTM.app.modules.registration.domain.repository.MissionRegistrationRepository;
import swm.hkcc.LGTM.app.modules.tag.domain.TechTag;
import swm.hkcc.LGTM.app.modules.tag.repository.TechTagPerMissionRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class MissionServiceImpl implements MissionService {

    private final MissionCustomRepository missionCustomRepository;
    private final MissionScrapRepository missionScrapRepository;
    private final MissionViewRepository missionViewRepository;
    private final MissionRegistrationRepository missionRegistrationRepository;
    private final TechTagPerMissionRepository techTagPerMissionRepository;

    @Override
    public MissionContentData getOngoingMissions(Long memberId) {
        List<Mission> missions = missionCustomRepository.getOnGoingMissions(memberId);

        return MissionContentData.of(
                missions.stream()
                        .map(this::toMissionDto)
                        .toList()
        );
    }

    @Override // todo: 추천 미션 가져오기 구현 미완료 -> 전체 미션 가져오기로 임시 대체
    public MissionContentData getRecommendMissions(Long memberId) {
        List<Mission> missions = missionCustomRepository.getTotalMissions(memberId);

        return MissionContentData.of(
                missions.stream()
                        .map(mission -> toMissionDetailDto(memberId, mission))
                        .toList()
        );
    }

    @Override
    public MissionContentData getTotalMissions(Long memberId) {
        List<Mission> missions = missionCustomRepository.getTotalMissions(memberId);

        return MissionContentData.of(
                missions.stream()
                        .map(mission -> toMissionDetailDto(memberId, mission))
                        .toList()
        );
    }

    private MissionDto toMissionDto(Mission ongoingMission) {
        List<TechTag> techTags = techTagPerMissionRepository.findTechTagsByMissionId(ongoingMission.getMissionId());
        return MissionDto.builder()
                .missionId(ongoingMission.getMissionId())
                .missionTitle(ongoingMission.getTitle())
                .techTagList(techTags)
                .missionThumbnailUrl(ongoingMission.getThumbnailImageUrl())
                .build();
    }

    private MissionDetailsDto toMissionDetailDto(Long memberId, Mission mission) {
        List<TechTag> techTags = techTagPerMissionRepository.findTechTagsByMissionId(mission.getMissionId());
        int viewCount = missionViewRepository.countByMission_MissionId(mission.getMissionId());
        int currentPeopleNumber = missionRegistrationRepository.countByMission_MissionId(mission.getMissionId());
        boolean isScraped = missionScrapRepository.existsByScrapper_MemberIdAndMission_MissionId(memberId, mission.getMissionId());
        int scrapCount = missionScrapRepository.countByMission_MissionId(mission.getMissionId());

        return MissionDetailsDto.builder()
                .missionId(mission.getMissionId())
                .missionTitle(mission.getTitle())
                .techTagList(techTags)
                .missionThumbnailUrl(mission.getThumbnailImageUrl())
                .remainingRegisterDays(remainingRegisterDays(mission))
                .viewCount(viewCount)
                .price(mission.getPrice())
                .currentPeopleNumber(currentPeopleNumber)
                .maxPeopleNumber(mission.getMaxPeopleNumber())
                .isScraped(isScraped)
                .scrapCount(scrapCount)
                .build();
    }

    private int remainingRegisterDays(Mission mission) {
        return (int) ChronoUnit.DAYS.between(LocalDate.now(), mission.getRegistrationDueDate());
    }
}
