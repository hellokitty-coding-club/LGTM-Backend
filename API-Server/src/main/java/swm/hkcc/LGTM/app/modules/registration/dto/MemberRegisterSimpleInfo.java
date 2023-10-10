package swm.hkcc.LGTM.app.modules.registration.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;
import swm.hkcc.LGTM.app.modules.registration.domain.ProcessStatus;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Optional;

@Data
@NoArgsConstructor
public class MemberRegisterSimpleInfo implements Serializable {
    private Long memberId;
    private String nickname;
    private String githubId;
    private String profileImageUrl;
    private ProcessStatus processStatus;
    private String paymentDate = "";
    private String missionFinishedDate = "";
    private String githubPrUrl = "";

    @QueryProjection
    public MemberRegisterSimpleInfo(Long memberId, String nickname, String githubId, String profileImageUrl, ProcessStatus processStatus, String githubPrUrl) {
        this.memberId = memberId;
        this.nickname = nickname;
        this.githubId = githubId;
        this.profileImageUrl = profileImageUrl;
        this.processStatus = processStatus;
        this.githubPrUrl = Optional.ofNullable(githubPrUrl).orElse("");
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
