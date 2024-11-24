package manage.store.DTO.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
@NoArgsConstructor
public class LoginRequest {

    @NotEmpty(message = "아이디를 입력하세요.")
    private String id;

    @NotEmpty(message = "비밀번호를 입력하세요.")
    private String password;

}
