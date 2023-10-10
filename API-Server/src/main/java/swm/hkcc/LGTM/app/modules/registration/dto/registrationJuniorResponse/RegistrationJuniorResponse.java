package swm.hkcc.LGTM.app.modules.registration.dto.registrationJuniorResponse;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import swm.hkcc.LGTM.app.modules.registration.domain.ProcessStatus;
import swm.hkcc.LGTM.app.modules.registration.dto.MissionHistoryInfo;
import swm.hkcc.LGTM.app.modules.tag.domain.TechTag;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuperBuilder
@Data
@NoArgsConstructor
public class RegistrationJuniorResponse {
    protected String missionName;
    protected List<TechTag> techTagList;
    protected ProcessStatus processStatus;
    protected Map<ProcessStatus,String> missionHistory;
    protected String buttonTitle;

    public void setMissionHistory(List<MissionHistoryInfo> missionHistory) {
        this.missionHistory = new HashMap<>();
        for (MissionHistoryInfo history : missionHistory) {
            this.missionHistory.put(history.getStatus(),history.getDateTime());
        }
    }
}
