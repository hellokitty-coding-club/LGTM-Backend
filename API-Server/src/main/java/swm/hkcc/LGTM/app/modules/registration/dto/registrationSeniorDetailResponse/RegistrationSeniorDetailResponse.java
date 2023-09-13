package swm.hkcc.LGTM.app.modules.registration.dto.registrationSeniorDetailResponse;

import lombok.Data;
import lombok.NoArgsConstructor;
import swm.hkcc.LGTM.app.modules.registration.domain.ProcessStatus;
import swm.hkcc.LGTM.app.modules.registration.dto.MissionHistoryInfo;

import java.util.List;

@Data
@NoArgsConstructor
public class RegistrationSeniorDetailResponse {
    protected Long memberId;
    protected String nickname;
    protected String githubId;
    protected ProcessStatus status;
    protected List<MissionHistoryInfo> missionHistory;
    protected String buttonTitle;
}
