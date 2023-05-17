package swm.hkcc.LGTM.app.global.dto;


import lombok.*;
import swm.hkcc.LGTM.app.global.constant.ErrorCode;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiResponse {

    private final Boolean success;
    private final Integer errorCode;
    private final String message;

    public static ApiResponse of(Boolean success, Integer errorCode, String message) {
        return new ApiResponse(success, errorCode, message);
    }

    public static ApiResponse of(Boolean success, ErrorCode errorCode) {
        return new ApiResponse(success, errorCode.getCode(), errorCode.getMessage());
    }

    public static ApiResponse of(Boolean success, ErrorCode errorCode, Exception e) {
        return new ApiResponse(success, errorCode.getCode(), errorCode.getMessage(e));
    }

    public static ApiResponse of(Boolean success, ErrorCode errorCode, String message) {
        return new ApiResponse(success, errorCode.getCode(), errorCode.getMessage(message));
    }
}