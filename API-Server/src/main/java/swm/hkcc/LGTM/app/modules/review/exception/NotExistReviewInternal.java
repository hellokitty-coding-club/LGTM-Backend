package swm.hkcc.LGTM.app.modules.review.exception;

import swm.hkcc.LGTM.app.global.constant.ResponseCode;
import swm.hkcc.LGTM.app.global.exception.GeneralException;

public class NotExistReviewInternal extends GeneralException {
    public NotExistReviewInternal() {
        super(ResponseCode.NOT_EXIST_REVIEW_INTERNAL);
    }
}
