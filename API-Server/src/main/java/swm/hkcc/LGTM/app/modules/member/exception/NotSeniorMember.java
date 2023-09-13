package swm.hkcc.LGTM.app.modules.member.exception;

import swm.hkcc.LGTM.app.global.constant.ResponseCode;
import swm.hkcc.LGTM.app.global.exception.GeneralException;

public class NotSeniorMember extends GeneralException {
    public NotSeniorMember(){
        super(ResponseCode.NOT_SENIOR_MEMBER);
    }
}
