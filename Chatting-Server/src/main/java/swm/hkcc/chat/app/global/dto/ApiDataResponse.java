package swm.hkcc.chat.app.global.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import swm.hkcc.chat.app.global.constant.ResponseCode;

@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
public class ApiDataResponse<T> extends ApiResponse {

    private final T data;

    private ApiDataResponse(T data) {
        super(true, ResponseCode.OK.getCode(), ResponseCode.OK.getMessage());
        this.data = data;
    }

    public static <T> ApiDataResponse<T> of(T data) {
        return new ApiDataResponse<>(data);
    }

    public static <T> ApiDataResponse<T> empty() {
        return new ApiDataResponse<>(null);
    }
}