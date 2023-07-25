package swm.hkcc.LGTM.app.modules.mission.domain.serverDrivenUI;

import org.springframework.stereotype.Component;
import swm.hkcc.LGTM.app.global.constant.ResponseCode;
import swm.hkcc.LGTM.app.global.exception.GeneralException;
import swm.hkcc.LGTM.app.modules.mission.constant.HomeServerDrivenUISequenceByVersion;
import swm.hkcc.LGTM.app.modules.mission.constant.MissionContentType;
import swm.hkcc.LGTM.app.modules.mission.domain.MissionContentSequence;
import swm.hkcc.LGTM.app.modules.serverDrivenUI.domain.ServerDrivenUISequenceFactory;

import java.util.List;

@Component
public class HomeServerDrivenUISequenceFactory implements ServerDrivenUISequenceFactory {
    @Override
    public List<MissionContentType> getServerDrivenUISequenceByVersion(int version) {
        return HomeServerDrivenUISequenceByVersion.findByVersion(version)
                .map(HomeServerDrivenUISequenceByVersion::getContents)
                .orElseThrow(() -> new GeneralException(ResponseCode.DATA_ACCESS_ERROR));
    }

    public MissionContentSequence createMissionContentSequenceFromVersion(int version) {
        return new MissionContentSequence(getServerDrivenUISequenceByVersion(version));
    }
}
