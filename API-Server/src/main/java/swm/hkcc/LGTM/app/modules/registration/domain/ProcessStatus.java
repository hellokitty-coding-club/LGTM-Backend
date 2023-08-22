package swm.hkcc.LGTM.app.modules.registration.domain;

import lombok.Getter;

@Getter
public enum ProcessStatus {

    WAITING_FOR_PAYMENT("입금 대기중", false, false, "입금하기"),
    PAYMENT_CONFIRMATION("입금 확인중", false, false, "입금 확인중"),
    MISSION_PROCEEDING("미션 수행중", true, false, "미션 수행"),
    CODE_REVIEW("코드 리뷰 작성", true, true, "코드 리뷰 작성"),
    MISSION_FINISHED("미션 완료!",  true, true, "미션 완료!"),
    FEEDBACK_REVIEWED("리뷰 완료!", true, true, "리뷰 완료!");

    private final String status;
    private final boolean isPayed;
    private final boolean isPullRequestCreated;
    private final String buttonMessage;

    ProcessStatus(String status, boolean isPayed, boolean isPullRequestCreated, String buttonMessage) {
        this.status = status;
        this.isPayed = isPayed;
        this.isPullRequestCreated = isPullRequestCreated;
        this.buttonMessage = buttonMessage;
    }
}
