package swm.hkcc.LGTM.app.modules.mission.exception;

import swm.hkcc.LGTM.app.global.constant.ResponseCode;
import swm.hkcc.LGTM.app.global.exception.GeneralException;

public class NotExistMission extends GeneralException {
    public NotExistMission() {
        super(ResponseCode.NOT_EXIST_MISSION);
    }
}