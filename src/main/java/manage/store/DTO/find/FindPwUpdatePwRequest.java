package manage.store.DTO.find;

import lombok.Getter;
import lombok.Setter;
import manage.store.annotation.NewPassword;

@Getter
@Setter
public class FindPwUpdatePwRequest extends FindPwBaseRequest{

    @NewPassword
    private String newPassword;

    public FindPwUpdatePwRequest() {
        super();
    }

}