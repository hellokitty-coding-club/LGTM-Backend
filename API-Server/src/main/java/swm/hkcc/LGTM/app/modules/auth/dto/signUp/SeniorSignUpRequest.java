package swm.hkcc.LGTM.app.modules.auth.dto.signUp;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import swm.hkcc.LGTM.app.modules.member.constant.Bank;

import java.util.List;

@Data
public class SeniorSignUpRequest extends SignUpRequest {
    @NotNull
    private String companyInfo;

    @NotNull
    private Integer careerPeriod;

    @NotNull
    private String position;

    @NotNull
    private String accountNumber;

    @NotNull
    private Bank bank;

    protected SeniorSignUpRequest() {
        super();
    }

    @Builder
    public SeniorSignUpRequest(String githubId, String nickName, String deviceToken, String profileImageUrl, String introduction, List<String> tagList, String companyInfo, Integer careerPeriod, String position, String accountNumber, Bank bank) {
        super(githubId, nickName, deviceToken, profileImageUrl, introduction, tagList);
        this.companyInfo = companyInfo;
        this.careerPeriod = careerPeriod;
        this.position = position;
        this.accountNumber = accountNumber;
        this.bank = bank;
    }
}
