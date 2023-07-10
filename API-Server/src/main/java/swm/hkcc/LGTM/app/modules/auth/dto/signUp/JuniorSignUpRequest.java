package swm.hkcc.LGTM.app.modules.auth.dto.signUp;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class JuniorSignUpRequest {

    @NotNull
    private CommonUserData commonUserData;

    @NotNull
    private String educationalHistory;

    @NotNull
    private String realName;

}
