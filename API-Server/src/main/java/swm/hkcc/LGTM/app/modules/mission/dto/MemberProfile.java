package swm.hkcc.LGTM.app.modules.mission.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class MemberProfile {
    @NotBlank
    private Long memberId;

    @NotBlank
    private String nickName;

    @NotBlank
    private String profileImageUrl;

    @NotBlank
    private String githubId;

    @NotBlank
    private String company;
}
