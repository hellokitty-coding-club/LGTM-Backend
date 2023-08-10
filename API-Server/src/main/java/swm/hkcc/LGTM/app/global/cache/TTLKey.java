package swm.hkcc.LGTM.app.global.cache;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum TTLKey {
    PERMANENT(60 * 60 * 24 * 365),
    ONE_MONTH(60 * 60 * 24 * 30),
    ONE_WEEK(60 * 60 * 24 * 7),
    ONE_DAY(60 * 60 * 24),
    FIVE_HOUR(60 * 60 * 5),
    ONE_HOUR(60 * 60),
    TEN_MIN(60 * 10),
    FIVE_MIN(60 * 5),
    ONE_MIN(60 * 1)
    ;

    private final int expiryTimeSec;

    public int getExpiryTimeSec() {
        return expiryTimeSec;
    }
}
