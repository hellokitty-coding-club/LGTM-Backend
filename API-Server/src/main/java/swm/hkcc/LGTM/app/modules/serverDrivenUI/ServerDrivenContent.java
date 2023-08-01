package swm.hkcc.LGTM.app.modules.serverDrivenUI;

import lombok.Builder;
import lombok.Getter;
import swm.hkcc.LGTM.app.modules.mission.dto.MissionDetailsDto;
import swm.hkcc.LGTM.app.modules.mission.dto.MissionDto;
import swm.hkcc.LGTM.app.modules.mission.dto.MissionTitleDto;

@Builder
@Getter
public class ServerDrivenContent<T> {

    private ViewType viewType;

    private Theme theme;

    private T content;

    public static ServerDrivenContent from(Theme theme, ViewType viewType) {
        return ServerDrivenContent.builder()
                .viewType(viewType)
                .theme(theme)
                .content("")
                .build();
    }

    public static ServerDrivenContent from(Object content, Theme theme, ViewType viewType) {
        return ServerDrivenContent.builder()
                .viewType(viewType)
                .theme(theme)
                .content(content)
                .build();
    }

}
