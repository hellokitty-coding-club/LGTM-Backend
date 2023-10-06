package swm.hkcc.LGTM.app.modules.registration.dto.registrationSeniorDetailResponse;

import lombok.Data;
import lombok.NoArgsConstructor;
import swm.hkcc.LGTM.app.modules.registration.domain.ProcessStatus;
import swm.hkcc.LGTM.app.modules.registration.dto.MissionHistoryInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class RegistrationSeniorDetailResponse {
    protected Long memberId;
    protected String nickname;
    protected String githubId;
    protected ProcessStatus status;
    protected Map<ProcessStatus,String> missionHistory;
    protected String buttonTitle;

    public void setMissionHistory(List<MissionHistoryInfo> missionHistory) {
        this.missionHistory = new HashMap<>();
        for (MissionHistoryInfo history : missionHistory) {
            this.missionHistory.put(history.getStatus(),history.getDateTime());
        }
    }
}
