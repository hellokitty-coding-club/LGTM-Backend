package swm.hkcc.LGTM.app.modules.member.exception;

import swm.hkcc.LGTM.app.global.constant.ResponseCode;
import swm.hkcc.LGTM.app.global.exception.GeneralException;

public class InvalidCareerPeriod extends GeneralException {
    public InvalidCareerPeriod(){
        super(ResponseCode.INVALID_CAREER_PERIOD);
    }
}
