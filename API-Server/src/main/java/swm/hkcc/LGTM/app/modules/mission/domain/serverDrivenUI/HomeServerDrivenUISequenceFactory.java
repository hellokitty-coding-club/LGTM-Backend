package swm.hkcc.LGTM.app.modules.mission.domain.serverDrivenUI;

import org.springframework.stereotype.Component;
import swm.hkcc.LGTM.app.global.constant.ResponseCode;
import swm.hkcc.LGTM.app.global.exception.GeneralException;
import swm.hkcc.LGTM.app.modules.mission.constant.HomeServerDrivenUISequenceByVersion;
import swm.hkcc.LGTM.app.modules.mission.domain.MissionContentSequence;
import swm.hkcc.LGTM.app.modules.serverDrivenUI.domain.ServerDrivenUISequenceFactory;

@Component
public class HomeServerDrivenUISequenceFactory implements ServerDrivenUISequenceFactory {
    @Override
    public MissionContentSequence getServerDrivenUISequence(String ABTestGroupName) {
        return HomeServerDrivenUISequenceByVersion.find(ABTestGroupName)
                .map(HomeServerDrivenUISequenceByVersion::getContents)
                .map(MissionContentSequence::new)
                .orElseThrow(() -> new GeneralException(ResponseCode.DATA_ACCESS_ERROR));
    }

    @Override
    public MissionContentSequence getServerDrivenUISequence(String ABTestGroupName, int apiVersion) {
        return HomeServerDrivenUISequenceByVersion.find(ABTestGroupName, apiVersion)
                .map(HomeServerDrivenUISequenceByVersion::getContents)
                .map(MissionContentSequence::new)
                .orElseThrow(() -> new GeneralException(ResponseCode.DATA_ACCESS_ERROR));
    }

}
