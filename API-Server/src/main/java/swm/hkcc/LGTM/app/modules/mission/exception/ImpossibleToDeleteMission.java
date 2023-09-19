package swm.hkcc.LGTM.app.modules.mission.exception;

import swm.hkcc.LGTM.app.global.constant.ResponseCode;
import swm.hkcc.LGTM.app.global.exception.GeneralException;

public class ImpossibleToDeleteMission extends GeneralException {
    public ImpossibleToDeleteMission() {
        super(ResponseCode.IMPOSSIBLE_TO_DELETE);
    }
}
