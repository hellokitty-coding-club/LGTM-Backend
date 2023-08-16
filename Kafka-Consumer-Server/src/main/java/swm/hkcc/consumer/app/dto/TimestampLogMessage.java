package swm.hkcc.consumer.app.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Builder
@ToString
@Getter
public class TimestampLogMessage {
    private String eventLogType;
    private String screenName;
    private String target;
    private Long missionId;
    private Long memberId;
    private LocalDateTime timestamp;

    private TimestampLogMessage() {
    }

    private TimestampLogMessage(String eventLogType, String screenName, String target, Long missionId, Long memberId, LocalDateTime timestamp) {
        this.eventLogType = eventLogType;
        this.screenName = screenName;
        this.target = target;
        this.missionId = missionId;
        this.memberId = memberId;
        this.timestamp = timestamp;
    }

    public static TimestampLogMessage from(CommonMessage message) {
        return TimestampLogMessage.builder()
                .eventLogType(message.getEventLogType())
                .screenName(message.getScreenName())
                .target(message.getTarget())
                .missionId(message.getMissionId())
                .memberId(message.getMemberId())
                .timestamp(message.getTimestamp())
                .build();
    }

}
