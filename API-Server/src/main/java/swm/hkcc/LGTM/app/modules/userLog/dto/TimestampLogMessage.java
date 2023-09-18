package swm.hkcc.LGTM.app.modules.userLog.dto;


import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
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

    public static TimestampLogMessage from(UserLogRequest request, Long memberId, LocalDateTime timestamp) {
        return TimestampLogMessage.builder()
                .eventLogType(request.getEventLogType())
                .screenName(request.getScreenName())
                .target(request.getTarget())
                .missionId(request.getMissionId())
                .memberId(memberId)
                .timestamp(timestamp)
                .build();
    }
}
