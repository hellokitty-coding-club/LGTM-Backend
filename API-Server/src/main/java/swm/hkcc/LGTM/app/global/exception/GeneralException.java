package swm.hkcc.LGTM.app.global.exception;

import lombok.Getter;
import swm.hkcc.LGTM.app.global.constant.ResponseCode;

@Getter
public class GeneralException extends RuntimeException{

    private final ResponseCode responseCode;

    public GeneralException() {
        super(ResponseCode.INTERNAL_ERROR.getMessage());
        this.responseCode = ResponseCode.INTERNAL_ERROR;
    }

    public GeneralException(String message) {
        super(ResponseCode.INTERNAL_ERROR.getMessage(message));
        this.responseCode = ResponseCode.INTERNAL_ERROR;
    }

    public GeneralException(String message, Throwable cause) {
        super(ResponseCode.INTERNAL_ERROR.getMessage(message), cause);
        this.responseCode = ResponseCode.INTERNAL_ERROR;
    }

    public GeneralException(Throwable cause) {
        super(ResponseCode.INTERNAL_ERROR.getMessage(cause));
        this.responseCode = ResponseCode.INTERNAL_ERROR;
    }

    public GeneralException(ResponseCode responseCode) {
        super(responseCode.getMessage());
        this.responseCode = responseCode;
    }

    public GeneralException(ResponseCode responseCode, String message) {
        super(responseCode.getMessage(message));
        this.responseCode = responseCode;
    }

    public GeneralException(ResponseCode responseCode, String message, Throwable cause) {
        super(responseCode.getMessage(message), cause);
        this.responseCode = responseCode;
    }

    public GeneralException(ResponseCode responseCode, Throwable cause) {
        super(responseCode.getMessage(cause), cause);
        this.responseCode = responseCode;
    }
}