package swm.hkcc.chat.app.modules.member.exception;

import swm.hkcc.chat.app.global.constant.ResponseCode;
import swm.hkcc.chat.app.global.exception.GeneralException;

public class NotSeniorMember extends GeneralException {
    public NotSeniorMember(){
        super(ResponseCode.NOT_SENIOR_MEMBER);
    }
}
