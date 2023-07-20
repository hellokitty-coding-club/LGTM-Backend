package swm.hkcc.LGTM.app.modules.serverDrivenUI.domain;

import swm.hkcc.LGTM.app.modules.serverDrivenUI.ServerDrivenScreenResponse;

public interface ServerDrivenUISequenceFactory {

    ServerDrivenScreenResponse createServerDrivenUISequenceByVersion(int version);
}
