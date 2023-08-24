package swm.hkcc.LGTM.app.modules.registration.domain;

import lombok.Getter;

@Getter
public enum ProcessStatus {
    WAITING_FOR_PAYMENT("입금 대기중", 0, "입금하기"),
    PAYMENT_CONFIRMATION("입금 확인중", 1, "입금 확인중"),
    MISSION_PROCEEDING("미션 수행중", 2, "미션 수행"),
    CODE_REVIEW("코드 리뷰 작성", 3, "코드 리뷰 작성"),
    MISSION_FINISHED("미션 완료!", 4, "미션 완료!"),
    FEEDBACK_REVIEWED("리뷰 완료!", 5, "리뷰 완료!");

    private final String status;
    private final int sequence;
    private final String buttonMessage;

    ProcessStatus(String status, int sequence, String buttonMessage) {
        this.status = status;
        this.sequence = sequence;
        this.buttonMessage = buttonMessage;
    }

    public boolean isPayed() {
        return this.sequence >= MISSION_PROCEEDING.sequence;
    }

    public boolean isPullRequestCreated() {
        return this.sequence >= CODE_REVIEW.sequence;
    }
}
