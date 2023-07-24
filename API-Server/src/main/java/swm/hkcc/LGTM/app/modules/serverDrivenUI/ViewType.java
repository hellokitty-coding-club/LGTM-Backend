package swm.hkcc.LGTM.app.modules.serverDrivenUI;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ViewType {
    TITLE("sectionTitle"),
    ITEM("sectionItem"),
    CLOSER("sectionCloser");

    private final String name;
}
