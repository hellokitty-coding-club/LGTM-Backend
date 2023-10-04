package swm.hkcc.chat.app.modules.auth.exception;

import swm.hkcc.chat.app.global.constant.ResponseCode;
import swm.hkcc.chat.app.global.exception.GeneralException;

public class InvalidAuthentication extends GeneralException {
    public InvalidAuthentication() {
        super(ResponseCode.INVALID_AUTHENTICATION);
    }
}
