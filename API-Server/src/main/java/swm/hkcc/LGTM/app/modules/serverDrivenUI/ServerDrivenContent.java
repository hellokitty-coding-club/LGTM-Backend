package swm.hkcc.LGTM.app.modules.serverDrivenUI;

import lombok.Builder;
import lombok.Getter;
import swm.hkcc.LGTM.app.modules.mission.dto.MissionDto;

@Builder
@Getter
public class ServerDrivenContent<T> {

    private ViewType viewType;

    private Theme theme;

    private T content;

    public static ServerDrivenContent from(MissionDto missionDto, Theme theme, ViewType viewType) {
        return ServerDrivenContent.builder()
                .viewType(viewType)
                .theme(theme)
                .content(missionDto)
                .build();
    }
}
