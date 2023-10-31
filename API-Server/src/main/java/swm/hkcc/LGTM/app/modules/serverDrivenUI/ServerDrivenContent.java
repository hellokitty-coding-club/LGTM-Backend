package swm.hkcc.LGTM.app.modules.serverDrivenUI;

import lombok.Builder;
import lombok.Getter;
import swm.hkcc.LGTM.app.modules.serverDrivenUI.constant.Theme;
import swm.hkcc.LGTM.app.modules.serverDrivenUI.constant.ViewType;

@Builder
@Getter
public class ServerDrivenContent<T> {

    private String viewTypeName;

    private Theme theme;

    private T content;

    public static ServerDrivenContent from(Theme theme, ViewType viewType) {
        return ServerDrivenContent.builder()
                .viewTypeName(viewType.getName())
                .theme(theme)
                .content("")
                .build();
    }

    public static ServerDrivenContent from(Object content, Theme theme, ViewType viewType) {
        return ServerDrivenContent.builder()
                .viewTypeName(viewType.getName())
                .theme(theme)
                .content(content)
                .build();
    }

}
