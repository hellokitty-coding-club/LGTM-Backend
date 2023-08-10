package swm.hkcc.LGTM.app.modules.mission.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.ToString;

import java.util.Arrays;

@Getter
@ToString
public enum MissionStatus {
    RECRUITING("참가자 모집중"),
    MISSION_PROCEEDING("미션 진행중"),
    MISSION_FINISHED("미션 종료");

    private final String status;

    MissionStatus(String status) {
        this.status = status;
    }



    // todo : https://developia.tistory.com/30
    @JsonCreator
    public static MissionStatus from(String status) {
        return Arrays.stream(MissionStatus.values())
                .filter(missionStatus -> missionStatus.getStatus().equals(status))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    @JsonValue
    public String getValue() {
        return this.status;
    }

}
