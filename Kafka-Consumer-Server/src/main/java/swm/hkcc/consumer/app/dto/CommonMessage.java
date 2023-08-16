package swm.hkcc.consumer.app.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommonMessage {
    private String eventLogType;
    private String screenName;
    private String target;
    private Long stayIntervalMs;
    private Long missionId;
    private Long memberId;
    private LocalDateTime timestamp;

    public Object getMessageByKey(String key) {
        switch (key) {
            case "timestamp" -> {
                return TimestampLogMessage.from(this);
            }
            case "interval" -> {
                return TimeIntervalLogMessage.from(this);
            }
            default -> {
                throw new IllegalArgumentException("Invalid key");
            }
        }
    }
}
