package swm.hkcc.LGTM.app.modules.auth.dto.signUp;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class SeniorSignUpRequest extends SignUpRequest {
    @NotNull
    private String companyInfo;

    @NotNull
    private Integer careerPeriod;

    private String position;

    protected SeniorSignUpRequest() {
        super();
    }

    @Builder
    public SeniorSignUpRequest(String githubId, String nickName, String deviceToken, String profileImageUrl, String introduction, List<String> tagList, String companyInfo, Integer careerPeriod, String position) {
        super(githubId, nickName, deviceToken, profileImageUrl, introduction, tagList);
        this.companyInfo = companyInfo;
        this.careerPeriod = careerPeriod;
        this.position = position;
    }
}
