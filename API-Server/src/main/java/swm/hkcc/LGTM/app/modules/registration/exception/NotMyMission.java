package swm.hkcc.LGTM.app.modules.registration.exception;

import swm.hkcc.LGTM.app.global.constant.ResponseCode;
import swm.hkcc.LGTM.app.global.exception.GeneralException;

public class NotMyMission extends GeneralException {
    public NotMyMission(){
        super(ResponseCode.NOT_MY_MISSION);
    }
}
