package swm.hkcc.LGTM.app.modules.auth.dto.signUp;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class JuniorSignUpRequest extends SignUpRequest {
    @NotNull
    private String educationalHistory;

    protected JuniorSignUpRequest() {
        super();
    }

    @Builder
    public JuniorSignUpRequest(String githubId, String name, String nickName, String deviceToken, String profileImageUrl, String introduction, List<String> tagList, String educationalHistory) {
        super(githubId, name, nickName, deviceToken, profileImageUrl, introduction, tagList);
        this.educationalHistory = educationalHistory;
    }
}
