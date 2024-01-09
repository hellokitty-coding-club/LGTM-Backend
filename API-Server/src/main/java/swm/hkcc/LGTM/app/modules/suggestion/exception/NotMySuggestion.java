package swm.hkcc.LGTM.app.modules.suggestion.exception;

import swm.hkcc.LGTM.app.global.constant.ResponseCode;
import swm.hkcc.LGTM.app.global.exception.GeneralException;

public class NotMySuggestion extends GeneralException {
    public NotMySuggestion() {
        super(ResponseCode.NOT_MY_SUGGESTION);
    }
}
