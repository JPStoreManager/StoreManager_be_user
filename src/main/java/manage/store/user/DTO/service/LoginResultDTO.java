package manage.store.user.DTO.service;

import lombok.Data;

@Data
public class LoginResultDTO {
    private final boolean isSuccess;
    private final String msg;

    public LoginResultDTO(boolean isSuccess) {
        this.isSuccess = isSuccess;
        this.msg = "Fail to login...";
    }
}
