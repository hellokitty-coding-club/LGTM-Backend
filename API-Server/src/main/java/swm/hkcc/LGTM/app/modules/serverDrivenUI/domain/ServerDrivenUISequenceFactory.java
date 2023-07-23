package swm.hkcc.LGTM.app.modules.serverDrivenUI.domain;

import swm.hkcc.LGTM.app.modules.serverDrivenUI.ServerDrivenUISequenceByVersion;

public interface ServerDrivenUISequenceFactory {

    ServerDrivenUISequenceByVersion getServerDrivenUISequenceByVersion(int version);
}
