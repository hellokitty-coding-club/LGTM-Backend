package swm.hkcc.LGTM.app.modules.mission.domain.holder;

import org.springframework.stereotype.Component;
import swm.hkcc.LGTM.app.modules.mission.constant.MissionContentType;
import swm.hkcc.LGTM.app.modules.serverDrivenUI.ServerDrivenContent;
import swm.hkcc.LGTM.app.modules.serverDrivenUI.Theme;
import swm.hkcc.LGTM.app.modules.serverDrivenUI.ViewType;

import java.util.Map;
import java.util.Optional;

@Component
public class MissionCloserHolder {

    private static final Map<MissionContentType, Theme> THEME_MAP = Map.of(
            MissionContentType.SECTION_DARK_CLOSER, Theme.DARK,
            MissionContentType.SECTION_LIGHT_CLOSER, Theme.LIGHT
    );

    public ServerDrivenContent getMissionCloser(MissionContentType missionContentType) {
        Theme theme = Optional.ofNullable(THEME_MAP.get(missionContentType))
                .orElseThrow(() -> new IllegalArgumentException("Invalid MissionContentType: " + missionContentType.getClass().getSimpleName()));
        return ServerDrivenContent.from(theme, ViewType.CLOSER);
    }
}