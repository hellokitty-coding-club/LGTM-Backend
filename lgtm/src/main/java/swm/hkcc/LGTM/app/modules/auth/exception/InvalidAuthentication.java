package swm.hkcc.LGTM.app.modules.auth.exception;

import swm.hkcc.LGTM.app.global.constant.ResponseCode;
import swm.hkcc.LGTM.app.global.exception.GeneralException;

public class InvalidAuthentication extends GeneralException {
    public InvalidAuthentication() {
        super(ResponseCode.INVALID_AUTHENTICATION);
    }
}
