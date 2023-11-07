package swm.hkcc.LGTM.app.modules.userLog.domain;

import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import swm.hkcc.LGTM.app.modules.userLog.dto.LogMessage;

import java.util.Map;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;

    private String eventLogName;

    private String screenName;

    private Integer logVersion;

    @Type(JsonType.class)
    @Column(columnDefinition = "json")
    private Map<String, Object> logData;

    private String sessionID;

    private Integer userID;

    private String osNameAndVersion;

    private String deviceModel;

    private String appVersion;

    private String region;

    private String topic;

    private Integer partitionNumber;

    private Long serverTimeStamp;

    private Long receivedTimeStamp;

    public static UserLog from(LogMessage message) {
        return UserLog.builder()
                .eventLogName(message.getEventLogName())
                .screenName(message.getScreenName())
                .logVersion(parseInteger(message.getLogVersion()))
                .logData(message.getLogData())
                .sessionID(message.getSessionID())
                .userID(parseInteger(message.getUserID()))
                .osNameAndVersion(message.getOsNameAndVersion())
                .deviceModel(message.getDeviceModel())
                .appVersion(message.getAppVersion())
                .region(message.getRegion())
                .serverTimeStamp(System.currentTimeMillis())
                .build();
    }

    private static int parseInteger(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
