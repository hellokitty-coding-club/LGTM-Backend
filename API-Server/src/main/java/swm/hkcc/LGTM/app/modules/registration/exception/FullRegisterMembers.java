package swm.hkcc.LGTM.app.modules.registration.exception;

import swm.hkcc.LGTM.app.global.constant.ResponseCode;
import swm.hkcc.LGTM.app.global.exception.GeneralException;

public class FullRegisterMembers extends GeneralException {
    public FullRegisterMembers(){
        super(ResponseCode.FULL_REGISTER_MEMBERS);
    }
}
