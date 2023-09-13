package swm.hkcc.LGTM.app.modules.registration.exception;

import swm.hkcc.LGTM.app.global.constant.ResponseCode;
import swm.hkcc.LGTM.app.global.exception.GeneralException;

public class InvalidProcessStatus extends GeneralException {
    public InvalidProcessStatus() {
        super(ResponseCode.INVALID_PROCESS_STATUS);
    }
}
