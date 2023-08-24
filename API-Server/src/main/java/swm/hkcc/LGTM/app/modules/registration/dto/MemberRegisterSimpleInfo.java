package swm.hkcc.LGTM.app.modules.registration.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.querydsl.core.Tuple;
import lombok.Data;
import swm.hkcc.LGTM.app.modules.registration.domain.ProcessStatus;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Optional;

import static swm.hkcc.LGTM.app.modules.member.domain.QMember.member;
import static swm.hkcc.LGTM.app.modules.registration.domain.QMissionRegistration.missionRegistration;

@Data
public class MemberRegisterSimpleInfo implements Serializable {
    private Long memberId;
    private String nickname;
    private String githubId;
    private String profileImageUrl;
    private ProcessStatus processStatus;
    private String paymentDate = "";
    private String missionFinishedDate = "";
    private String githubPrUrl = "";

    public static MemberRegisterSimpleInfo createMemberRegisterInfo(Tuple tuple) {
        MemberRegisterSimpleInfo memberRegisterSimpleInfo = new MemberRegisterSimpleInfo();
        memberRegisterSimpleInfo.memberId = tuple.get(member.memberId);
        memberRegisterSimpleInfo.nickname = tuple.get(member.nickName);
        memberRegisterSimpleInfo.githubId = tuple.get(member.githubId);
        memberRegisterSimpleInfo.profileImageUrl = tuple.get(member.profileImageUrl);
        memberRegisterSimpleInfo.processStatus = tuple.get(missionRegistration.status);
        memberRegisterSimpleInfo.githubPrUrl = Optional.ofNullable(tuple.get(missionRegistration.githubPullRequestUrl)).orElse("");
        return memberRegisterSimpleInfo;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        if (paymentDate != null)
            this.paymentDate = paymentDate.toString();
    }

    public void setMissionFinishedDate(LocalDateTime missionFinishedDate) {
        if (missionFinishedDate != null)
            this.missionFinishedDate = missionFinishedDate.toString();
    }
}
