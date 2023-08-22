package swm.hkcc.LGTM.app.modules.mission.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import swm.hkcc.LGTM.app.modules.mission.domain.Mission;
import swm.hkcc.LGTM.app.modules.mission.domain.MissionStatus;
import swm.hkcc.LGTM.app.modules.registration.domain.ProcessStatus;

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
    @Cacheable(value = "on_going_missions", key = "#memberId")
    public List<Mission> getOnGoingMissions(Long memberId) {
        return getMissions(isMemberParticipating(memberId), isNotCompleted());
    }

    @Override // todo: get recommended missions
    public List<Mission> getRecommendedMissions () {
        return null;
    }

    @Override
    @Cacheable(value = "total_missions")
    public List<Mission> getTotalMissions() {
        return getMissions(isMissionNotFinished());
    }

    private List<Mission> getMissions(BooleanExpression isParticipating, BooleanExpression isNotCompleted) {
        return jpaQueryFactory
                .select(mission)
                .from(mission)
                .join(missionRegistration).on(mission.missionId.eq(missionRegistration.mission.missionId))
                .join(member).on(member.memberId.eq(missionRegistration.junior.memberId))
                .where(isParticipating.and(isNotCompleted))
                .limit(3)
                .fetch();
    }

    private List<Mission> getMissions(BooleanExpression isMissionNotFinished) {
        return jpaQueryFactory
                .select(mission)
                .from(mission)
                .where(isMissionNotFinished)
                .limit(3)
                .fetch();
    }

    private BooleanExpression isMemberParticipating(Long memberId) {
        return member.memberId.eq(memberId);
    }

    private BooleanExpression isNotCompleted() {
        return missionRegistration.status.ne(ProcessStatus.MISSION_FINISHED);
    }

    private BooleanExpression isMissionNotFinished() {
        return mission.missionStatus.ne(MissionStatus.MISSION_FINISHED);
    }
}
