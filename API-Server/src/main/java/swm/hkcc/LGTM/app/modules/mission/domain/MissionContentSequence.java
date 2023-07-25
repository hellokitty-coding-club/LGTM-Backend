package swm.hkcc.LGTM.app.modules.mission.domain;

import lombok.Getter;
import swm.hkcc.LGTM.app.modules.mission.constant.MissionContentType;

import java.util.List;

@Getter
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
