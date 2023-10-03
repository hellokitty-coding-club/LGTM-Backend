package swm.hkcc.consumer.app.dto;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.Map;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
//@Document("UserDataLog")
public class UserDataLog {
    @Id
    private String logId;

    private String eventLogName;

    private String screenName;

    private Integer logVersion;

    private Integer userID;

    private Map<String, Object> logData;

    private String topic;

    private int partition;

    private long receivedTimeStamp;
}
