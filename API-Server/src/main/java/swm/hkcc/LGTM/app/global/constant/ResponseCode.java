package swm.hkcc.LGTM.app.global.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import swm.hkcc.LGTM.app.global.exception.GeneralException;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

@Getter
@RequiredArgsConstructor
public enum ResponseCode {

    OK(0, HttpStatus.OK, "Ok"),

    BAD_REQUEST(10000, HttpStatus.BAD_REQUEST, "Bad request"),
    NOT_FOUND(10001, HttpStatus.NOT_FOUND, "Requested resource is not found"),
    VALIDATION_ERROR(10002, HttpStatus.BAD_REQUEST, "Validation error"),
    INVALID_GITHUB_ACCESS_TOKEN(10003, HttpStatus.BAD_REQUEST, "Invalid Github Access Token"),

    INTERNAL_ERROR(20000, HttpStatus.INTERNAL_SERVER_ERROR, "Internal error"),
    DATA_ACCESS_ERROR(20001, HttpStatus.INTERNAL_SERVER_ERROR, "Data access error")
    ;

    private final Integer code;
    private final HttpStatus httpStatus;
    private final String message;


    public static ResponseCode valueOf(HttpStatus httpStatus) {
        if (httpStatus == null) {
            throw new GeneralException("HttpStatus is null.");
        }

        return Arrays.stream(values())
                .filter(responseCode -> responseCode.getHttpStatus() == httpStatus)
                .findFirst()
                .orElseGet(() -> {
                    if (httpStatus.is4xxClientError()) {
                        return ResponseCode.BAD_REQUEST;
                    } else if (httpStatus.is5xxServerError()) {
                        return ResponseCode.INTERNAL_ERROR;
                    } else {
                        return ResponseCode.OK;
                    }
                });
    }

    public String getMessage(Throwable e) {
        return this.getMessage(this.getMessage() + " - " + e.getMessage());
    }

    public String getMessage(String message) {
        return Optional.ofNullable(message)
                .filter(Predicate.not(String::isBlank))
                .orElse(this.getMessage());
    }

    @Override
    public String toString() {
        return String.format("%s (%d)", this.name(), this.getCode());
    }

}
