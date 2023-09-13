package swm.hkcc.LGTM.app.modules.registration.exception;

import swm.hkcc.LGTM.app.global.constant.ResponseCode;
import swm.hkcc.LGTM.app.global.exception.GeneralException;

public class AlreadyRegisteredMission extends GeneralException {
    public AlreadyRegisteredMission() {
        super(ResponseCode.ALREADY_REGISTERED_MISSION);
    }
}
