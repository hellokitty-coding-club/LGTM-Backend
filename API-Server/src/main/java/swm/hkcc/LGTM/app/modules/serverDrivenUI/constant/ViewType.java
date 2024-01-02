package swm.hkcc.LGTM.app.modules.serverDrivenUI.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ViewType {
    TITLE("sectionTitle"),
    ITEM("sectionItem"),
    SUB_ITEM("sectionSubItem"),
    CLOSER("sectionCloser"),
    EMPTY("empty");

    private final String name;
}
