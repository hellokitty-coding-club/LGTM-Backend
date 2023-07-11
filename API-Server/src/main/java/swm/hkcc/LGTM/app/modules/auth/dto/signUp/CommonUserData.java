package swm.hkcc.LGTM.app.modules.auth.dto.signUp;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
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
    protected Integer githubUuid;

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
