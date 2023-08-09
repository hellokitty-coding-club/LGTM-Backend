package swm.hkcc.LGTM.app.modules.member.exception;

import swm.hkcc.LGTM.app.global.constant.ResponseCode;
import swm.hkcc.LGTM.app.global.exception.GeneralException;

public class NotExistMember extends GeneralException {
    public NotExistMember(){
        super(ResponseCode.NOT_EXIST_MEMBER);
    }
}
