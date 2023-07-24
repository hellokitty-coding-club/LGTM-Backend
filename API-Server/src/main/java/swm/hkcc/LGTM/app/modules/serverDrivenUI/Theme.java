package swm.hkcc.LGTM.app.modules.serverDrivenUI;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Theme {
    DARK("#000010", "#000020", "#000030", "dark"),
    LIGHT("#FFFFF0", "#FFFFF1", "#FFFFF2", "light");

    private final String backgroundColor;
    private final String titleColor;
    private final String borderColor;
    private final String boxStyle;


}
