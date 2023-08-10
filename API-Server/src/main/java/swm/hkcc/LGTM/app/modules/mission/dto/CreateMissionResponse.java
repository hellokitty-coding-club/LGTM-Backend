package swm.hkcc.LGTM.app.modules.mission.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class CreateMissionResponse {
    @NotNull
    private Long writerId;
    @NotNull
    private Long missionId;
}
