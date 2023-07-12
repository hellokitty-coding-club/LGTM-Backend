package swm.hkcc.LGTM.app.modules.mission.domain;

public enum MissionStatus {
    RECRUITING("참가자 모집중"),
    MISSION_PROCEEDING("미션 진행중"),
    MISSION_FINISHED("미션 종료");

    private final String status;

    MissionStatus(String status) {
        this.status = status;
    }

    // todo : 검증로직 필요
}
