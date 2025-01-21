package manage.store.DTO.find;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import manage.store.annotation.UserEmail;

@NoArgsConstructor
@Getter
@Setter
public class FindPwBaseRequest {

    @NotBlank
    protected String userId;

    @UserEmail
    protected String email;

}

