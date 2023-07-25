package swm.hkcc.LGTM.app.modules.mission.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import swm.hkcc.LGTM.app.modules.mission.domain.Mission;
import swm.hkcc.LGTM.app.modules.mission.dto.MissionDetailsDto;
import swm.hkcc.LGTM.app.modules.mission.dto.MissionDto;
import swm.hkcc.LGTM.app.modules.registration.domain.PersonalStatus;
import swm.hkcc.LGTM.app.modules.tag.domain.TechTag;

import java.util.List;
import java.util.stream.Collectors;

import static swm.hkcc.LGTM.app.modules.member.domain.QMember.member;
import static swm.hkcc.LGTM.app.modules.mission.domain.QMission.mission;
import static swm.hkcc.LGTM.app.modules.registration.domain.QMissionRegistration.missionRegistration;
import static swm.hkcc.LGTM.app.modules.tag.domain.QTechTagPerMission.techTagPerMission;

@Repository
@RequiredArgsConstructor
public class MissionCustomRepositoryImpl implements MissionCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final MissionRepository missionRepository;

    @Override
    public List<MissionDto> getOnGoingMissions(Long memberId) {
        List<Mission> onGoingMissions = getMissions(isMemberParticipating(memberId), isNotFinished());

        return onGoingMissions.stream()
                .map(this::toMissionDto)
                .collect(Collectors.toList());
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

    // todo: get total missions
    @Override
    public List<MissionDetailsDto> getTotalMissions(Long memberId) {
//        List<Mission> totalMissions = getMissions(isNotFinished());
//
//        return totalMissions.stream()
//                .map(this::toMissionDetailsDto)
//                .collect(Collectors.toList());
//
        return null;
    }

//    private MissionDetailsDto toMissionDetailsDto(Mission d) {
//        Long missionId = d.getMissionId();
//
//        return jpaQueryFactory
//                .select(Projections.constructor(MissionDetailsDto.class,
//                        mission.missionId,
//                        mission.title,
//                        mission.thumbnailImageUrl,
//                        remainingRegisterDays(d),
//                        viewCount(),
//                        mission.price,
//                        currentPeopleNumber(),
//                        mission.maxPeopleNumber,
//                        isScraped(1L),
//                        scrapCount()))
//                .from(mission)
//                .where(mission.missionId.eq(missionId))
//                .fetchOne();
//    }
//
//    private NumberExpression<Integer> remainingRegisterDays(Mission mission) {
//        return Expressions.asNumber(ChronoUnit.DAYS.between(LocalDate.now(), mission.getRegistrationDueDate()))
//                .intValue();
//    }
//
//    private JPQLQuery<Long> viewCount() {
//        return JPAExpressions
//                .select(missionView.count())
//                .from(missionView)
//                .where(missionView.mission.eq(mission));
//    }
//
//    private JPQLQuery<Long> currentPeopleNumber() {
//        return JPAExpressions
//                .select(missionRegistration.count())
//                .from(missionRegistration)
//                .where(missionRegistration.mission.eq(mission));
//    }
//
//    private JPQLQuery<Long> scrapCount() {
//        return JPAExpressions
//                .select(missionScrap.count())
//                .from(missionScrap)
//                .where(missionScrap.mission.eq(mission));
//    }
//
//    private BooleanExpression isScraped(Long memberId) {
//        return JPAExpressions
//                .selectFrom(missionScrap)
//                .where(missionScrap.mission.eq(mission)
//                        .and(missionScrap.scrapper.memberId.eq(memberId)))
//                .exists();
//    }

}
