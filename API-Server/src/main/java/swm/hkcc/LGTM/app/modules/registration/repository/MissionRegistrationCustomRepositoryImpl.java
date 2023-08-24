package swm.hkcc.LGTM.app.modules.registration.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.mission.domain.Mission;
import swm.hkcc.LGTM.app.modules.registration.domain.MissionHistory;
import swm.hkcc.LGTM.app.modules.registration.domain.ProcessStatus;
import swm.hkcc.LGTM.app.modules.registration.dto.MemberRegisterSimpleInfo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static swm.hkcc.LGTM.app.modules.member.domain.QMember.member;
import static swm.hkcc.LGTM.app.modules.mission.domain.QMission.mission;
import static swm.hkcc.LGTM.app.modules.registration.domain.QMissionHistory.missionHistory;
import static swm.hkcc.LGTM.app.modules.registration.domain.QMissionRegistration.missionRegistration;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MissionRegistrationCustomRepositoryImpl implements MissionRegistrationCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public List<MemberRegisterSimpleInfo> getRegisteredMembersByMission(Long missionId) {
        List<Tuple> tuples = jpaQueryFactory.select(member.memberId, member.nickName, member.githubId, member.profileImageUrl, missionRegistration.githubPullRequestUrl, missionRegistration.status)
                .from(mission)
                .innerJoin(missionRegistration).on(mission.missionId.eq(missionRegistration.mission.missionId))
                .leftJoin(member).on(missionRegistration.junior.memberId.eq(member.memberId))
                .where(mission.missionId.eq(missionId))
                .orderBy(missionRegistration.createdAt.asc())
                .fetch();

        return tuples.stream().map(MemberRegisterSimpleInfo::createMemberRegisterInfo).collect(Collectors.toList());
    }

    @Override
    public Optional<LocalDateTime> getStatusDateTime(ProcessStatus status, Mission mission, Member junior) {
        return Optional.ofNullable(jpaQueryFactory.select(missionHistory.createdAt)
                .from(missionRegistration)
                .leftJoin(missionHistory).on(missionRegistration.eq(missionHistory.registration))
                .where(missionRegistration.mission.eq(mission), missionRegistration.junior.eq(junior), missionRegistration.status.eq(status))
                .fetchOne());
    }
}
