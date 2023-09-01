package swm.hkcc.LGTM.app.modules.registration.exception;

import swm.hkcc.LGTM.app.global.constant.ResponseCode;
import swm.hkcc.LGTM.app.global.exception.GeneralException;

public class NotRegisteredMission extends GeneralException {
    public NotRegisteredMission() {
        super(ResponseCode.NOT_REGISTERED_MISSION);
    }
}
