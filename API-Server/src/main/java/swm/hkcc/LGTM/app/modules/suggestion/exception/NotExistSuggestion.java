package swm.hkcc.LGTM.app.modules.suggestion.exception;

import swm.hkcc.LGTM.app.global.constant.ResponseCode;
import swm.hkcc.LGTM.app.global.exception.GeneralException;

public class NotExistSuggestion extends GeneralException {
    public NotExistSuggestion() {
        super(ResponseCode.NOT_EXIST_SUGGESTION);
    }
}
