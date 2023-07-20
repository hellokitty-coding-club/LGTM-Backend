package swm.hkcc.LGTM.app.modules.mission.service;

import org.springframework.stereotype.Service;
import swm.hkcc.LGTM.app.modules.mission.dto.CreateMissionRequest;
import swm.hkcc.LGTM.app.modules.mission.dto.CreateMissionResponse;

@Service
public interface CreateMissionService {
    CreateMissionResponse createMission(String memberGithubId, CreateMissionRequest requestBody);
}
