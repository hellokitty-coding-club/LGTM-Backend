package swm.hkcc.LGTM.app.modules.mission.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SubItemDto {
    private String text;
    private String appUrl;
}
