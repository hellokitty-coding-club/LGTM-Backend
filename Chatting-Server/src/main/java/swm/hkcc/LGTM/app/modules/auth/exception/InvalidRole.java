package swm.hkcc.LGTM.app.modules.auth.exception;

import swm.hkcc.LGTM.app.global.constant.ResponseCode;
import swm.hkcc.LGTM.app.global.exception.GeneralException;

public class InvalidRole extends GeneralException {
    public InvalidRole() {
        super(ResponseCode.INVALID_ROLE);
    }
}
