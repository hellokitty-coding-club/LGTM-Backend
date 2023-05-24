package swm.hkcc.LGTM.app.global.dto;


import lombok.*;
import swm.hkcc.LGTM.app.global.constant.ResponseCode;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiResponse {

    private final Boolean success;
    private final Integer responseCode;
    private final String message;

    public static ApiResponse of(Boolean success, Integer responseCode, String message) {
        return new ApiResponse(success, responseCode, message);
    }

    public static ApiResponse of(Boolean success, ResponseCode responseCode) {
        return new ApiResponse(success, responseCode.getCode(), responseCode.getMessage());
    }

    public static ApiResponse of(Boolean success, ResponseCode responseCode, Exception e) {
        return new ApiResponse(success, responseCode.getCode(), responseCode.getMessage(e));
    }

    public static ApiResponse of(Boolean success, ResponseCode responseCode, String message) {
        return new ApiResponse(success, responseCode.getCode(), responseCode.getMessage(message));
    }
}