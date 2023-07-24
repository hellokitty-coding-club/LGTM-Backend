package swm.hkcc.LGTM.app.modules.mission.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateMissionResponse {
    @NotNull
    private Long writerId;
    @NotNull
    private Long missionId;
}
