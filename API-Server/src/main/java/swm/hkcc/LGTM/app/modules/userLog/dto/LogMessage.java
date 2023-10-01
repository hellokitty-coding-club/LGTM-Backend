package swm.hkcc.LGTM.app.modules.userLog.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Data
@Getter
@ToString
@Builder
public class LogMessage {
    private String eventLogName;
    private String screenName;
    private Integer logVersion;

    private Map<String, Object> logData;
}
