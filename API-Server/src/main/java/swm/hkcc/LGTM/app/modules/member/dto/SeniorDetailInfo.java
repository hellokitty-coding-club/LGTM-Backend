package swm.hkcc.LGTM.app.modules.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SeniorDetailInfo implements MemberDetailInfo {
    private String companyInfo;
    private int careerPeriod;
    private String position;
}
