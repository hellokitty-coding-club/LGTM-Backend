package swm.hkcc.LGTM.app.modules.auth.exception;

import swm.hkcc.LGTM.app.global.constant.ResponseCode;
import swm.hkcc.LGTM.app.global.exception.GeneralException;

public class DuplicateNickName extends GeneralException {
    public DuplicateNickName() {
        super(ResponseCode.DUPLICATE_NICK_NAME);
    }
}