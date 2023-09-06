package swm.hkcc.LGTM.app.modules.member.dto;

import lombok.*;
import swm.hkcc.LGTM.app.modules.mission.dto.MissionDto;
import swm.hkcc.LGTM.app.modules.tag.domain.TechTag;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class MemberDetailProfile {
    private Long memberId;
    private String githubId;
    private String nickName;
    private String profileImageUrl;
    private String introduction;
    private List<TechTag> techTagList;
    private boolean isAgreeWithEventInfo;
    private MemberDetailInfo memberDetailInfo;
    private List<MissionDto> memberMissionHistory;
}
