package swm.hkcc.LGTM.app.modules.registration.dto;

import com.querydsl.core.Tuple;
import lombok.Builder;
import lombok.Data;
import swm.hkcc.LGTM.app.modules.registration.domain.MissionHistory;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import swm.hkcc.LGTM.app.modules.registration.domain.ProcessStatus;

import java.time.LocalDateTime;

import static swm.hkcc.LGTM.app.modules.registration.domain.QMissionHistory.missionHistory;

@Data
@Builder
@AllArgsConstructor
public class MissionHistoryInfo {
    private ProcessStatus status;
    private String dateTime;

    @QueryProjection
    public MissionHistoryInfo(ProcessStatus status, LocalDateTime dateTime) {
        this.status = status;
        this.dateTime = getDateTime(dateTime);
    }

    public static MissionHistoryInfo createMissionHistoryInfo(Tuple tuple) {
        return MissionHistoryInfo.builder()
                .status(tuple.get(missionHistory.status))
                .dateTime(getDateTime(tuple.get(missionHistory.createdAt)))
                .build();
    }

    public static MissionHistoryInfo from(MissionHistory missionHistory) {
        return MissionHistoryInfo.builder()
                .status(missionHistory.getStatus())
                .dateTime(getDateTime(missionHistory.getCreatedAt()))
                .build();
    }

    private static String getDateTime(LocalDateTime dateTime) {
        if (dateTime != null)
            return dateTime.toString();
        return "";
    }
}
