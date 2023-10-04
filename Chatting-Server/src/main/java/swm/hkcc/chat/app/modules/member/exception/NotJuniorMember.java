package swm.hkcc.chat.app.modules.member.exception;

import swm.hkcc.chat.app.global.constant.ResponseCode;
import swm.hkcc.chat.app.global.exception.GeneralException;

public class NotJuniorMember extends GeneralException {
    public NotJuniorMember(){
        super(ResponseCode.NOT_JUNIOR_MEMBER);
    }
}
