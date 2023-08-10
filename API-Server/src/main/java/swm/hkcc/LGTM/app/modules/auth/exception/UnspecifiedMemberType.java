package swm.hkcc.LGTM.app.modules.auth.exception;

import swm.hkcc.LGTM.app.global.constant.ResponseCode;
import swm.hkcc.LGTM.app.global.exception.GeneralException;

public class UnspecifiedMemberType extends GeneralException {
    public UnspecifiedMemberType() {
        super(ResponseCode.UNSPECIFIED_MEMBER_TYPE);
    }
}
