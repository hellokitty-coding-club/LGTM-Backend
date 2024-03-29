package swm.hkcc.LGTM.app.modules.userLog.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Builder
@ToString
@Getter
public class TimeIntervalLogMessage {
    private String eventLogType;
    private String screenName;
    private String target;
    private Long stayIntervalMs;
    private Long missionId;
    private Long memberId;
    private LocalDateTime timestamp;

    private TimeIntervalLogMessage() {
    }

    private TimeIntervalLogMessage(String eventLogType, String screenName, String target, Long stayIntervalMs, Long missionId, Long memberId, LocalDateTime timestamp) {
        this.eventLogType = eventLogType;
        this.screenName = screenName;
        this.target = target;
        this.stayIntervalMs = stayIntervalMs;
        this.missionId = missionId;
        this.memberId = memberId;
        this.timestamp = timestamp;
    }

    public static TimeIntervalLogMessage from(UserLogRequest request, Long memberId, LocalDateTime timestamp) {
        return TimeIntervalLogMessage.builder()
                .eventLogType(request.getEventLogType())
                .screenName(request.getScreenName())
                .target(request.getTarget())
                .stayIntervalMs(request.getStayIntervalMs())
                .missionId(request.getMissionId())
                .memberId(memberId)
                .timestamp(timestamp)
                .build();
    }
}
