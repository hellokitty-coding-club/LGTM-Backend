package swm.hkcc.chat.app.modules.auth.exception;

import swm.hkcc.chat.app.global.constant.ResponseCode;
import swm.hkcc.chat.app.global.exception.GeneralException;

public class UnspecifiedMemberType extends GeneralException {
    public UnspecifiedMemberType() {
        super(ResponseCode.UNSPECIFIED_MEMBER_TYPE);
    }
}
