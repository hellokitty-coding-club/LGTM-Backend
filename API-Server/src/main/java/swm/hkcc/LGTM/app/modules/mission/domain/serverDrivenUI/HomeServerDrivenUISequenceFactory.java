package swm.hkcc.LGTM.app.modules.mission.domain.serverDrivenUI;

import org.springframework.stereotype.Component;
import swm.hkcc.LGTM.app.modules.mission.constant.HomeServerDrivenUISequenceByVersion;
import swm.hkcc.LGTM.app.modules.serverDrivenUI.domain.ServerDrivenUISequenceFactory;

@Component
public class HomeServerDrivenUISequenceFactory implements ServerDrivenUISequenceFactory {
    @Override
    public HomeServerDrivenUISequenceByVersion getServerDrivenUISequenceByVersion(int version) {
        return null;
    }
}
