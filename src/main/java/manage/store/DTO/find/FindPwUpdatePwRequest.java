package manage.store.DTO.find;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindPwUpdatePwRequest extends FindPwBaseRequest{

    @NotBlank
    private String newPassword;

    public FindPwUpdatePwRequest(String newPassword, String userId, String email) {
        super(userId, email);
        this.newPassword = newPassword;
    }

    public FindPwUpdatePwRequest() {
        super();
    }

}