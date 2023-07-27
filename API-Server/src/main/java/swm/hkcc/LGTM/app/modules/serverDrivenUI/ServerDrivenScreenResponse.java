package swm.hkcc.LGTM.app.modules.serverDrivenUI;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class ServerDrivenScreenResponse {

    private final String screenName;

    private final List<ServerDrivenContent> contents;
}
