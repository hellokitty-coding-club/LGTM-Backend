package swm.hkcc.LGTM.app.modules.auth.exception;

import swm.hkcc.LGTM.app.global.constant.ResponseCode;
import swm.hkcc.LGTM.app.global.exception.GeneralException;

public class InvalidGithubAccessToken extends GeneralException {
    public InvalidGithubAccessToken() {
        super(ResponseCode.INVALID_GITHUB_ACCESS_TOKEN);
    }
}
