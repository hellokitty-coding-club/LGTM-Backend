package swm.hkcc.LGTM.app.modules.serverDrivenUI.exception;

import swm.hkcc.LGTM.app.global.constant.ResponseCode;
import swm.hkcc.LGTM.app.global.exception.GeneralException;

public class UserABTestGroupNotFound extends GeneralException {
    public UserABTestGroupNotFound() {
        super(ResponseCode.USER_AB_TEST_GROUP_NOT_FOUND);
    }
}
