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
