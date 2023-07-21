package swm.hkcc.LGTM.app.modules.mission.service;

import org.springframework.stereotype.Service;
import swm.hkcc.LGTM.app.modules.serverDrivenUI.ServerDrivenScreenResponse;

@Service
public class HomeServiceImpl implements HomeService{
    @Override
    public ServerDrivenScreenResponse getHomeScreen(String githubId, int version) {
        return null;
    }
}
