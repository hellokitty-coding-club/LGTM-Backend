package swm.hkcc.LGTM.app.modules.mission.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import swm.hkcc.LGTM.app.modules.mission.domain.Mission;
import swm.hkcc.LGTM.app.modules.mission.dto.MissionDetailsDto;
import swm.hkcc.LGTM.app.modules.mission.dto.MissionDto;
import swm.hkcc.LGTM.app.modules.registration.domain.PersonalStatus;
import swm.hkcc.LGTM.app.modules.registration.domain.repository.MissionRegistrationRepository;
import swm.hkcc.LGTM.app.modules.tag.domain.TechTag;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static swm.hkcc.LGTM.app.modules.member.domain.QMember.member;
import static swm.hkcc.LGTM.app.modules.mission.domain.QMission.mission;
import static swm.hkcc.LGTM.app.modules.registration.domain.QMissionRegistration.missionRegistration;
import static swm.hkcc.LGTM.app.modules.tag.domain.QTechTagPerMission.techTagPerMission;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MissionCustomRepositoryImpl implements MissionCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final MissionRepository missionRepository;
    private final MissionScrapRepository missionScrapRepository;
    private final MissionViewRepository missionViewRepository;
    private final MissionRegistrationRepository missionRegistrationRepository;

    @Override
    public List<MissionDto> getOnGoingMissions(Long memberId) {
        List<Mission> onGoingMissions = getMissions(isMemberParticipating(memberId), isNotFinished());

        return onGoingMissions.stream()
                .map(this::toMissionDto)
                .toList();
    }

    private List<Mission> getMissions(BooleanExpression isParticipating, BooleanExpression isNotFinished) {
        return jpaQueryFactory
                .select(mission)
                .from(mission)
                .join(missionRegistration).on(mission.missionId.eq(missionRegistration.mission.missionId))
                .join(member).on(member.memberId.eq(missionRegistration.junior.memberId))
                .where(isParticipating.and(isNotFinished))
                .fetch();
    }

    private List<Mission> getMissions(BooleanExpression isNotFinished) {
        return jpaQueryFactory
                .select(mission)
                .from(mission)
                .where(isNotFinished)
                .fetch();
    }

    private BooleanExpression isMemberParticipating(Long memberId) {
        return member.memberId.eq(memberId);
    }

    private BooleanExpression isNotFinished() {
        return missionRegistration.status.ne(PersonalStatus.MISSION_FINISHED);
    }

    private MissionDto toMissionDto(Mission ongoingMission) {
        List<TechTag> techTags = getTechTagList(ongoingMission.getMissionId());
        return MissionDto.builder()
                .missionId(ongoingMission.getMissionId())
                .missionTitle(ongoingMission.getTitle())
                .techTagList(techTags)
                .missionThumbnailUrl(ongoingMission.getThumbnailImageUrl())
                .build();
    }

    private List<TechTag> getTechTagList(Long missionId) {
        return jpaQueryFactory
                .select(techTagPerMission.techTag)
                .from(techTagPerMission)
                .where(techTagPerMission.mission.missionId.eq(missionId))
                .fetch();
    }

    // todo: get recommended missions
    @Override
    public List<MissionDetailsDto> getRecommendedMissions(Long memberId) {
        return null;
    }

    @Override
    public List<MissionDetailsDto> getTotalMissions(Long memberId) {
        List<Mission> missions = getMissions(isNotFinished());

        return missions.stream()
                .map(mission -> getMissionDetails(memberId, mission))
                .toList();
    }

    private MissionDetailsDto getMissionDetails(Long memberId, Mission mission) {
        return MissionDetailsDto.builder()
                .missionId(mission.getMissionId())
                .missionTitle(mission.getTitle())
                .techTagList(getTechTagList(mission.getMissionId()))
                .missionThumbnailUrl(mission.getThumbnailImageUrl())
                .remainingRegisterDays(remainingRegisterDays(mission))
                .viewCount(missionViewRepository.countByMission_MissionId(mission.getMissionId()))
                .price(mission.getPrice())
                .currentPeopleNumber(missionRegistrationRepository.countByMission_MissionId(mission.getMissionId()))
                .maxPeopleNumber(mission.getMaxPeopleNumber())
                .isScraped(missionScrapRepository.existsByScrapper_MemberIdAndMission_MissionId(memberId, mission.getMissionId()))
                .scrapCount(missionScrapRepository.countByMission_MissionId(mission.getMissionId()))
                .build();
    }

    private int remainingRegisterDays(Mission mission) {
        return (int) ChronoUnit.DAYS.between(LocalDate.now(), mission.getRegistrationDueDate());
    }

}
