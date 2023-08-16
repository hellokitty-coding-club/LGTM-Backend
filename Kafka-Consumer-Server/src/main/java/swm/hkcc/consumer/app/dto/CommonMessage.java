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
                return TimestampLogMessage.builder()
                        .eventLogType(this.eventLogType)
                        .screenName(this.screenName)
                        .target(this.target)
                        .missionId(this.missionId)
                        .memberId(this.memberId)
                        .timestamp(this.timestamp)
                        .build();
            }
            case "interval" -> {
                return TimeIntervalLogMessage.builder()
                        .eventLogType(this.eventLogType)
                        .screenName(this.screenName)
                        .target(this.target)
                        .stayIntervalMs(this.stayIntervalMs)
                        .missionId(this.missionId)
                        .memberId(this.memberId)
                        .timestamp(this.timestamp)
                        .build();
            }
            default -> {
                throw new IllegalArgumentException("Invalid key");
            }
        }
    }
}
