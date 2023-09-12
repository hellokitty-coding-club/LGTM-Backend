package swm.hkcc.LGTM.app.modules.registration.exception;

import swm.hkcc.LGTM.app.global.constant.ResponseCode;
import swm.hkcc.LGTM.app.global.exception.GeneralException;

public class NotRegisteredMissionInternal extends GeneralException {
    public NotRegisteredMissionInternal() {
        super(ResponseCode.NOT_REGISTERED_MISSION_INTERNAL);
    }
}
