package swm.hkcc.LGTM.app.modules.mission.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import swm.hkcc.LGTM.app.modules.mission.dto.MissionDetailsDto;
import swm.hkcc.LGTM.app.modules.mission.dto.MissionDto;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class MissionContentData <T>{

    private final List<T> missionData;

    public static <T> MissionContentData of(List<T> missionData) {
        validateMissions(missionData);
        return new MissionContentData(missionData);
    }

    private static void validateMissions(List<?> missionData) {
        missionData.forEach(mission -> {
            if (!(mission instanceof MissionDto) && !(mission instanceof MissionDetailsDto)) {
                throw new IllegalArgumentException("MissionContentData must be composed of MissionDto or MissionDetailsDto");
            }
        });
    }
}
