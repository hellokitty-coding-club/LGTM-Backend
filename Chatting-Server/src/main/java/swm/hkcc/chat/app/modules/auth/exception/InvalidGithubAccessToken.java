package swm.hkcc.chat.app.modules.auth.exception;

import swm.hkcc.chat.app.global.constant.ResponseCode;
import swm.hkcc.chat.app.global.exception.GeneralException;

public class InvalidGithubAccessToken extends GeneralException {
    public InvalidGithubAccessToken() {
        super(ResponseCode.INVALID_GITHUB_ACCESS_TOKEN);
    }
}
