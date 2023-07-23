package swm.hkcc.LGTM.app.modules.mission.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import swm.hkcc.LGTM.app.modules.mission.constant.HomeServerDrivenUISequenceByVersion;
import swm.hkcc.LGTM.app.modules.mission.domain.serverDrivenUI.HomeServerDrivenUISequenceFactory;
import swm.hkcc.LGTM.app.modules.mission.repository.MissionRepository;
import swm.hkcc.LGTM.app.modules.serverDrivenUI.ServerDrivenScreenResponse;

@Slf4j
@RequiredArgsConstructor
@Service
public class HomeServiceImpl implements HomeService{

    private final HomeServerDrivenUISequenceFactory homeServerDrivenUISequenceFactory;
    private final MissionRepository missionRepository;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public ServerDrivenScreenResponse getHomeScreen(Long memberId, int version) {
        return null;
    }
}
