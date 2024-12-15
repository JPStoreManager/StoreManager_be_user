package manage.store.DTO.login;

import lombok.Data;
import manage.store.consts.Message;
import manage.store.consts.SuccessFlag;

@Data
public class LoginResponse {
    private final SuccessFlag loginResult;
    private final String msg;

    public LoginResponse(SuccessFlag isSuccess) {
        this.loginResult = isSuccess;
        this.msg = isSuccess == SuccessFlag.Y ? Message.LOGIN_SUCCESS : Message.LOGIN_FAIL_NOT_EXIST_USER;
    }
}
