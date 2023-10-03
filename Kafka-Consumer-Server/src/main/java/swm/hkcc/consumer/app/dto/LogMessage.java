package swm.hkcc.consumer.app.dto;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import swm.hkcc.consumer.app.domain.UserLog;

import java.util.Map;

@Data
@Getter @ToString
public class LogMessage {
    private String eventLogName;
    private String screenName;
    private Integer logVersion;
    private String sessionID;
    private Integer userID;
    private String deviceOS;
    private String deviceModel;
    private String appVersion;
    private String region;

    private Map<String, Object> logData;

    public UserLog toUserLog(String topic, int partition, long receivedTimeStamp) {
        return UserLog.builder()
                .eventLogName(eventLogName)
                .screenName(screenName)
                .logVersion(logVersion)
                .logData(logData)
                .sessionID(sessionID)
                .userID(userID)
                .deviceOS(deviceOS)
                .deviceModel(deviceModel)
                .appVersion(appVersion)
                .region(region)
                .topic(topic)
                .partitionNumber(partition)
                .receivedTimeStamp(receivedTimeStamp)
                .build();
    }
}
