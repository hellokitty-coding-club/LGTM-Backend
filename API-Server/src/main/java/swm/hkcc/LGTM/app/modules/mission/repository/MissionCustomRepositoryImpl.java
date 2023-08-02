package swm.hkcc.LGTM.app.modules.mission.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import swm.hkcc.LGTM.app.modules.mission.domain.Mission;
import swm.hkcc.LGTM.app.modules.registration.domain.PersonalStatus;

import java.util.List;

import static swm.hkcc.LGTM.app.modules.member.domain.QMember.member;
import static swm.hkcc.LGTM.app.modules.mission.domain.QMission.mission;
import static swm.hkcc.LGTM.app.modules.registration.domain.QMissionRegistration.missionRegistration;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MissionCustomRepositoryImpl implements MissionCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Mission> getOnGoingMissions(Long memberId) {
        return getMissions(isMemberParticipating(memberId), isNotFinished());
    }

    @Override // todo: get recommended missions
    public List<Mission> getRecommendedMissions(Long memberId) {
        return null;
    }

    @Override
    public List<Mission> getTotalMissions(Long memberId) {
        return getMissions(isNotFinished());
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
                .join(missionRegistration).on(mission.missionId.eq(missionRegistration.mission.missionId))
                .where(isNotFinished)
                .fetch();
    }

    private BooleanExpression isMemberParticipating(Long memberId) {
        return member.memberId.eq(memberId);
    }

    private BooleanExpression isNotFinished() {
        return missionRegistration.status.ne(PersonalStatus.MISSION_FINISHED);
    }

}
