package swm.hkcc.LGTM.app.modules.registration.exception;

import swm.hkcc.LGTM.app.global.constant.ResponseCode;
import swm.hkcc.LGTM.app.global.exception.GeneralException;

public class TooManyLockError extends GeneralException {
    public TooManyLockError() {
        super(ResponseCode.TOO_MANY_LOCK_ERROR);
    }
}
