package swm.hkcc.LGTM.app.modules.mission.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateMissionResponse {
    private Long writerId;
    private Long missionId;
}
