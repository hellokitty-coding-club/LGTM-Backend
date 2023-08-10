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

    // Auth/SignIn
    INVALID_GITHUB_ACCESS_TOKEN(10003, HttpStatus.BAD_REQUEST, "유효하지 않은 Github Access Token입니다. 다시 시도해주세요."),
    DUPLICATE_NICK_NAME(10004, HttpStatus.BAD_REQUEST, "중복된 닉네임입니다. 다른 닉네임을 사용해주세요."),
    INVALID_TECH_TAG(10005, HttpStatus.BAD_REQUEST, "유효하지 않은 기술 태그입니다. 다시 시도해주세요."),
    INVALID_CAREER_PERIOD(10006, HttpStatus.BAD_REQUEST, "유효하지 않은 경력 기간입니다. 경력 기간은 최소 12달 이상이어야 합니다."),
    INVALID_BANK_NAME(10007, HttpStatus.BAD_REQUEST, "유효하지 않은 은행 이름입니다. 다시 시도해주세요."),
    INVALID_AUTHENTICATION(10007, HttpStatus.BAD_REQUEST, "유효하지 않은 인증 정보입니다. 다시 시도해주세요."),
    UNSPECIFIED_MEMBER_TYPE(10008, HttpStatus.INTERNAL_SERVER_ERROR, "회원이 시니어, 주니어 둘 다 속하지 않습니다. 회원가입 과정에서 문제가 발생했을 가능성이 있습니다."),



    // Member Authority
    NOT_EXIST_MEMBER(10100, HttpStatus.BAD_REQUEST, "존재하지 않는 회원입니다."),
    NOT_SENIOR_MEMBER(10101, HttpStatus.BAD_REQUEST, "시니어 회원만 접근할 수 있습니다."),
    NOT_JUNIOR_MEMBER(10102, HttpStatus.BAD_REQUEST, "주니어 회원만 접근할 수 있습니다."),

    // Member Authority
    NOT_EXIST_MEMBER(10100, HttpStatus.BAD_REQUEST, "존재하지 않는 회원입니다."),
    NOT_SENIOR_MEMBER(10101, HttpStatus.BAD_REQUEST, "시니어 회원만 접근할 수 있습니다."),
    NOT_JUNIOR_MEMBER(10102, HttpStatus.BAD_REQUEST, "주니어 회원만 접근할 수 있습니다."),

    INTERNAL_ERROR(20000, HttpStatus.INTERNAL_SERVER_ERROR, "Internal error"),
    DATA_ACCESS_ERROR(20001, HttpStatus.INTERNAL_SERVER_ERROR, "Data access error");

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