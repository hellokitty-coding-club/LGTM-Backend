package swm.hkcc.chat.app.modules.auth.exception;

import swm.hkcc.chat.app.global.constant.ResponseCode;
import swm.hkcc.chat.app.global.exception.GeneralException;

public class InvalidRole extends GeneralException {
    public InvalidRole() {
        super(ResponseCode.INVALID_ROLE);
    }
}
