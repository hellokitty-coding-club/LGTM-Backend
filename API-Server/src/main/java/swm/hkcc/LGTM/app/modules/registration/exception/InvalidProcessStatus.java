package swm.hkcc.LGTM.app.modules.registration.exception;

import swm.hkcc.LGTM.app.global.constant.ResponseCode;
import swm.hkcc.LGTM.app.global.exception.GeneralException;
import swm.hkcc.LGTM.app.modules.registration.domain.ProcessStatus;

public class InvalidProcessStatus extends GeneralException {
    public InvalidProcessStatus() {
        super(ResponseCode.INVALID_PROCESS_STATUS);
    }
    public InvalidProcessStatus(ProcessStatus currentStatus) {
        super(
                ResponseCode.INVALID_PROCESS_STATUS,
                ResponseCode.INVALID_PROCESS_STATUS.getMessage() + "현재 상태: " + currentStatus
        );
    }
}
