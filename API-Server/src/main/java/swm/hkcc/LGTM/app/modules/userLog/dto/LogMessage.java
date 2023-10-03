package swm.hkcc.LGTM.app.modules.userLog.dto;

import lombok.*;

import java.util.Map;

@Data
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogMessage {
    private String eventLogName;
    private String screenName;
    private Integer logVersion;
    private Integer userID;

    private Map<String, Object> logData;
}
