package swm.hkcc.LGTM.app.modules.serverDrivenUI.domain;

import swm.hkcc.LGTM.app.modules.mission.domain.MissionContentSequence;

public interface ServerDrivenUISequenceFactory {

    MissionContentSequence getServerDrivenUISequence(String ABTestGroupName);
    MissionContentSequence getServerDrivenUISequence(String ABTestGroupName, int apiVersion);
}
