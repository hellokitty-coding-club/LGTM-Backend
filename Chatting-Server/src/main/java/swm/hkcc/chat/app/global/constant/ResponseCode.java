package swm.hkcc.chat.app.global.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import swm.hkcc.chat.app.global.exception.GeneralException;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

@Getter
@RequiredArgsConstructor
public enum ResponseCode {
    OK(0, HttpStatus.OK, "Ok"),

    // 4xxx : Common Client Error
    BAD_REQUEST(4000, HttpStatus.BAD_REQUEST, "Bad request"),
    NOT_FOUND(4001, HttpStatus.NOT_FOUND, "Requested resource is not found"),
    VALIDATION_ERROR(4002, HttpStatus.BAD_REQUEST, "Validation error"),

    // 41xx : JWT Error
    JWT_SIGNITURE_ERROR(4100, HttpStatus.UNAUTHORIZED, "손상된 JWT 토큰입니다."),
    JWT_MALFORMED_ERROR(4101, HttpStatus.UNAUTHORIZED, "JWT 토큰이 올바르지 않습니다."),
    JWT_EXPIRED_ERROR(4102, HttpStatus.UNAUTHORIZED, "JWT 토큰이 만료되었습니다."),
    INVALID_AUTHENTICATION(4103, HttpStatus.UNAUTHORIZED, "유효하지 않은 인증 정보입니다. 다시 시도해주세요."),
    UNSPECIFIED_MEMBER_TYPE(4104, HttpStatus.INTERNAL_SERVER_ERROR, "회원이 시니어, 주니어 둘 다 속하지 않습니다. 회원가입 과정에서 문제가 발생했을 가능성이 있습니다."),
    INVALID_ROLE(4105, HttpStatus.FORBIDDEN, "유효하지 않은 인가 정보입니다. 사용자 권한을 확인해 주세요."),

    // 5xxx : Common Server Error
    INTERNAL_ERROR(5000, HttpStatus.INTERNAL_SERVER_ERROR, "Internal error"),
    DATA_ACCESS_ERROR(5001, HttpStatus.INTERNAL_SERVER_ERROR, "Data access error"),

    // 100xx : Auth/SignIn
    INVALID_GITHUB_ACCESS_TOKEN(10003, HttpStatus.BAD_REQUEST, "유효하지 않은 Github Access Token입니다. 다시 시도해주세요."),
    // 101xx : Member Authority
    NOT_EXIST_MEMBER(10100, HttpStatus.BAD_REQUEST, "존재하지 않는 회원입니다."),
    NOT_SENIOR_MEMBER(10101, HttpStatus.BAD_REQUEST, "시니어 회원만 접근할 수 있습니다."),
    NOT_JUNIOR_MEMBER(10102, HttpStatus.BAD_REQUEST, "주니어 회원만 접근할 수 있습니다."),

    // 104xx : 검색

    // 105xx :

    // 106xx :

    // 107xx : Review(feedback)
    NOT_EXIST_REVIEW_INTERNAL(10700, HttpStatus.INTERNAL_SERVER_ERROR, "DB 조회 과정에서 문제가 발생했습니다: 존재하지 않는 리뷰입니다.")
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
        return this.getMessage(e.getMessage());
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