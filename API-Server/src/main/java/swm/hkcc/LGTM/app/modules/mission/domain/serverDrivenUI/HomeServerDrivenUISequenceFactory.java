package swm.hkcc.LGTM.app.modules.mission.domain.serverDrivenUI;

import org.springframework.stereotype.Component;
import swm.hkcc.LGTM.app.modules.mission.constant.HomeServerDrivenUISequenceByVersion;
import swm.hkcc.LGTM.app.modules.mission.constant.MissionContentType;
import swm.hkcc.LGTM.app.modules.serverDrivenUI.domain.ServerDrivenUISequenceFactory;

import java.util.List;
import java.util.Optional;

@Component
public class HomeServerDrivenUISequenceFactory implements ServerDrivenUISequenceFactory {
    @Override
    public Optional<List<MissionContentType>> getServerDrivenUISequenceByVersion(int version) {
        return HomeServerDrivenUISequenceByVersion.findByVersion(version)
                .map(HomeServerDrivenUISequenceByVersion::getComponents);
    }
}
