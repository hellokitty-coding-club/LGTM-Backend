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
    private String logVersion;

    private String sessionID;
    private String userID;
    private String osNameAndVersion;
    private String deviceModel;
    private String appVersion;
    private String region;

    private Map<String, Object> logData;
}
