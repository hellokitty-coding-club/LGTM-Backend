package swm.hkcc.LGTM.app.modules.serverDrivenUI.domain;

import swm.hkcc.LGTM.app.modules.mission.constant.MissionContentType;

import java.util.List;

public interface ServerDrivenUISequenceFactory {

    List<MissionContentType> getServerDrivenUISequenceByVersion(int version);
}
