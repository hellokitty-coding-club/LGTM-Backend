package swm.hkcc.LGTM.app.modules.registration.dto;

import lombok.Builder;
import lombok.Data;
import swm.hkcc.LGTM.app.modules.tag.domain.TechTag;

import java.util.List;

@Data @Builder
public class RegistrationSeniorResponse {
    private String missionName;
    private List<TechTag> techTagList;
    private List<MemberRegisterSimpleInfo> memberInfoList;
}
