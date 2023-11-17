package swm.hkcc.LGTM.app.modules.mission.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import swm.hkcc.LGTM.app.modules.mission.domain.MissionView;

import java.util.List;

import static swm.hkcc.LGTM.app.modules.mission.domain.QMissionView.missionView;


@Slf4j
@Repository
@RequiredArgsConstructor
public class MissionViewCustomRepositoryImpl implements MissionViewCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<MissionView> findByOrderByViewCountDesc(int n) {
        return jpaQueryFactory
                .select(missionView)
                .from(missionView)
                .join(missionView.mission).fetchJoin() // missionView 캐싱 시 mission 정보까지 저장하기 위함
                .join(missionView.viewer).fetchJoin()  // missionView 캐싱 시 viewer 정보까지 저장하기 위함
                .groupBy(missionView.mission.missionId)
                .orderBy(missionView.mission.missionId.count().desc())
                .limit(n)
                .fetch();
    }
}
