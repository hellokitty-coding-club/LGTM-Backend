package swm.hkcc.LGTM.app.modules.mission.domain.serverDrivenUI;

import swm.hkcc.LGTM.app.modules.mission.constant.MissionContentType;
import swm.hkcc.LGTM.app.modules.serverDrivenUI.domain.ServerDrivenUISequenceFactory;

import java.util.List;
import java.util.Optional;

public class MissionProcessServerDrivenUISequenceFactory implements ServerDrivenUISequenceFactory {
    @Override
    public Optional<List<MissionContentType>> getServerDrivenUISequenceByVersion(int version) {
        return null;
    }
}
