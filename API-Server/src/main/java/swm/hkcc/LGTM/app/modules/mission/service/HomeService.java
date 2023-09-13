package swm.hkcc.LGTM.app.modules.mission.service;

import swm.hkcc.LGTM.app.modules.serverDrivenUI.ServerDrivenScreenResponse;

public interface HomeService {
    ServerDrivenScreenResponse getHomeScreen(Long memberId, int version);
}
