package swm.hkcc.LGTM.app.modules.serverDrivenUI.domain;

import swm.hkcc.LGTM.app.modules.mission.constant.MissionContentType;

import java.util.List;
import java.util.Optional;

public interface ServerDrivenUISequenceFactory {

    Optional<List<MissionContentType>> getServerDrivenUISequenceByVersion(int version);
}
