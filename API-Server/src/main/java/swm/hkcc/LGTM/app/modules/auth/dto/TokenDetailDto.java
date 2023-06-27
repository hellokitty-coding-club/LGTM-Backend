package swm.hkcc.LGTM.app.modules.auth.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@ToString(callSuper=true, includeFieldNames=true)
@SuperBuilder
public class TokenDetailDto extends TokenDto {
    private String accessToken;
    private String refreshToken;
}
