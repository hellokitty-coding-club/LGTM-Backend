package swm.hkcc.LGTM.app.global.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

@Data
public class PushTestRequest {
    @NotNull
    Map<String, String> data;
}
