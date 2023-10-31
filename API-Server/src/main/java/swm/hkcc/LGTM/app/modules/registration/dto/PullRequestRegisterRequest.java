package swm.hkcc.LGTM.app.modules.registration.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PullRequestRegisterRequest {
    @NotBlank
    @URL
    private String githubPrUrl;

    public String getGithubPrUrl() {
        // todo: valid github url

        return githubPrUrl;
    }
}
