package swm.hkcc.LGTM.app.modules.auth.exception;

import swm.hkcc.LGTM.app.global.constant.ResponseCode;
import swm.hkcc.LGTM.app.global.exception.GeneralException;

public class InvalidTechTag extends GeneralException {
    public InvalidTechTag() {
        super(ResponseCode.INVALID_TECH_TAG);
    }
}