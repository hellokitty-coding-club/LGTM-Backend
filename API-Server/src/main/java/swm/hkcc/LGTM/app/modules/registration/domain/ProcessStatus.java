package swm.hkcc.LGTM.app.modules.registration.domain;

import lombok.Getter;

@Getter
public enum ProcessStatus {
    WAITING_FOR_PAYMENT("입금 대기중", 0, "리뷰이의 입금을 대기중이에요.", "입금 완료 알리기"),
    PAYMENT_CONFIRMATION("입금 확인중", 1, "입금 확인하기", "입금 확인 대기중"),
    MISSION_PROCEEDING("미션 수행중", 2, "리뷰이가 미션 수행중이에요.", "리뷰 요청하기"),
    CODE_REVIEW("코드 리뷰 작성", 3, "미션 리뷰 완료", ""),
    MISSION_FINISHED("미션 완료!", 4, "미션이 완료되었어요. \uD83C\uDF89", "후기 작성하기"),
    FEEDBACK_REVIEWED("리뷰 완료!", 5, "후기 보러가기", "후기 보거가기");

    private final String status;
    private final int sequence;
    private final String seniorBottomTitle;
    private final String juniorBottomTitle;

    ProcessStatus(String status, int sequence, String seniorBottomTitle, String juniorBottomTitle) {
        this.status = status;
        this.sequence = sequence;
        this.seniorBottomTitle = seniorBottomTitle;
        this.juniorBottomTitle = juniorBottomTitle;
    }

    public boolean isPayed() {
        return this.sequence >= MISSION_PROCEEDING.sequence;
    }

    public boolean isPullRequestCreated() {
        return this.sequence >= CODE_REVIEW.sequence;
    }
}
