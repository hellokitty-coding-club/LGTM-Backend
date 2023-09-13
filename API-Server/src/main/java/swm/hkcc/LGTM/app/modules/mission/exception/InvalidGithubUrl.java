package swm.hkcc.LGTM.app.modules.mission.exception;

import swm.hkcc.LGTM.app.global.constant.ResponseCode;
import swm.hkcc.LGTM.app.global.exception.GeneralException;

public class InvalidGithubUrl extends GeneralException {
    public InvalidGithubUrl() {
        super(ResponseCode.INVALID_GITHUB_URL);
    }
}
