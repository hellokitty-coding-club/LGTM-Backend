package swm.hkcc.LGTM.app.modules.mission.service;

import org.springframework.stereotype.Service;
import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.mission.domain.Mission;
import swm.hkcc.LGTM.app.modules.mission.dto.CreateMissionRequest;
import swm.hkcc.LGTM.app.modules.mission.dto.CreateMissionRequestV2;

@Service
public interface CreateMissionService {
    Mission createMission(Member writer, CreateMissionRequest requestBody);
    Mission createMission(Member writer, CreateMissionRequestV2 requestBody);
}
