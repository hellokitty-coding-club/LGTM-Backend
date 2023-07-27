package swm.hkcc.LGTM.app.modules.mission.domain;

import lombok.Getter;
import org.springframework.stereotype.Component;
import swm.hkcc.LGTM.app.modules.mission.constant.MissionContentType;

import java.util.List;

@Getter
@Component
public class MissionContentSequence {

    private final List<MissionContentType> missionContents;

    public MissionContentSequence(List<MissionContentType> missionContents) {
        validateMissionContents(missionContents);
        this.missionContents = missionContents;
    }

    private void validateMissionContents(List<MissionContentType> missionContents) {
        // todo: implement this method
    }

}
