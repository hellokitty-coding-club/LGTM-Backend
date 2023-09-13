package swm.hkcc.LGTM.app.modules.registration.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import swm.hkcc.LGTM.app.modules.registration.domain.ProcessStatus;

import java.time.LocalDateTime;

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

    private static String getDateTime(LocalDateTime dateTime) {
        if (dateTime != null)
            return dateTime.toString();
        return "";
    }
}
