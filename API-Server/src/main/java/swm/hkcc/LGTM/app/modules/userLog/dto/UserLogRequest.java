package swm.hkcc.LGTM.app.modules.userLog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLogRequest {
    @NotBlank
    private String eventLogType;
    @NotBlank
    private String screenName;
    @NotBlank
    private String target;
    private Long stayIntervalMs;
    @Positive
    private Long missionId;
}
