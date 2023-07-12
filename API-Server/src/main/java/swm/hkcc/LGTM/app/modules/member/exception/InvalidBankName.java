package swm.hkcc.LGTM.app.modules.member.exception;

import swm.hkcc.LGTM.app.global.constant.ResponseCode;
import swm.hkcc.LGTM.app.global.exception.GeneralException;

public class InvalidBankName extends GeneralException {
    public InvalidBankName(){
        super(ResponseCode.INVALID_BANK_NAME);
    }
}
