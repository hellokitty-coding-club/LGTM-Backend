package swm.hkcc.LGTM.app.modules.serverDrivenUI;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class ServerDrivenContents {

    private final List<ServerDrivenContent> contents;

    public static ServerDrivenContents of(List<ServerDrivenContent> contents) {
        return new ServerDrivenContents(contents);
    }
}
