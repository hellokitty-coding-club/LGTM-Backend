package swm.hkcc.LGTM.app.modules.registration.domain;

import lombok.Getter;

@Getter
public enum PersonalStatus {

    WAITING_FOR_PAYMENT("입금 대기중"),
    PAYMENT_CONFIRMATION("입금 확인중"),
    MISSION_PROCEEDING("미션 수행"),
    CODE_REVIEW("코드 리뷰 작성"),
    MISSION_FINISHED("미션 완료!");

    private final String status;

    PersonalStatus(String status) {
        this.status = status;
    }
}
