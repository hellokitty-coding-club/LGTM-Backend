package swm.hkcc.LGTM.app.modules.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import swm.hkcc.LGTM.app.modules.auth.constants.MemberType;
import swm.hkcc.LGTM.app.modules.mission.dto.MissionDtoV2;
import swm.hkcc.LGTM.app.modules.tag.domain.TechTag;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class MemberDetailProfile {
    private Long memberId;
    private MemberType memberType;
    private boolean isMyProfile;
    private String githubId;
    private String nickName;
    private String profileImageUrl;
    private String introduction;
    private List<TechTag> techTagList;
    private boolean isAgreeWithEventInfo;
    private MemberDetailInfo memberDetailInfo;
    private List<MissionDtoV2> memberMissionHistory;
}
