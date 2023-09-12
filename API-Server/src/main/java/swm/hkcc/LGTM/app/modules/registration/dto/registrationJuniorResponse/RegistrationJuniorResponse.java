package swm.hkcc.LGTM.app.modules.registration.dto.registrationJuniorResponse;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import swm.hkcc.LGTM.app.modules.registration.domain.ProcessStatus;
import swm.hkcc.LGTM.app.modules.registration.dto.MissionHistoryInfo;
import swm.hkcc.LGTM.app.modules.tag.domain.TechTag;

import java.util.List;

@SuperBuilder
@Data
@NoArgsConstructor
public class RegistrationJuniorResponse {
    protected String missionName;
    protected List<TechTag> techTagList;
    protected ProcessStatus processStatus;
    protected List<MissionHistoryInfo> missionHistory;
    protected String buttonTitle;
}
