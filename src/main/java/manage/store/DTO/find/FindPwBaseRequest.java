package manage.store.DTO.find;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import manage.store.consts.Const;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FindPwBaseRequest {

    @NotBlank
    protected String userId;

    @Email(regexp = Const.emailRegex)
    @NotBlank
    protected String email;

}

