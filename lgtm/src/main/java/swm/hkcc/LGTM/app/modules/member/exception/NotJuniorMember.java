package swm.hkcc.LGTM.app.modules.member.exception;

import swm.hkcc.LGTM.app.global.constant.ResponseCode;
import swm.hkcc.LGTM.app.global.exception.GeneralException;

public class NotJuniorMember extends GeneralException {
    public NotJuniorMember(){
        super(ResponseCode.NOT_JUNIOR_MEMBER);
    }
}
