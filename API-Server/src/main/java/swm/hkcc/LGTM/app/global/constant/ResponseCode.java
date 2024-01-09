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

    // 1xxxx : API Error
    // 100xx : Auth/SignIn
    INVALID_GITHUB_ACCESS_TOKEN(10003, HttpStatus.BAD_REQUEST, "유효하지 않은 Github Access Token입니다. 다시 시도해주세요."),
    DUPLICATE_NICK_NAME(10004, HttpStatus.BAD_REQUEST, "중복된 닉네임입니다. 다른 닉네임을 사용해주세요."),
    INVALID_TECH_TAG(10005, HttpStatus.BAD_REQUEST, "유효하지 않은 기술 태그입니다. 다시 시도해주세요."),
    INVALID_CAREER_PERIOD(10006, HttpStatus.BAD_REQUEST, "유효하지 않은 경력 기간입니다. 경력 기간은 최소 1달 이상이어야 합니다."),
    INVALID_BANK_NAME(10007, HttpStatus.BAD_REQUEST, "유효하지 않은 은행 이름입니다. 다시 시도해주세요."),

    // 101xx : Member Authority
    NOT_EXIST_MEMBER(10100, HttpStatus.BAD_REQUEST, "존재하지 않는 회원입니다."),
    NOT_SENIOR_MEMBER(10101, HttpStatus.BAD_REQUEST, "시니어 회원만 접근할 수 있습니다."),
    NOT_JUNIOR_MEMBER(10102, HttpStatus.BAD_REQUEST, "주니어 회원만 접근할 수 있습니다."),

    // 102xx : Mission
    NOT_EXIST_MISSION(10200, HttpStatus.BAD_REQUEST, "존재하지 않는 미션입니다."),
    INVALID_GITHUB_URL(10201, HttpStatus.BAD_REQUEST, "유효하지 않은 Github Repository URL입니다. public repository인 github 주소인지 확인해주세요."),
    IMPOSSIBLE_TO_DELETE(10202, HttpStatus.BAD_REQUEST, "이미 참여한 사람이 있어 삭제할 수 없는 미션입니다."),
    USER_AB_TEST_GROUP_NOT_FOUND(10203, HttpStatus.INTERNAL_SERVER_ERROR, "사용자의 AB Test Group을 찾을 수 없습니다."),

    // 103xx : Mission Registration
    MISS_REGISTER_DEADLINE(10300, HttpStatus.BAD_REQUEST, "미션 등록 기간이 이미 마감되었습니다."),
    FULL_REGISTER_MEMBERS(10301, HttpStatus.BAD_REQUEST, "미션 등록 인원이 모두 찼습니다."),
    ALREADY_REGISTERED_MISSION(10302, HttpStatus.BAD_REQUEST, "이미 참가한 미션입니다."),
    TOO_MANY_LOCK_ERROR(10303, HttpStatus.INTERNAL_SERVER_ERROR, "대기시간이 만료되어 미션 참가에 실패했습니다."),
    NOT_MY_MISSION(10304, HttpStatus.BAD_REQUEST, "접근 권한이 없는 미션입니다."),
    NOT_REGISTERED_MISSION(10305, HttpStatus.BAD_REQUEST, "주니어가 참가하지 않은 미션입니다."),
    NOT_REGISTERED_MISSION_INTERNAL(10306, HttpStatus.INTERNAL_SERVER_ERROR, "DB 조회 과정에서 문제가 발생했습니다: 주니어가 참가하지 않은 미션입니다."),
    INVALID_PROCESS_STATUS(10307, HttpStatus.BAD_REQUEST, "현재 진행상태에서 수행할 수 없는 작업입니다."),


    // 104xx : 미션 제안하기
    NOT_EXIST_SUGGESTION(10400, HttpStatus.BAD_REQUEST, "존재하지 않는 게시글입니다."),
    NOT_MY_SUGGESTION(10401, HttpStatus.BAD_REQUEST, "수정 권한이 없는 게시글입니다."),

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