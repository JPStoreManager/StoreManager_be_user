package manage.store.DTO.login;

import manage.store.DTO.base.BaseResponse;
import manage.store.consts.Message;
import manage.store.consts.SuccessFlag;


public class LoginResponse extends BaseResponse {

    public LoginResponse(SuccessFlag isSuccess) {
        super(isSuccess, isSuccess == SuccessFlag.Y ? Message.LOGIN_SUCCESS : Message.LOGIN_FAIL_NOT_EXIST_USER);
    }

}
