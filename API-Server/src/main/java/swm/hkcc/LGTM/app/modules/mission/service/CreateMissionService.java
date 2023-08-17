package swm.hkcc.LGTM.app.modules.mission.service;

import org.springframework.stereotype.Service;
import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.mission.domain.Mission;
import swm.hkcc.LGTM.app.modules.mission.dto.CreateMissionRequest;

@Service
public interface CreateMissionService {
    Mission createMission(Member member, CreateMissionRequest requestBody);
}
