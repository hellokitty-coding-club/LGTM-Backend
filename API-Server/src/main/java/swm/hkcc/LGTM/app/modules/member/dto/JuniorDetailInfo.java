package swm.hkcc.LGTM.app.modules.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class JuniorDetailInfo implements MemberDetailInfo {
    private String educationalHistory;
}
