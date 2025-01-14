package manage.store.DTO.find;

import manage.store.DTO.common.BaseResponse;
import manage.store.consts.SuccessFlag;

public class FindPwValidateOtpResponse extends BaseResponse {

    public FindPwValidateOtpResponse(SuccessFlag isSuccess, String msg) {
        super(isSuccess, msg);
    }

}
