package swm.hkcc.LGTM.app.modules.serverDrivenUI;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Theme {
    DARK("#030c1b", "#56ee9b", "#e7ecf2", "dark"),
    LIGHT("#FFFFFF", "#000000", "#e7ecf2", "light");

    private final String backgroundColor;
    private final String titleColor;
    private final String borderColor;
    private final String boxStyle;


}
