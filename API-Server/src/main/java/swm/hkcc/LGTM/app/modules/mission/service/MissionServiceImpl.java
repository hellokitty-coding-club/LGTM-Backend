package swm.hkcc.LGTM.app.modules.mission.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import swm.hkcc.LGTM.app.modules.member.domain.Senior;
import swm.hkcc.LGTM.app.modules.member.exception.NotExistMember;
import swm.hkcc.LGTM.app.modules.member.repository.MemberRepository;
import swm.hkcc.LGTM.app.modules.member.repository.SeniorRepository;
import swm.hkcc.LGTM.app.modules.member.service.MemberService;
import swm.hkcc.LGTM.app.modules.mission.domain.Mission;
import swm.hkcc.LGTM.app.modules.mission.domain.MissionContentData;
import swm.hkcc.LGTM.app.modules.mission.domain.mapper.MissionMapper;
import swm.hkcc.LGTM.app.modules.mission.dto.MissionDetailViewResponse;
import swm.hkcc.LGTM.app.modules.mission.dto.MissionDetailsDto;
import swm.hkcc.LGTM.app.modules.mission.exception.NotExistMission;
import swm.hkcc.LGTM.app.modules.mission.repository.MissionRepository;
import swm.hkcc.LGTM.app.modules.mission.repository.MissionScrapRepository;
import swm.hkcc.LGTM.app.modules.mission.repository.MissionViewRepository;
import swm.hkcc.LGTM.app.modules.registration.repository.MissionRegistrationRepository;
import swm.hkcc.LGTM.app.modules.tag.domain.TechTag;
import swm.hkcc.LGTM.app.modules.tag.repository.TechTagPerMissionRepository;

import java.util.List;
import java.util.stream.Collectors;

import static swm.hkcc.LGTM.app.modules.mission.domain.mapper.MissionMapper.missionAndMemberToDetailView;

@Slf4j
@RequiredArgsConstructor
@Service
public class MissionServiceImpl implements MissionService {

    private final MissionRepository missionRepository;
    private final MissionScrapRepository missionScrapRepository;
    private final MissionViewRepository missionViewRepository;
    private final MissionRegistrationRepository missionRegistrationRepository;
    private final TechTagPerMissionRepository techTagPerMissionRepository;
    private final MemberRepository memberRepository;
    private final SeniorRepository seniorRepository;
    private final MemberService memberService;

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
        List<Mission> missions = missionRepository.getTotalMissions();

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

    @Override
    public MissionDetailViewResponse getMissionDetail(Long memberId, Long missionId) {
        Mission mission = missionRepository.findById(missionId).orElseThrow(NotExistMission::new);
        boolean isScraped = missionScrapRepository.existsByScrapper_MemberIdAndMission_MissionId(memberId, missionId);

        Long missionWriterId = mission.getWriter().getMemberId();
        Senior missionWriter = seniorRepository.findByMember_MemberId(missionWriterId).orElseThrow(NotExistMember::new);

        List<TechTag> techTagList = techTagPerMissionRepository.findTechTagsByMissionId(mission.getMissionId());
        int currentPeopleNumber = missionRegistrationRepository.countByMission_MissionId(mission.getMissionId());
        String memberType = memberService.getMemberType(memberId);
        boolean isParticipated = checkMemberIsParticipated(memberId, missionId, memberType);

        return missionAndMemberToDetailView(mission, isScraped, missionWriter, techTagList, currentPeopleNumber, memberType, isParticipated);
    }

    // 미완성
    @Override // todo: 추천 미션 가져오기 구현 미완료 -> 전체 미션 가져오기로 임시 대체
    public MissionContentData getRecommendMissions(Long memberId) {
        List<Mission> missions = missionRepository.getTotalMissions();

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

    private boolean checkMemberIsParticipated(Long memberId, Long missionId, String memberType) {
        if (memberType.equals("JUNIOR")) {
            return missionRegistrationRepository.existsByMissionIdAndMemberId(missionId, memberId);
        }
        return missionRepository.existsByMissionIdAndWriter_MemberId(missionId, memberId);
    }

}
