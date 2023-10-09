package swm.hkcc.LGTM.app.modules.mission.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.mission.domain.Mission;
import swm.hkcc.LGTM.app.modules.mission.dto.UpdateMissionRequest;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class UpdateMissionService {
    public Mission updateMission(Member member, Long missionId, UpdateMissionRequest requestBody) {
    }
}
