package swm.hkcc.LGTM.app.modules.registration.exception;

import swm.hkcc.LGTM.app.global.constant.ResponseCode;
import swm.hkcc.LGTM.app.global.exception.GeneralException;

public class MissRegisterDeadline extends GeneralException {
    public MissRegisterDeadline() {
        super(ResponseCode.MISS_REGISTER_DEADLINE);
    }
}
