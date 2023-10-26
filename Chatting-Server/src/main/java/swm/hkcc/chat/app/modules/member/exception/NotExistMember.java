package swm.hkcc.chat.app.modules.member.exception;

import swm.hkcc.chat.app.global.constant.ResponseCode;
import swm.hkcc.chat.app.global.exception.GeneralException;

public class NotExistMember extends GeneralException {
    public NotExistMember(){
        super(ResponseCode.NOT_EXIST_MEMBER);
    }
}
