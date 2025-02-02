package manage.store.DTO.login;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "올바른 로그인 정보를 입력하세요.")
    private String id;

    @NotBlank(message = "올바른 로그인 정보를 입력하세요.")
    private String password;

}
