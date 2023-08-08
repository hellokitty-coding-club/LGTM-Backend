package swm.hkcc.LGTM.app.global.cache;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CacheKey {
    PERMANENT("permanent", 0),
    ONE_MONTH("one_month", 60 * 60 * 24 * 30),
    ONE_WEEK("one_week", 60 * 60 * 24 * 7),
    ONE_DAY("one_day", 60 * 60 * 24),
    FIVE_HOUR("five_hour", 60 * 60 * 5),
    ONE_HOUR("one_hour", 60 * 60),
    TEN_MIN("ten_min", 60 * 10),
    FIVE_MIN("five_min", 60 * 5),
    ONE_MIN("one_min", 60 * 1)
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
