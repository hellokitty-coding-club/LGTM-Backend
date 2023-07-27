package swm.hkcc.LGTM.app.modules.mission.domain.serverDrivenUI;

import org.springframework.stereotype.Component;
import swm.hkcc.LGTM.app.modules.mission.domain.MissionContentSequence;
import swm.hkcc.LGTM.app.modules.serverDrivenUI.domain.ServerDrivenUISequenceFactory;

@Component
public class MissionProcessServerDrivenUISequenceFactory implements ServerDrivenUISequenceFactory {
    @Override
    public MissionContentSequence getServerDrivenUISequenceByVersion(int version) {
        // todo: implement this method
        return null;
    }
}
