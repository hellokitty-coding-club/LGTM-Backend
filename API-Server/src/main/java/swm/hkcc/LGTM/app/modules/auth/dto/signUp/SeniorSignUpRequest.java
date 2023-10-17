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
public class SeniorSignUpRequest extends CommonUserData{

    @NotNull
    private String companyInfo;

    @NotNull
    private Integer careerPeriod;

    @NotNull
    private String position;

    @NotNull
    private String accountNumber;

    @NotNull
    private String bankName;

    @NotNull
    private String accountHolderName;

}
