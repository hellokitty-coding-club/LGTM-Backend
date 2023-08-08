package swm.hkcc.LGTM.app.global.cache;

import lombok.RequiredArgsConstructor;
import static swm.hkcc.LGTM.app.global.cache.TTLKey.*;

@RequiredArgsConstructor
public enum CacheKey {
    TECH_TAG("tech_tag", ONE_MONTH.getExpiryTimeSec()),
    TECH_TAG_EXISTS("tech_tag_exists", ONE_MONTH.getExpiryTimeSec()),
    TECH_TAG_PER_MEMBER("tech_tag_per_member", ONE_DAY.getExpiryTimeSec()),
    TECH_TAG_PER_MISSION("tech_tag_per_mission", ONE_DAY.getExpiryTimeSec()),
    MISSION_PARTICIPANT_COUNT("mission_participant_count", ONE_DAY.getExpiryTimeSec()),
    ON_GOING_MISSIONS("on_going_missions", ONE_MIN.getExpiryTimeSec()),
    RECOMMENDED_MISSIONS("recommended_missions", ONE_MIN.getExpiryTimeSec()),
    TOTAL_MISSIONS("total_missions", ONE_MIN.getExpiryTimeSec()),
    MISSION_SCRAP_COUNT("mission_scrap_count", ONE_MIN.getExpiryTimeSec()),
    MISSION_VIEW_COUNT("mission_view_count", ONE_MIN.getExpiryTimeSec()),
    ;

    private final String key;
    private final int expiryTimeSec;

    public String getKey() {
        return key;
    }

    public int getExpiryTimeSec() {
        return expiryTimeSec;
    }

}
