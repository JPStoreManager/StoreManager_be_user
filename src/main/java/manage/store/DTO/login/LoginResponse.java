package manage.store.DTO.login;

import lombok.Data;

@Data
public class LoginResponse {
    private final boolean logResult;
    private final String msg;

    public LoginResponse(boolean isSuccess) {
        this.logResult = isSuccess;
        // TODO 변경 필요
        this.msg = isSuccess ? "로그인 성공" : "로그인 실패";
    }
}
