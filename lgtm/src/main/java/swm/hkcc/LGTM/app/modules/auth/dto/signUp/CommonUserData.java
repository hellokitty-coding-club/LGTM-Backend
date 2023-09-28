package swm.hkcc.LGTM.app.modules.auth.dto.signUp;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CommonUserData {

    @NotNull
    protected String githubId;

    @NotNull
    protected Integer githubOauthId;

    @NotNull
    protected String nickName;

    protected String deviceToken;

    @NotNull
    protected String profileImageUrl;

    @NotNull
    @Size(max = 1000)
    protected String introduction;

    @NotNull
    protected boolean isAgreeWithEventInfo;

    @NotNull
    protected List<String> tagList;
}
