package swm.hkcc.consumer.app.dto;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Data
@Getter @ToString
public class LogMessage {
    private String eventLogName;
    private String screenName;
    private Integer logVersion;

    private Map<String, Object> logData;

    public UserDataLog toUserDataLog(String topic, int partition, long receivedTimeStamp) {
        return UserDataLog.builder()
                .logId(eventLogName + "_" + receivedTimeStamp + "_" + topic + "_" + partition)
                .eventLogName(eventLogName)
                .screenName(screenName)
                .logVersion(logVersion)
                .logData(logData)
                .topic(topic)
                .partition(partition)
                .receivedTimeStamp(receivedTimeStamp)
                .build();
    }
}
