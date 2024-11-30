package manage.store.DTO.login;

import lombok.Data;
import manage.store.consts.SuccessFlag;

@Data
public class LoginResponse {
    private final SuccessFlag loginResult;
    private final String msg;

    public LoginResponse(SuccessFlag isSuccess) {
        this.loginResult = isSuccess;
        // TODO 변경 필요
        this.msg = isSuccess == SuccessFlag.Y ? "로그인 성공" : "로그인 실패";
    }
}
