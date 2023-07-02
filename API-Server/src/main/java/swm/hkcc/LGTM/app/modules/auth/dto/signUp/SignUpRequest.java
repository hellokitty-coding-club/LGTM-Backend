package swm.hkcc.LGTM.app.modules.auth.dto.signUp;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = JuniorSignUpRequest.class, name = "junior"),
        @JsonSubTypes.Type(value = SeniorSignUpRequest.class, name = "senior")
})
@AllArgsConstructor
@NoArgsConstructor
public abstract class SignUpRequest {

    @NotNull
    protected String githubId;

    @NotNull
    protected String nickName;

    @NotNull
    protected String deviceToken;

    @NotNull
    protected String profileImageUrl;

    @NotNull
    @Size(max = 1000)
    protected String introduction;

    @NotNull
    protected List<String> tagList;
}
